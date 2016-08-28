package StoneEngine3D.Core;

import StoneLabs.sutil.Debug;

public class MainComponent 
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Dev build 1 - Stone Engine 3D";
	public static final double FRAME_CAP = 5000.0;
	
	private boolean isRunning;
	private Game game;
	
	public MainComponent()
	{
		Debug.Log("OpenGL " + RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		game = new Game();
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
		
		final double frameTime = 1.0 / FRAME_CAP;
		
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
				
				Input.update();
				game.input();
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
	
	public static void main(String[] args)
	{
		Debug.Log("Main init...");
		Window.createWindow(800, 800, TITLE);
		
		MainComponent game = new MainComponent();
		game.Start();
	}
}
