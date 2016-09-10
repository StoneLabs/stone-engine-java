package StoneEngine.Scene.Rendering;

import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.Mesh;
import StoneEngine.Rendering.Shading.Shader;
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
	public void render(Shader shader)
	{
		shader.bind();
		shader.updateUniforms(getGameObject(), material);
		mesh.draw();
	}
}