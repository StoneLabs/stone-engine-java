package StoneEngine.ResourceLoader.Models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import StoneEngine.Core.Util;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Vertex;

public class Mesh
{
	private MeshResource resource;
	
	public Mesh()
	{
		this.resource = new MeshResource();
	}
	public Mesh(MeshResource resource)
	{
		this.resource = resource;
		resource.addReference();
	}
	public Mesh(Vertex[] vertices, int[] indices)
	{
		this();
		
		addVertices(vertices, indices);
	}

	@Override
	public void finalize()
	{
		resource.removeReference();
	}

	private void addVertices(Vertex[] vertices, int[] indices)
	{		
		resource.setSize(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw()
	{
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);  //pos 		(OFFSET: 0 ; SIZE = 3*float = 12b)
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); //texCoord	(OFFSET: 12; SIZE = 2*float = 8b )
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20); //nomals		(OFFSET: 20; SIZE = 3*float = 12b)
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32); //tangent	(OFFSET: 32; SIZE = 3*float = 12b)
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
	
	public MeshResource getBuffers() { return resource; }
}
