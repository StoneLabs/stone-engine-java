package StoneEngine.Core;

import java.util.ArrayList;

import StoneEngine.Components.GameComponent;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.Shader;

public class GameObject
{
	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;
	private Transform transform;
	
	public GameObject()
	{
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
		transform = new Transform();
	}
	
	public void addChild(GameObject child)
	{
		children.add(child);
	}
	
	public void addComponent(GameComponent component)
	{
		components.add(component);
	}
	
	public void input(float delta)
	{
		for (GameComponent component : components)
			component.input(transform, delta);
		
		for (GameObject child : children)
			child.input(delta);
	}
	public void update(float delta)
	{
		for (GameComponent component : components)
			component.update(transform, delta);
		
		for (GameObject child : children)
			child.update(delta);
	}
	public void render(Shader shader)
	{
		for (GameComponent component : components)
			component.render(transform, shader);
		
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

	public Transform getTransform() 
	{ return transform;	}
}
