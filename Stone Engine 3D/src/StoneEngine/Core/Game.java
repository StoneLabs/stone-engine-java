package StoneEngine.Core;

import StoneEngine.Scene.GameObject;
import StoneLabs.sutil.Debug;

public abstract class Game
{
	private GameObject root;
	
	public Game()
	{
		Debug.Log("Loading game...");
		root = new GameObject();
	}
	
	public void init() {}
	
	public void update(float delta)
	{
		root.update(delta);
	}
	
	public GameObject getRootObject() //todo: make root object private
	{ return root; }
	
	void setRootEngine(CoreEngine core)
	{ root.setEngine(core); }
}
