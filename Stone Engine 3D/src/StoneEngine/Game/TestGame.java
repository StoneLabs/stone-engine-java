package StoneEngine.Game;

import StoneEngine.Core.Input;
import StoneEngine.Core.CoreEngine;
import StoneEngine.Core.Game;
import StoneEngine.Core.GameObject;
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

public class TestGame extends Game
{
	private Camera camera;
	
	private GameObject root;

	public void init()
	{
		root = new GameObject();
		camera = new Camera();
		
		
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;
		
		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
											new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
		
		int indices[] = { 0, 1, 2,
					      2, 1, 3};
		
		Mesh mesh = new Mesh(vertices, indices, true);
		
		Material material = new Material(
				ResourceLoader.loadTexture("test.png"),
				new Vector3f(1,1,1),
				0.4f, 8
				);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		root.addComponent(meshRenderer);
		root.getTransform().setTranslation(0, -1, 5);
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		Transform.setCamera(camera);
		
	}
	
	boolean mouseLocked = false;
	public void input()
	{
		root.input();
		
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
	}

	public void update()
	{
		root.update();
	}
	
	public void render() 
	{
		root.render();
	}
}
