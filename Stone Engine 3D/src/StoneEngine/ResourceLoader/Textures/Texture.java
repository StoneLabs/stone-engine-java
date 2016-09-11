package StoneEngine.ResourceLoader.Textures;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture
{
	private TextureResource resource;
	
	public Texture(TextureResource resource)
	{
		this.resource = resource;
		this.resource.addReference();
	}
	
	@Override
	public void finalize()
	{
		resource.removeReference();
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, resource.getId());
	}
	
	public int getID()
	{
		return resource.getId();
	}
}
