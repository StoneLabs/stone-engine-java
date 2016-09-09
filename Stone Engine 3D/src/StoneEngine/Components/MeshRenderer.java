package StoneEngine.Components;

import StoneEngine.Core.Transform;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.Mesh;
import StoneEngine.Rendering.Shading.BasicShader;
import StoneEngine.Rendering.Shading.Shader;

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
	public void render(Transform transform, Shader shader)
	{
		shader.bind();
		shader.updateUniforms(transform, material);
		mesh.draw();
	}
}
