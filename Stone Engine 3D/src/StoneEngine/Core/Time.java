package StoneEngine.Core;

public class Time
{
	private static final long C_SECONDS = 1000000000;
	private static double delta;
	
	public static double getTime()
	{
		return (double)System.nanoTime()/(double)C_SECONDS;
	}
}
