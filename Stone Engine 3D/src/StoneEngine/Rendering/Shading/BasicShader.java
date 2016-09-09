package StoneEngine.Rendering.Shading;

import StoneEngine.Core.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Scene.Transform;

public class BasicShader extends Shader
{
	private static BasicShader instance = new BasicShader();
	
	public static BasicShader getInstance()
	{
		return instance;
	}
	
	private BasicShader()
	{
		super();
				
		addVertexShader(ResourceLoader.loadShader("basicVertex.vs"));
		addFragmentShader(ResourceLoader.loadShader("basicFragment.fs"));
		compileShader();
		
		addUniform("transform");
		addUniform("color");
	}
	
	public void updateUniforms(Transform transform, Material material)
	{
		Matrix4f worldMatrix = transform.getTanformation();
		Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);
		
		material.getTexture().bind();
		
		setUniform("transform", projectedMatrix);
		setUniform("color", material.getColor());
	}
}
