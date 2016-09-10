package StoneEngine.Scene;

import java.util.ArrayList;

import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.Shader;

public class GameObject extends Transform //extends Transform is an experimental construct
{
	private GameObject parent = null;
	
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
	public void render(Shader shader)
	{		
		for (GameComponent component : components)
			component.render(shader);
		
		for (GameObject child : children)
			child.render(shader);
	}

	//Temporary solution!
	public void addToRenderingEngine(RenderingEngine renderingEngine)
	{
		for (GameComponent component : components)
			component.addToRenderingEngine(renderingEngine);
		
		for (GameObject child : children)
			child.addToRenderingEngine(renderingEngine);
	}

	public void move(Vector3f dir, float amnt)
	{
		this.setTranslation(
				this.getTranslation().add(
						dir.mul(amnt)));
	}
	public void move(Vector3f amnt)
	{
		this.setTranslation(
				this.getTranslation().add(amnt));
	}

	public GameObject getParent() {
		return parent;
	}

	private void setParent(GameObject parent) {
		this.parent = parent;
		super.setParent(parent);
	}
}
