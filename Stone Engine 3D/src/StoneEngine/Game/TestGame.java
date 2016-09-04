package StoneEngine.Game;

import StoneEngine.Core.Input;
import StoneEngine.Components.DirectionalLight;
import StoneEngine.Components.MeshRenderer;
import StoneEngine.Components.PointLight;
import StoneEngine.Components.SpotLight;
import StoneEngine.Core.CoreEngine;
import StoneEngine.Core.Game;
import StoneEngine.Core.GameObject;
import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Time;
import StoneEngine.Core.Transform;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Attenuation;
import StoneEngine.Rendering.BaseLight;
import StoneEngine.Rendering.Camera;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.Mesh;
import StoneEngine.Rendering.PhongShader;
import StoneEngine.Rendering.Shader;
import StoneEngine.Rendering.Vertex;
import StoneEngine.Rendering.Window;
import StoneLabs.sutil.Debug;

public class TestGame extends Game
{
	private Camera camera;

	public void init()
	{
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

		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.getTransform().setTranslation(0, -1, 5);

		DirectionalLight directionalLight1 = new DirectionalLight(new BaseLight(new Vector3f(1.0f,0f,0f), 0.4f), new Vector3f(1.0f,1.0f,1.0f));
		PointLight pointLight1 = new PointLight(new BaseLight(new Vector3f(0f, 0f, 1.0f), 0.8f), new Attenuation(0, 0, 1), new Vector3f(3, 0f, 0), 100);
		SpotLight spotLight1 = new SpotLight(new PointLight(new BaseLight(new Vector3f(0,1,1), 0.4f),
			new Attenuation(0,0,0.1f),
			new Vector3f(0,0,20), 100),
			new Vector3f(1,0,0), 0.7f);
		
		GameObject directionalLightTest = new GameObject();
		directionalLightTest.addComponent(directionalLight1);
		directionalLightTest.addComponent(pointLight1);
		directionalLightTest.addComponent(spotLight1);
		
		
		getRootObject().addChild(planeObject);
		getRootObject().addChild(directionalLightTest);
	}
}
