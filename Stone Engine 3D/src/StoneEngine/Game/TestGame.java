package StoneEngine.Game;

import StoneEngine.Core.Game;
import StoneEngine.Core.Input;
import StoneEngine.Math.Attenuation;
import StoneEngine.Math.Quaternion;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.Vertex;
import StoneEngine.Rendering.Window;
import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.ResourceLoader.Models.Mesh;
import StoneEngine.ResourceLoader.Models.OBJ.OBJModel;
import StoneEngine.Scene.GameObject;
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
	GameObject monkey = new GameObject();
	GameObject monkey2 = new GameObject();
	GameObject cameraObject = new GameObject();
	
	public TestGame()
	{
		
	}
	
	@Override
	public void init()
	{
		Mesh mesh = ResourceLoader.loadMesh("models/plane.obj", OBJModel.class);
		
		Material material = new Material();
		material.addTexture("diffuse", ResourceLoader.loadTexture("textures/test.png"));
		material.addTexture("normalMap", ResourceLoader.loadTexture("textures/default_normal.png"));
		material.addTexture("dispMap", ResourceLoader.loadTexture("textures/default_disp.png"));
		material.addFloat("specularIntensity", 1f);
		material.addFloat("specularExponent", 64f);
		material.addFloat("dispMapScale", 0f);
		
		Material materialBricks = new Material();
		materialBricks.addTexture("diffuse", ResourceLoader.loadTexture("textures/bricks_diffuse.jpg"));
		materialBricks.addTexture("normalMap", ResourceLoader.loadTexture("textures/bricks_normal.jpg"));
		materialBricks.addTexture("dispMap", ResourceLoader.loadTexture("textures/bricks_disp.jpg"));
		materialBricks.addFloat("specularIntensity", 0.5f);
		materialBricks.addFloat("specularExponent", 32f);
		materialBricks.addFloat("dispMapScale", 0.04f);


		MeshRenderer meshRenderer = new MeshRenderer(mesh, materialBricks);
		MeshRenderer meshRenderer1 = new MeshRenderer(mesh, materialBricks);
		MeshRenderer meshRenderer2 = new MeshRenderer(mesh, materialBricks);
		MeshRenderer meshRenderer3 = new MeshRenderer(mesh, materialBricks);
		
		MeshRenderer meshRendererMonkey = new MeshRenderer(ResourceLoader.loadMesh("models/monkey.obj", OBJModel.class), material);
		MeshRenderer meshRendererMonkey2 = new MeshRenderer(ResourceLoader.loadMesh("models/monkey.obj", OBJModel.class), material);

		DirectionalLight directionalLight1 = new DirectionalLight(new Vector3f(1.0f,1,1f), 0.4f);
		PointLight pointLight1 = new PointLight(new Vector3f(0f, 0f, 1.0f), 1.0f, new Attenuation(0, 0, 0.4f));
		SpotLight spotLight1 = new SpotLight(new Vector3f(0,1,1), 0.8f, new Attenuation(0, 0.01f, 0.05f), 0.7f);

		//Creating gameObject
		
		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.setTranslation(0, -1, 5);
		
		GameObject directionalLightTest1 = new GameObject();
		directionalLightTest1.setRotation(Quaternion.rotation(directionalLightTest1.getRotation().getRight(), (float)Math.toRadians(-45)));
		directionalLightTest1.addComponent(directionalLight1);
		
		directionalLightTest2.setTranslation(7, 0, 7);
		directionalLightTest2.setRotation(Quaternion.rotation(new Vector3f(0,1,0), (float)Math.toRadians(90)));
		directionalLightTest2.addComponent(pointLight1);
		directionalLightTest2.addComponent(spotLight1);
		

		getRootObject().addChild(planeObject);
		getRootObject().addChild(directionalLightTest1);
		getRootObject().addChild(directionalLightTest2);
		
		structureTest1.addComponent(meshRenderer1);
		structureTest1.setScale(0.1f, 0.1f, 0.1f);
		structureTest1.setRotation(Quaternion.rotation(Vector3f.YAXIS(), 0.785f));
		structureTest1.setTranslation(10.0f, 1.0f, -10.0f);

		GameObject structureTest2 = new GameObject();
		structureTest2.addComponent(meshRenderer2);
		structureTest2.setScale(1f, 1f, 1f);
		structureTest2.setTranslation(0.0f, 0.0f, 50.0f);

		GameObject structureTest3 = new GameObject();
		structureTest3.addComponent(meshRenderer3);
		structureTest3.setScale(0.15f, 0.2f, 0.15f);
		structureTest3.setTranslation(-1.0f, -1.0f, -1.0f);
		
		cameraObject.addComponent(new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 10000.0f));
		cameraObject.addComponent(new FreeLook());
		cameraObject.addChild(structureTest3);
		cameraObject.setTranslation(5, 1, 0);
		
		structureTest1.addChild(structureTest2);
		
		this.getRootObject().addChild(cameraObject);
		this.getRootObject().addChild(structureTest1);
		
		monkey.addComponent(meshRendererMonkey);
		monkey.setScale(2f, 2f, 2f);
		monkey.setTranslation(10, 5, 10);
		
		monkey2.addComponent(meshRendererMonkey2);
		monkey2.setScale(2f, 2f, 2f);
		monkey2.setTranslation(15, 5, 15);
		
		this.getRootObject().addChild(monkey);
		this.getRootObject().addChild(monkey2);	
		
		
		Mesh tankMesh = ResourceLoader.loadMesh("tank/TANK.obj", OBJModel.class);
		Material tankMat = new Material();

		tankMat.addTexture("diffuse", ResourceLoader.loadTexture("tank/TANK.jpg"));
		tankMat.addTexture("normalMap", ResourceLoader.loadTexture("tank/TANK_NM.jpg"));
		tankMat.addTexture("dispMap", ResourceLoader.loadTexture("tank/TANK_SM.jpg"));
		tankMat.addFloat("specularIntensity", 2f);
		tankMat.addFloat("specularExponent", 32f);
		tankMat.addFloat("dispMapScale", 0.05f);
		
		MeshRenderer tankRend = new MeshRenderer(tankMesh, tankMat);
		
		GameObject tank = new GameObject();
		tank.addComponent(tankRend);
		tank.setTranslation(-5, -0.9f, 0);
		tank.setScale(5, 5, 5);
		
		this.getRootObject().addChild(tank);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);

		directionalLightTest2.rotate(Vector3f.YAXIS(), -deltaTime);
		structureTest1.rotate(Vector3f.YAXIS(), deltaTime);
		monkey.rotate(Vector3f.YAXIS(), deltaTime/2);
		
		Quaternion lookAtDirection = monkey2.getLookAtDirection(cameraObject.getTranslation(), Vector3f.YAXIS());
		Quaternion currentRotation = monkey2.getRotation();
		
		monkey2.setRotation(currentRotation.sLerp(lookAtDirection, deltaTime * 2f, true));
	}
}
