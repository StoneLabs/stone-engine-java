package StoneEngine.Core;

import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Window;
import StoneLabs.sutil.Debug;

public class CoreEngine 
{	
	private boolean isRunning;
	private Game game;
	private RenderingEngine renderingEngine;
	private int width, height;
	private float frameTime;
	
	public CoreEngine(int width, int height, float framerate, Game game)
	{
		isRunning = false;
		this.game = game;
		this.width = width;
		this.height = height;
		this.frameTime = 1.0f/framerate;
	}
		
	public void createWindow(String title)
	{
		Window.createWindow(width, height, title);
		this.renderingEngine = new RenderingEngine();
	}
	
	public void Start()
	{
		if (isRunning)
			return;
		
		run();
	}
	public void Stop()
	{
		if (!isRunning)
			return;
		
		isRunning = false;
	}
	private void run()
	{
		isRunning = true;
		
		int frames = 0;
		double frameCounter = 0;
		
		game.init();
		
		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning)
		{
			boolean render = false;
			
			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while (unprocessedTime > frameTime)
			{
				render=true;
				
				unprocessedTime -= frameTime;
				
				if(Window.isCloseRequested())
					Stop();
				
//				Time.setDelta(frameTime);
				
				game.input(frameTime);
				renderingEngine.input(frameTime); //tmp hack
				Input.update();
				
				game.update(frameTime);
				
				if (frameCounter >= 1.0)//seconds
				{
					Debug.Log("FPS " + frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			
			if (render)
			{
				renderingEngine.render(game.getRootObject());
				Window.render();
				frames++;
			}
		}
		cleanUp();
	}
	
	private void cleanUp()
	{
		Window.dispose();
	}
}
