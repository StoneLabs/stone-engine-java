package StoneEngine.Game;

import StoneEngine.Core.Input;
import StoneEngine.Core.CoreEngine;
import StoneEngine.Core.Game;
import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Time;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Math.Vertex;
import StoneEngine.Rendering.Camera;
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
import StoneEngine.Scene.Miscellaneous.MeshRenderer;
import StoneLabs.sutil.Debug;

@SuppressWarnings("unused") //TODO REMOVE
public class TestGame extends Game
{
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
		planeObject.setTranslation(0, -1, 5);

		DirectionalLight directionalLight1 = new DirectionalLight(new Vector3f(1.0f,0f,0f), 0.4f, new Vector3f(1.0f,1.0f,1.0f));
		PointLight pointLight1 = new PointLight(new Vector3f(0f, 0f, 1.0f), 1.0f, 0, 0, 0.5f);
		SpotLight spotLight1 = new SpotLight(
			new Vector3f(0,1,1), 0.4f,0,0,0.1f,
			new Vector3f(1,0,0), 0.7f);
		
		GameObject directionalLightTest = new GameObject();
		directionalLightTest.setTranslation(2, 0, 0);
		directionalLightTest.addComponent(directionalLight1);
		directionalLightTest.addComponent(pointLight1);
		directionalLightTest.addComponent(spotLight1);
		
		
		getRootObject().addChild(planeObject);
		getRootObject().addChild(directionalLightTest);
	}
}
