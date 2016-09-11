package StoneEngine.ResourceLoader.Textures;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

import StoneEngine.ResourceLoader.ResourceLoader;

public class TextureResource
{
	private int id; //texture pointer
	private int referenceCounter; //For finalization
	private String fileReference = null;
	
	public TextureResource(int id)
	{
		this.id = id;
		this.referenceCounter = 1;
	}

	public void addReference()
	{
		this.referenceCounter++;
	}
	public void removeReference()
	{
		this.referenceCounter--;
		if (this.referenceCounter == 0) finalize();
	}
	
	@Override
	protected void finalize()
	{
		glDeleteBuffers(this.id);
		if (fileReference != null)
		{
			ResourceLoader.getKnownTextures().remove(fileReference);
			fileReference = null;
		}
	}

	public int getId() {
		return id;
	}

	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}
}
