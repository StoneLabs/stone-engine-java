package StoneEngine.Game;

import StoneEngine.Core.Input;
import StoneEngine.Core.CoreEngine;
import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Time;
import StoneEngine.Core.Transform;
import StoneEngine.Core.Vector2f;
import StoneEngine.Core.Vector3f;
import StoneEngine.Rendering.Attenuation;
import StoneEngine.Rendering.BaseLight;
import StoneEngine.Rendering.Camera;
import StoneEngine.Rendering.DirectionalLight;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.Mesh;
import StoneEngine.Rendering.PhongShader;
import StoneEngine.Rendering.PointLight;
import StoneEngine.Rendering.Shader;
import StoneEngine.Rendering.SpotLight;
import StoneEngine.Rendering.Vertex;
import StoneEngine.Rendering.Window;
import StoneLabs.sutil.Debug;

public class TestGame implements Game
{
	private Mesh mesh;
	private Shader shader;
	private Transform transform;
	
	private Material material;

	private Camera camera;

	PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1,0,0), 0.8f), new Attenuation(0, 0, 1), new Vector3f(-2,2,5),10);
	PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0,0,1), 0.8f), new Attenuation(0, 0, 1), new Vector3f(2,2,7),10);
	
	SpotLight sLight1 = new SpotLight(
			new PointLight(new BaseLight(new Vector3f(0,1,1), 0.8f), new Attenuation(0, 0, 0.01f), new Vector3f(-2,0,5),500),
			new Vector3f(1,1,1), 0.7f);
	
	public void init()
	{
		mesh = new Mesh();
//		mesh = ResourceLoader.loadMesh("monkey.obj");
		material = new Material(
				ResourceLoader.loadTexture("test.png"),
				new Vector3f(1,1,1),
				0.4f, 8
				);
		shader = PhongShader.getInstance();
		camera = new Camera();
		transform = new Transform();
		
//		Vertex[] vertices = new Vertex[] {	
//										new Vertex(	new Vector3f(-1, -1, 0), new Vector2f(0		,0		)),
//										new Vertex(	new Vector3f( 0,  1, 0), new Vector2f(0.5f	,0		)),
//										new Vertex(	new Vector3f( 1, -1, 0), new Vector2f(1.0f	,0		)),
//										new Vertex( new Vector3f( 0, -1, 1), new Vector2f(0.5f	,1.0f	)),
//										};
//		
//		int[] indices = new int[] {	3,1,0,
//									2,1,3,
//									0,1,2,
//									0,2,3};
		
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;
		
		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
											new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
		
		int indices[] = { 0, 1, 2,
					      2, 1, 3};
		
		mesh.addVertices(vertices, indices, true);
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		Transform.setCamera(camera);
		
		PhongShader.setAmbientLight(new Vector3f(0.0f,0.0f,0.0f));
		PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1f,1f,1f), 0.2f), new Vector3f(1,1,1)));
		
		
		PhongShader.setPointLights(new PointLight[] {pLight1, pLight2});
		PhongShader.setSpotLights(new SpotLight[] {sLight1});
	}
	
	boolean mouseLocked = false;
	public void input()
	{
		Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
		
		float moveAmnt = (float)(10 * Time.getDelta());
		float rotAmnt = (float)(100 * Time.getDelta());
		float sensitivity = 0.2f;
		
		if (Input.getKey(Input.Keys.KEY_W))
			camera.move(camera.getForward(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_S))
			camera.move(camera.getForward(), -moveAmnt);
		if (Input.getKey(Input.Keys.KEY_A))
			camera.move(camera.getLeft(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_D))
			camera.move(camera.getRight(), moveAmnt);
		
		if (Input.getKey(Input.Keys.KEY_UP))
			camera.rotateX(-rotAmnt);
		if (Input.getKey(Input.Keys.KEY_DOWN))
			camera.rotateX(rotAmnt);
		if (Input.getKey(Input.Keys.KEY_LEFT))
			camera.rotateY(-rotAmnt);
		if (Input.getKey(Input.Keys.KEY_RIGHT))
			camera.rotateY(rotAmnt);
		

		if(Input.getKey(Input.Keys.KEY_ESCAPE))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}
//		Debug.Log("MB 0: " + Input.getMouse(0));
//		Debug.Log("MB 1: " + Input.getMouse(1));
//		Debug.Log("MB 2: " + Input.getMouse(2));
//		Debug.Log("MB 3: " + Input.getMouse(3));
//		Debug.Log("MB 4: " + Input.getMouse(4));
		if(Input.getMouseDown(0))
		{
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}
		if(mouseLocked)
		{
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				camera.rotateY(deltaPos.getX() * sensitivity);
			if(rotX)
				camera.rotateX(-deltaPos.getY() * sensitivity);
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
		}
		
		sLight1.getPointLight().setPosition(camera.getPos());
		sLight1.setDirection(camera.getForward());
	}

	
	float tmp = 0.0F;
	public void update()
	{
		tmp += Time.getDelta();
//
//		float sin = (float)Math.sin(tmp);
//		float posSin = Math.abs(sin);
		
		transform.setTranslation(0, -1, 5);
//		transform.setRotation(0,sin*180,0);
//		transform.setScale(posSin,posSin,posSin);
//		transform.setTranslation(0, 0, 5);
		
		pLight1.setPosition(new Vector3f(3,0,8.0f * (float)(Math.sin(tmp) + 0.5f)+10));
		pLight2.setPosition(new Vector3f(3,0,8.0f * (float)(Math.cos(tmp) + 0.5f)+10));
	}
	
	public void render() 
	{
		shader.bind();
		shader.updateUniforms(transform.getTanformation(), transform.getProjectedTransformation(), material);
		mesh.draw();
	}
}
