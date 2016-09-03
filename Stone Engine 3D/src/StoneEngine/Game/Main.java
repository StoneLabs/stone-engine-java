package StoneEngine.Game;

import StoneEngine.Core.CoreEngine;

public class Main 
{
	public static void main(String[] args)
	{
		CoreEngine engine = new CoreEngine(800, 600, 100000.0f, new TestGame());
		engine.createWindow("Stone Engine Test");
		engine.Start();
	}
}
