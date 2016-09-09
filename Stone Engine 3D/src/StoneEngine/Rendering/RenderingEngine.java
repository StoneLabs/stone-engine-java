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
import static org.lwjgl.opengl.GL11.glBindTexture;
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

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Shading.ForwardAmbient;
import StoneEngine.Rendering.Shading.ForwardDirectional;
import StoneEngine.Rendering.Shading.ForwardPoint;
import StoneEngine.Rendering.Shading.ForwardSpot;
import StoneEngine.Rendering.Shading.Shader;
import StoneEngine.Scene.GameObject;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneLabs.sutil.Debug;

public class RenderingEngine
{
	private Camera mainCamera;
	private Vector3f ambientLight;
	
	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;

	public RenderingEngine()
	{
		lights = new ArrayList<BaseLight>();
		
		Debug.Log("OpenGL " + RenderingEngine.getOpenGLVersion());
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glEnable(GL_TEXTURE_2D);
//		glEnable(GL_FRAMEBUFFER_SRGB);
		
		mainCamera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000.0f);
		
		ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
	}
	
	public Vector3f getAmbientLight()
	{
		return ambientLight;
	}
	
	//Temp. hack
	public void input(float delta)
	{
//		spotLight.getPointLight().setPosition(mainCamera.getPos());
//		spotLight.setDirection(mainCamera.getForward());
		mainCamera.input(delta);
	}
	
//	private void clearLightList()
//	{
//		directionalLights.clear();
//		pointLights.clear();
//		spotLights.clear();
//	}
	
	public void render(GameObject object)
	{
		clearScreen();
		
		lights.clear();
		object.addToRenderingEngine(this); //Temp...

		Shader forwardAmbient = ForwardAmbient.getInstance();
		forwardAmbient.setRenderingEngine(this);
		
		{ //Actual render pipeline
			object.render(forwardAmbient); //Initial render cycle
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glDepthMask(false);
			glDepthFunc(GL_EQUAL);
			
			//BLENDING ZONE
			for (BaseLight light : lights)
			{
				light.getShader().setRenderingEngine(this);

				activeLight = light;

				object.render(light.getShader()); //Blending cycle
			}
			//BLENDING ZONE

			glDepthFunc(GL_LESS);
			glDepthMask(true);
			glDisable(GL_BLEND);
		}
	}	
	
	private static void clearScreen()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	private static void setTexture(boolean enabled)
	{
		if (enabled)
			glEnable(GL_TEXTURE_2D);
		else
			glDisable(GL_TEXTURE_2D);
	}
	
	private static void unbindTextures()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private static void setClearcolor(Vector3f color)
	{
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}
	
	public static String getOpenGLVersion()
	{
		return glGetString(GL_VERSION);
	}

	public Camera getMainCamera()
	{
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera)
	{
		this.mainCamera = mainCamera;
	}
	
//	public DirectionalLight getDirectionalLight()
//	{
//		return activeDirectionalLight;
//	}
//	
//	public PointLight getPointLight()
//	{
//		return activePointLight;
//	}
//	
//	public SpotLight getSpotLight()
//	{
//		return activeSpotLight;
//	}
//	
//	public void addDirectionalLight(DirectionalLight directionalLight)
//	{
//		directionalLights.add(directionalLight);
//	}
//	
//	public void addPointLight(PointLight pointLight)
//	{
//		pointLights.add(pointLight);
//	}
//	
//	public void addSpotLight(SpotLight spotLight)
//	{
//		spotLights.add(spotLight);
//	}
	
	public void addLight(BaseLight light)
	{
		lights.add(light);
	}
	
	public BaseLight getActiveLight()
	{
		return activeLight;
	}
}
