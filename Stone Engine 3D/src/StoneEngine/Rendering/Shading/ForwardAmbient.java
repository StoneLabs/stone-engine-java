package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Scene.Transform;

public class ForwardAmbient extends Shader
{
	private static ForwardAmbient instance = new ForwardAmbient();
	
	public static ForwardAmbient getInstance()
	{
		return instance;
	}
	
	private ForwardAmbient()
	{
		super();
				
		addVertexShader(ResourceLoader.loadShader("forward-ambient.vs"));
		addFragmentShader(ResourceLoader.loadShader("forward-ambient.fs"));
		
		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		
		compileShader();
		
		addUniform("MVP");
		addUniform("ambientIntensity");
	}
	
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
	{
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
		
		material.getTexture("diffuse").bind();
		
		setUniform("MVP", projectedMatrix);
		setUniform("ambientIntensity", renderingEngine.getAmbientLight());
	}
}
