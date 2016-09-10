package StoneEngine.Game;

import StoneEngine.Core.Input;
import StoneEngine.Core.CoreEngine;
import StoneEngine.Core.Game;
import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Time;
import StoneEngine.Math.Quaternion;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Math.Vertex;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.Mesh;
import StoneEngine.Rendering.Window;
import StoneEngine.Rendering.Shading.Shader;
import StoneEngine.Scene.GameObject;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.DirectionalLight;
import StoneEngine.Scene.Lighting.PointLight;
import StoneEngine.Scene.Lighting.SpotLight;
import StoneEngine.Scene.Rendering.Camera;
import StoneEngine.Scene.Rendering.MeshRenderer;
import StoneLabs.sutil.Debug;

@SuppressWarnings("unused") //TODO REMOVE
public class TestGame extends Game
{
	@Override
	public void init()
	{
		//Creating components
		
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;

		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
				new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
				new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
				new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};

		int indices[] = { 0, 1, 2,
				2, 1, 3};

		Mesh mesh = new Mesh(vertices, indices, true);
		Material material = new Material(ResourceLoader.loadTexture("test.png"), new Vector3f(1,1,1), 1, 8);

		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		MeshRenderer meshRenderer1 = new MeshRenderer(mesh, material);
		MeshRenderer meshRenderer2 = new MeshRenderer(mesh, material);

		DirectionalLight directionalLight1 = new DirectionalLight(new Vector3f(1.0f,0f,0f), 0.4f, new Vector3f(0.0f,1.0f,0.0f));
		PointLight pointLight1 = new PointLight(new Vector3f(0f, 0f, 1.0f), 1.0f, 0, 0, 0.5f);
		SpotLight spotLight1 = new SpotLight(
			new Vector3f(0,1,1), 0.4f,0,0,0.1f, 0.7f);

		//Creating gameObject
		
		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.setTranslation(0, -1, 5);
		
		GameObject directionalLightTest = new GameObject();
		directionalLightTest.setTranslation(2, 0, 0);
		directionalLightTest.setRotation(Quaternion.rotation(new Vector3f(0,1,0), (float)Math.toRadians(-90)));
		directionalLightTest.addComponent(directionalLight1);
		directionalLightTest.addComponent(pointLight1);
		directionalLightTest.addComponent(spotLight1);
		
		GameObject cameraObject = new GameObject();
		cameraObject.addComponent(new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000.0f));

		getRootObject().addChild(planeObject);
		getRootObject().addChild(cameraObject);
		getRootObject().addChild(directionalLightTest);
		
		GameObject structureTest1 = new GameObject();
		structureTest1.addComponent(meshRenderer1);
		structureTest1.setScale(0.1f, 0.1f, 0.1f);
		structureTest1.setRotation(Quaternion.rotation(Vector3f.YAXIS(), 0.785f));
		structureTest1.setTranslation(0.0f, 1.0f, 0.0f);

		GameObject structureTest2 = new GameObject();
		structureTest2.addComponent(meshRenderer2);
		structureTest2.setScale(1f, 1f, 1f);
		structureTest2.setTranslation(0.0f, 0.0f, 50.0f);
		
		structureTest1.addChild(structureTest2);
		
		getRootObject().addChild(structureTest1);
	}
}
