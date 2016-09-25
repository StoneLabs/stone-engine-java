package StoneEngine.Rendering;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;

import java.util.ArrayList;
import java.util.HashMap;

import StoneEngine.Math.Vector3f;
import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Scene.GameObject;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Rendering.Camera;
import StoneLabs.sutil.Debug;

public class RenderingEngine
{
	private Camera mainCamera;
	
	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;
	
	private HashMap<String, Integer> samplerMap;
	
	private HashMap<String, Vector3f> vector3fHashMap;
	private HashMap<String, Float> floatHashMap;
	
	private Shader ambientShader;

	public RenderingEngine()
	{
		Debug.Log("OpenGL " + RenderingEngine.getOpenGLVersion());
		
		lights = new ArrayList<BaseLight>();
		samplerMap = new HashMap<String, Integer>();

		vector3fHashMap = new HashMap<String, Vector3f>();
		floatHashMap = new HashMap<String, Float>();
		
		vector3fHashMap.put("ambient", new Vector3f(0.1f, 0.1f, 0.1f));
		
		samplerMap.put("diffuse", 0);
		samplerMap.put("normalMap", 1);
		
		ambientShader = ResourceLoader.loadShader("shaders\\forward-ambient.shader");
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glEnable(GL_TEXTURE_2D);
//		glEnable(GL_FRAMEBUFFER_SRGB);
	}
		
	public void render(GameObject object)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Shader forwardAmbient = ambientShader;
		
		{ //Actual render pipeline
			object.render(forwardAmbient, this); //Initial render cycle
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glDepthMask(false);
			glDepthFunc(GL_EQUAL);
			
			//BLENDING ZONE
			for (BaseLight light : lights)
			{
				activeLight = light;

				object.render(light.getShader(), this); //Blending cycle
			}
			//BLENDING ZONE

			glDepthFunc(GL_LESS);
			glDepthMask(true);
			glDisable(GL_BLEND);
		}
	}

	public int getSamplerSlot(String samplerName) 
	{ 
		if (!samplerMap.containsKey(samplerName)) Debug.Error("Sampler slot could not be found: " + samplerName);
		return samplerMap.get(samplerName); 
	}
	
	public static String getOpenGLVersion()	{ return glGetString(GL_VERSION); }
	public Camera getMainCamera()			{ return mainCamera; }	
	public BaseLight getActiveLight()		{ return activeLight; }
	
	public void addCamera(Camera camera)	{ this.mainCamera = camera; }
	public void addLight(BaseLight light)	{ lights.add(light); }
	
	public Vector3f getVector3f (String name)
	{ 
		Vector3f ret = vector3fHashMap.get(name);
		if (ret == null)
			return Vector3f.NULL();
		return ret;
	}
	public Float getFloat(String name)
	{ 
		Float ret = floatHashMap.get(name);
		if (ret == null)
			return 0f;
		return ret;
	}
	
}
