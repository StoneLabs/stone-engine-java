package StoneEngine.ResourceLoader.Models;

import java.util.ArrayList;

import StoneEngine.Core.Util;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Vertex;

public class IndexedModel
{
	private ArrayList<Vector3f>	positions;
	private ArrayList<Vector2f>	texCoords;
	private ArrayList<Vector3f>	tangents;
	private ArrayList<Vector3f>	normals;
	private ArrayList<Integer>	indices;
	
	public IndexedModel() {
		this.positions	= new ArrayList<Vector3f>();
		this.texCoords	= new ArrayList<Vector2f>();
		this.tangents	= new ArrayList<Vector3f>();
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
			
			Vector3f v1 = positions.get(i1).sub(positions.get(i0)); //edge 1
			Vector3f v2 = positions.get(i2).sub(positions.get(i0)); //edge 2
			
			Vector3f normal = v1.cross(v2).normalize();
			
			normals.set(i0, normals.get(i0).add(normal)); //Average out normals in case
			normals.set(i1, normals.get(i1).add(normal)); //they are used in more than
			normals.set(i2, normals.get(i2).add(normal)); //more than one triangle...
		}
		
		for (int i = 0; i < normals.size(); i++)
			normals.set(i, normals.get(i).normalize()); //"actual averaging"
	}
	
	public void calcTangents()
	{
		for (int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i+1);
			int i2 = indices.get(i+2);
			
			Vector3f v1 = positions.get(i1).sub(positions.get(i0)); //edge 1
			Vector3f v2 = positions.get(i2).sub(positions.get(i0)); //edge 2
			
			float deltaU1 = texCoords.get(i1).getX() - texCoords.get(i0).getX();
			float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();
			float deltaU2 = texCoords.get(i2).getX() - texCoords.get(i0).getX();
			float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();
			
			float f = 1.0f/(deltaU1 * deltaV2 - deltaU2 * deltaV1);
			
			Vector3f tangent = new Vector3f(
				f * (deltaV2 * v1.getX() - deltaV1 * v2.getX()),
				f * (deltaV2 * v1.getY() - deltaV1 * v2.getY()),
				f * (deltaV2 * v1.getZ() - deltaV1 * v2.getZ())
				);
			
			tangents.set(i0, tangents.get(i0).add(tangent)); //Average out normals in case
			tangents.set(i1, tangents.get(i1).add(tangent)); //they are used in more than
			tangents.set(i2, tangents.get(i2).add(tangent)); //more than one triangle...
		}
		
		for (int i = 0; i < tangents.size(); i++)
			tangents.set(i, tangents.get(i).normalize()); //"actual averaging"
	}
	
	public Mesh ToMesh()
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for (int i = 0; i < positions.size(); i++) //create Vertex array with all known data
		{
			vertices.add(new Vertex(
					positions.get(i),
					texCoords.get(i),
					normals.get(i),
					tangents.get(i)
					));
		}
		
		Vertex[] vertexData = vertices.toArray(new Vertex[vertices.size()]);
		Integer[] indexData =  indices.toArray(new Integer[indices.size()]);
		
		return new Mesh(vertexData, Util.toIntArray(indexData), false); //Create mesh with vertexdata and Index data
	}
	
	public ArrayList<Vector3f> getPositions() { return positions; }
	public ArrayList<Vector2f> getTexCoords() { return texCoords; }
	public ArrayList<Vector3f> getTangents() { return tangents; }
	public ArrayList<Vector3f> getNormals() { return normals; }
	public ArrayList<Integer> getIndices() { return indices; }
}
