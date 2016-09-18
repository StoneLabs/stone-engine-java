package StoneEngine.Scene.Rendering;

import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shader;
import StoneEngine.ResourceLoader.Models.Mesh;
import StoneEngine.Scene.GameComponent;

public class MeshRenderer extends GameComponent
{
	private Mesh mesh;
	private Material material;
	
	public MeshRenderer(Mesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine)
	{
		shader.bind();
		shader.updateUniforms(getGameObject(), material, renderingEngine);
		mesh.draw();
	}
}
