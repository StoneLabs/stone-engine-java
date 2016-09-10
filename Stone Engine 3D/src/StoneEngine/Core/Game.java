package StoneEngine.Core;

import StoneEngine.Scene.GameObject;

public abstract class Game
{
	private GameObject root;
	
	public void init() {}
	
	public void update(float delta)
	{
		root.update(delta);
	}
	
	public GameObject getRootObject() //todo: make root object private
	{ if (root == null) root = new GameObject(); return root; }
}
