package StoneEngine.ResourceLoader.Models;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import StoneEngine.ResourceLoader.ResourceLoader;

public class MeshResource 
{
	private int vbo; //VERTEX pointer
	private int ibo; //INDEX  pointer
	private int size;
	private int referenceCounter; //For finalization
	private String fileReference = null;
	
	public MeshResource() 
	{
		this.vbo = glGenBuffers();
		this.ibo = glGenBuffers();
		this.referenceCounter = 1;
		this.size = 0;
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
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
		if (fileReference != null)
		{
			ResourceLoader.getKnownModels().remove(fileReference);
			fileReference = null;
		}
	}

	public int getVbo() {
		return vbo;
	}

	public int getIbo() {
		return ibo;
	}

	public int getSize() {
		return size;
	}

	public String getFileReference() {
		return fileReference;
	}

	protected void setSize(int size) {
		this.size = size;
	}

	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}
}
