package StoneEngine.Core;

public class Vector2f 
{
	private float x;
	private float y;
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x+y*y);
	}
	public float dot(Vector2f r)
	{
		return x*r.x + y*r.y;
	}
	public Vector2f normalize()
	{
		float length = length();
		
		return new Vector2f(x/length, y/length);
	}
	public Vector2f rotate(float angle)
	{
		float rad = (float)Math.toRadians(angle);
		float cos = (float)Math.cos(rad);
		float sin = (float)Math.sin(rad);
		
		return new Vector2f(x * cos - y * sin, x * sin + y * cos);
	}
	public Vector2f add(Vector2f r)
	{
		return new Vector2f(x+r.x, y+r.y);
	}
	public Vector2f add(float f)
	{
		return new Vector2f(x+f, y+f);
	}
	public Vector2f sub(Vector2f r)
	{
		return new Vector2f(x-r.x, y-r.y);
	}
	public Vector2f sub(float f)
	{
		return new Vector2f(x-f, y-f);
	}
	public Vector2f mul(Vector2f r)
	{
		return new Vector2f(x*r.x, y*r.y);
	}
	public Vector2f mul(float f)
	{
		return new Vector2f(x*f, y*f);
	}
	public Vector2f div(Vector2f r)
	{
		return new Vector2f(x/r.x, y/r.y);
	}
	public Vector2f div(float f)
	{
		return new Vector2f(x/f, y/f);
	}
	
	
	public String toString()
	{
		return "(" + x + " " + y + ")";
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
