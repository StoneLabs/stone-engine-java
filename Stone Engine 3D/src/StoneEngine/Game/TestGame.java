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
	GameObject structureTest1 = new GameObject();
	GameObject directionalLightTest2 = new GameObject();
	
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
		Material material = new Material();
		material.addTexture("diffuse", ResourceLoader.loadTexture("test.png"));
		material.addFloat("specularIntensity", 1f);
		material.addFloat("specularExponent", 8f);

		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		MeshRenderer meshRenderer1 = new MeshRenderer(mesh, material);
		MeshRenderer meshRenderer2 = new MeshRenderer(mesh, material);
		MeshRenderer meshRenderer3 = new MeshRenderer(mesh, material);

		DirectionalLight directionalLight1 = new DirectionalLight(new Vector3f(1.0f,0f,0f), 0.4f);
		PointLight pointLight1 = new PointLight(new Vector3f(0f, 0f, 1.0f), 1.0f, 0, 0, 0.5f);
		SpotLight spotLight1 = new SpotLight(
			new Vector3f(0,1,1), 0.4f,0,0,0.1f, 0.7f);

		//Creating gameObject
		
		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.setTranslation(0, -1, 5);
		
		GameObject directionalLightTest1 = new GameObject();
		directionalLightTest1.setRotation(Quaternion.rotation(directionalLightTest1.getRotation().getRight(), (float)Math.toRadians(-45)));
		directionalLightTest1.addComponent(directionalLight1);
		
		directionalLightTest2.setTranslation(2, 0, 0);
		directionalLightTest2.setRotation(Quaternion.rotation(new Vector3f(0,1,0), (float)Math.toRadians(90)));
		directionalLightTest2.addComponent(pointLight1);
		directionalLightTest2.addComponent(spotLight1);
		

		getRootObject().addChild(planeObject);
		getRootObject().addChild(directionalLightTest1);
		getRootObject().addChild(directionalLightTest2);
		
		structureTest1.addComponent(meshRenderer1);
		structureTest1.setScale(0.1f, 0.1f, 0.1f);
		structureTest1.setRotation(Quaternion.rotation(Vector3f.YAXIS(), 0.785f));
		structureTest1.setTranslation(0.0f, 1.0f, 0.0f);

		GameObject structureTest2 = new GameObject();
		structureTest2.addComponent(meshRenderer2);
		structureTest2.setScale(1f, 1f, 1f);
		structureTest2.setTranslation(0.0f, 0.0f, 50.0f);

		GameObject structureTest3 = new GameObject();
		structureTest3.addComponent(meshRenderer3);
		structureTest3.setScale(0.1f, 1f, 0.1f);
		structureTest3.setTranslation(-1.0f, -1f, -1.0f);
		
		GameObject cameraObject = new GameObject();
		cameraObject.addComponent(new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000.0f));
		cameraObject.addChild(structureTest3);
		
		structureTest1.addChild(structureTest2);
		
		this.getRootObject().addChild(cameraObject);
		this.getRootObject().addChild(structureTest1);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);

		directionalLightTest2.rotate(Vector3f.YAXIS(), -deltaTime);
		structureTest1.rotate(Vector3f.YAXIS(), deltaTime);
	}
}
