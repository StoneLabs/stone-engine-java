package StoneEngine.Game;

import StoneEngine.Core.CoreEngine;
import StoneLabs.sutil.Debug;

public class Main 
{
	public static void main(String[] args)
	{
		Debug.Log("Starting engine...");
		CoreEngine engine = new CoreEngine(800, 600, 5000.0f, new TestGame());
		engine.createWindow("Stone Engine Test");
		engine.Start();
	}
}
