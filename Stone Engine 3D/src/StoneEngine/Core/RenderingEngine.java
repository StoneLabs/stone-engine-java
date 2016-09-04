package StoneEngine.Core;

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

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Attenuation;
import StoneEngine.Rendering.BaseLight;
import StoneEngine.Rendering.Camera;
import StoneEngine.Rendering.DirectionalLight;
import StoneEngine.Rendering.ForwardAmbient;
import StoneEngine.Rendering.ForwardDirectional;
import StoneEngine.Rendering.ForwardPoint;
import StoneEngine.Rendering.ForwardSpot;
import StoneEngine.Rendering.PointLight;
import StoneEngine.Rendering.Shader;
import StoneEngine.Rendering.SpotLight;
import StoneEngine.Rendering.Window;
import StoneLabs.sutil.Debug;

public class RenderingEngine
{
	private Camera mainCamera;
	private Vector3f ambientLight;
	private DirectionalLight directionalLight;
	private PointLight pointLight;
	private SpotLight spotLight;
	

	public RenderingEngine()
	{
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
		directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1.0f,0f,0f), 0.4f), new Vector3f(1.0f,1.0f,1.0f));
		pointLight = new PointLight(new BaseLight(new Vector3f(0f, 0f, 1.0f), 0.8f), new Attenuation(0, 0, 1), new Vector3f(3, 0f, 0), 100);
		
		spotLight = new SpotLight(new PointLight(new BaseLight(new Vector3f(0,1,1), 0.4f),
				new Attenuation(0,0,0.1f),
				new Vector3f(0,0,20), 100),
				new Vector3f(1,0,0), 0.7f);
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
	
	public void render(GameObject object)
	{
		clearScreen();

		Shader forwardAmbient = ForwardAmbient.getInstance();
		Shader forwardDirectional = ForwardDirectional.getInstance();
		Shader forwardPoint = ForwardPoint.getInstance();
		Shader forwardSpot = ForwardSpot.getInstance();
		
		forwardAmbient.setRenderingEngine(this);
		forwardDirectional.setRenderingEngine(this);
		forwardPoint.setRenderingEngine(this);
		forwardSpot.setRenderingEngine(this);
		
		{ //Actual render pipeline
			object.render(forwardAmbient);
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glDepthMask(false);
			glDepthFunc(GL_EQUAL);
			
			//BLENDING ZONE
			object.render(forwardDirectional);
			object.render(forwardPoint);
			object.render(forwardSpot);
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
	
	public DirectionalLight getDirectionalLight()
	{
		return directionalLight;
	}
	
	public PointLight getPointLight()
	{
		return pointLight;
	}
	
	public SpotLight getSpotLight()
	{
		return spotLight;
	}
	
	
}
