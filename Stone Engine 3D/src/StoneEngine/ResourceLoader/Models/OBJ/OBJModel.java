package StoneEngine.ResourceLoader.Models.OBJ;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import StoneEngine.Core.Util;
import StoneEngine.ResourceLoader.Models.IndexedModel;
import StoneEngine.ResourceLoader.Models.ResourceModel;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneLabs.sutil.Debug;

public class OBJModel implements ResourceModel
{
	private ArrayList<Vector3f>	positions;
	private ArrayList<Vector2f>	texCoords;
	private ArrayList<Vector3f>	normals;
	private ArrayList<OBJIndex>	indices;
	private boolean hasTexCoords	= false;
	private boolean hasNormals		= false;
	
	public OBJModel() {}
	
	@Override
	public String defaultExtension() { return "obj"; }
	
	@Override
	public void load(Reader source)
	{
		this.positions	= new ArrayList<Vector3f>();
		this.texCoords	= new ArrayList<Vector2f>();
		this.normals	= new ArrayList<Vector3f>();
		this.indices	= new ArrayList<OBJIndex>();

		BufferedReader meshReader = null;
		
		try
		{
			meshReader = new BufferedReader(source);
			String line;
			while ((line = meshReader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if (tokens.length == 0 || tokens[0].equals("#")) continue;
				if (tokens[0].equals("v"))
					positions.add(new Vector3f(
							Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])
							));
				else if (tokens[0].equals("vt"))
					texCoords.add(new Vector2f(
							Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2])
							));
				else if (tokens[0].equals("vn"))
					normals.add(new Vector3f(
							Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])
							));
				else if (tokens[0].equals("f"))
				{
					//Triangulate indices
					for (int i = 0; i < tokens.length - 3; i++)
					{
						indices.add(parseOBJIndex(tokens[1    ]));
						indices.add(parseOBJIndex(tokens[2 + i]));
						indices.add(parseOBJIndex(tokens[3 + i]));
					}
				}
			}
			
			meshReader.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	private OBJIndex parseOBJIndex(String token)
	{
		String[] values = token.split("/");
		
		OBJIndex result = new OBJIndex();
		result.vertexIndex = Integer.valueOf(values[0]) - 1;
		
		if (values.length > 1)
		{
			hasTexCoords = true;
			result.texCoordIndex = Integer.valueOf(values[1]) - 1;
			
			if (values.length > 2)
			{
				hasNormals = true;
				result.normalIndex = Integer.valueOf(values[2]) - 1;
			}
		}
		
		return result;
	}
	
	@Override
	public IndexedModel toIndexedModel()
	{
		IndexedModel result = new IndexedModel();
		IndexedModel normalModel = new IndexedModel();
		
		HashMap<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
		HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < indices.size(); i++)
		{
			OBJIndex currentIndex = indices.get(i);
			
			Vector3f currentPosition = positions.get(currentIndex.vertexIndex);
			Vector2f currentTexCoord;
			Vector3f currentNormal;
			
			if (hasTexCoords)
				currentTexCoord = texCoords.get(currentIndex.texCoordIndex);
			else
				currentTexCoord = new Vector2f(0,0);
			
			if (hasNormals)
				currentNormal = normals.get(currentIndex.normalIndex);
			else
				currentNormal = new Vector3f(0,0,0);
			
			//Mesh optimization
			Integer modelVertexIndex = resultIndexMap.get(currentIndex);
						
			if (modelVertexIndex == null)
			{
				modelVertexIndex = result.getPositions().size(); //No other occurrence found
				
				resultIndexMap.put(currentIndex, modelVertexIndex);

				
				result.getPositions().add(currentPosition);
				result.getTexCoords().add(currentTexCoord);
				if (hasNormals)
					result.getNormals().add(currentNormal);
			}
			
			Integer normalModelIndex = normalIndexMap.get(currentIndex.vertexIndex);
			
			if (normalModelIndex == null)
			{
				normalModelIndex = normalModel.getPositions().size(); //No other occurrence found
				
				normalIndexMap.put(currentIndex.vertexIndex, normalModelIndex);

				
				normalModel.getPositions().add(currentPosition);
				normalModel.getTexCoords().add(currentTexCoord);
				normalModel.getNormals().add(currentNormal);
			}
			
			result.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			indexMap.put(modelVertexIndex, normalModelIndex);
		}
		
		if (!hasNormals)
		{
			normalModel.calcNormals();
			
			for (int i = 0; i < result.getPositions().size(); i++)
				result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
		}
		
		return result;
	}
}
