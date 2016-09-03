package StoneEngine.Core;

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
	
	private void initRenderSystem()
	{
		Debug.Log("OpenGL " + RenderingEngine.getOpenGLVersion());
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
		long frameCounter = 0;
		
		game.init();
		
		long lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning)
		{
			boolean render = false;
			
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime / (double)Time.SECOND;
			frameCounter += passedTime;
			
			while (unprocessedTime > frameTime)
			{
				render=true;
				
				unprocessedTime -= frameTime;
				
				if(Window.isCloseRequested())
					Stop();
				
				Time.setDelta(frameTime);
				
				game.input();
				Input.update();
				
				game.update();
				
				if (frameCounter >= Time.SECOND)
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
