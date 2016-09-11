package StoneEngine.ResourceLoader.Models;

import java.io.Reader;

public interface ResourceModel 
{
	public void Load(Reader file);
	public IndexedModel ToIndexedModel();
	public String DefaultExtension();
}
