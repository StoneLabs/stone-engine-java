package StoneEngine.Scene;

import java.util.ArrayList;

import StoneEngine.Core.CoreEngine;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shader;

public class GameObject extends Transform //extends Transform is an experimental construct
{
	private GameObject parent = null;
	private CoreEngine engine = null;
	
	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;
	
	public GameObject()
	{
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
	}
	
	public void addChild(GameObject child)
	{
		children.add(child);
		child.setEngine(engine);
		child.setParent(this);
	}
	
	public void addComponent(GameComponent component)
	{
		components.add(component);
		component.setParent(this);
	}
	
	public void update(float delta)
	{
		super.update();
		
		for (GameComponent component : components)
			component.update(delta);
		
		for (GameObject child : children)
			child.update(delta);
	}
	public void render(Shader shader, RenderingEngine renderingEngine)
	{		
		for (GameComponent component : components)
			component.render(shader, renderingEngine);
		
		for (GameObject child : children)
			child.render(shader, renderingEngine);
	}

	public void setEngine(CoreEngine engine) 
	{
		if (this.engine == engine)
			return;
		
		this.engine = engine;
		
		for (GameComponent component : components)
			component.addToEngine(engine);
		
		for (GameObject child : children)
			child.setEngine(engine);
	}
	
	@SuppressWarnings("unchecked") //TODO remove clone?
	public ArrayList<GameObject> getChildren()
	{ return (ArrayList<GameObject>)children.clone(); }
	
	public ArrayList<GameObject> getAllChildren()
	{
		ArrayList<GameObject> result = new ArrayList<>();
		
		for (GameObject child : children)
			result.addAll(child.getAllChildren());
		
		result.add(this);
		return result;
	}

	public void move(Vector3f amnt) { this.setTranslation(this.getTranslation().add(amnt)); }
	public void move(Vector3f dir, float amnt) { this.setTranslation(this.getTranslation().add(dir.mul(amnt))); }
	private void setParent(GameObject parent) { this.parent = parent; super.setParent(parent); }
	public GameObject getParent() { return parent; }
	public CoreEngine getEngine() { return engine; }
}
