package StoneEngine.ResourceLoader.Textures;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

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

	public void bind() { bind(0); }
	public void bind(int samplerSlot)
	{
		assert(samplerSlot >= 0 && samplerSlot <= 31);
		glActiveTexture(GL_TEXTURE0 + samplerSlot);
		glBindTexture(GL_TEXTURE_2D, resource.getId());
	}
	
	public int getID()
	{
		return resource.getId();
	}
}
