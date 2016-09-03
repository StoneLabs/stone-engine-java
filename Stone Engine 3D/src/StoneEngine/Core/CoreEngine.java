package StoneEngine.Core;

import StoneEngine.Rendering.RenderUtil;
import StoneEngine.Rendering.Window;
import StoneLabs.sutil.Debug;

public class CoreEngine 
{	
	private boolean isRunning;
	private Game game;
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
		Debug.Log("OpenGL " + RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
	}
	
	public void createWindow(String title)
	{
		Window.createWindow(width, height, title);
		initRenderSystem();		
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
				render();
				frames++;
			}
		}
		cleanUp();
	}
	private void render()
	{
		RenderUtil.clearScreen();
		game.render();
		Window.render();
	}
	private void cleanUp()
	{
		Window.dispose();
	}
}
