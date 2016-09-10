package StoneEngine.Core;

import StoneEngine.Scene.GameObject;

public abstract class Game
{
	private GameObject root;
	
	public void init() {}
	
	public void update(float delta)
	{
		getRootObject().update(delta);
	}
	
	public GameObject getRootObject()
	{ if (root == null) root = new GameObject(); return root; }
}
