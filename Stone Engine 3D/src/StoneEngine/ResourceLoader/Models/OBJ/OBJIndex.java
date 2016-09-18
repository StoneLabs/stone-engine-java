package StoneEngine.ResourceLoader.Models.OBJ;

public class OBJIndex
{
	public int vertexIndex;
	public int texCoordIndex;
	public int normalIndex;
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj.getClass() != OBJIndex.class)
			return false;
		
		OBJIndex r = (OBJIndex)obj;
		return this.vertexIndex 	== r.vertexIndex
			&& this.texCoordIndex 	== r.texCoordIndex
			&& this.normalIndex 	== r.normalIndex;
	}
	
	@Override
	public int hashCode()
	{
		final int BASE = 11; //PRIME
		final int MULTIPLIER = 17; //PRIME
		
		int result = BASE;
		result = MULTIPLIER * result + vertexIndex;
		result = MULTIPLIER * result + texCoordIndex;
		result = MULTIPLIER * result + normalIndex;
		
		return result;
	}
}
