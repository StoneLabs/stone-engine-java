package StoneEngine.ResourceLoader.Models;

import java.util.ArrayList;

import StoneEngine.Core.Util;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Math.Vertex;

public class IndexedModel
{
	private ArrayList<Vector3f>	positions;
	private ArrayList<Vector2f>	texCoords;
	private ArrayList<Vector3f>	normals;
	private ArrayList<Integer>	indices;
	
	public IndexedModel() {
		this.positions	= new ArrayList<Vector3f>();
		this.texCoords	= new ArrayList<Vector2f>();
		this.normals	= new ArrayList<Vector3f>();
		this.indices	= new ArrayList<Integer	>();
	}
	
	public void calcNormals()
	{
		for (int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i+1);
			int i2 = indices.get(i+2);
			
			Vector3f v1 = positions.get(i1).sub(positions.get(i0));
			Vector3f v2 = positions.get(i2).sub(positions.get(i0));
			
			Vector3f normal = v1.cross(v2).normalize();
			
			normals.set(i0, normals.get(i0).add(normal));
			normals.set(i1, normals.get(i1).add(normal));
			normals.set(i2, normals.get(i2).add(normal));
		}
		
		for (int i = 0; i < positions.size(); i++)
			normals.set(i, normals.get(i).normalize());
	}
	
	public Mesh ToMesh()
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for (int i = 0; i < positions.size(); i++)
		{
			vertices.add(new Vertex(
					positions.get(i),
					texCoords.get(i),
					normals.get(i)
					));
		}
		
		Vertex[] vertexData = vertices.toArray(new Vertex[vertices.size()]);
		Integer[] indexData =  indices.toArray(new Integer[indices.size()]);
		
		return new Mesh(vertexData, Util.toIntArray(indexData), false);
	}
	
	public ArrayList<Vector3f> getPositions() { return positions; }
	public ArrayList<Vector2f> getTexCoords() { return texCoords; }
	public ArrayList<Vector3f> getNormals() { return normals; }
	public ArrayList<Integer> getIndices() { return indices; }
}
