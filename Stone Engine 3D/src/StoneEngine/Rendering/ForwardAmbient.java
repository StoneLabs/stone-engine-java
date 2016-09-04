package StoneEngine.Rendering;

import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Transform;
import StoneEngine.Math.Matrix4f;

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
	
	public void updateUniforms(Transform transform, Material material)
	{
		Matrix4f worldMatrix = transform.getTanformation();
		Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);
		
		material.getTexture().bind();
		
		setUniform("MVP", projectedMatrix);
		setUniform("ambientIntensity", getRenderingEngine().getAmbientLight());
	}
}
