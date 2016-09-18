package StoneEngine.ResourceLoader.Models;

import java.io.Reader;

public interface ResourceModel 
{
	public void load(Reader file);
	public IndexedModel toIndexedModel();
	public String defaultExtension();
}
