package StoneEngine.Core;

public class Vector3f 
{
	public static final Vector3f NULL()
	{
		return new Vector3f(0,0,0);
	}
	public static final Vector3f YAXIS()
	{
		return new Vector3f(0,1,0);
	}
	
	private float x,y,z;
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	

	public float length()
	{
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	public float dot(Vector3f r)
	{
		return x*r.x + y*r.y + z*r.z;
	}
	public Vector3f cross(Vector3f r)
	{
		float x_ = y*r.z - z*r.y;
		float y_ = z*r.x - x*r.z;
		float z_ = x*r.y - y*r.x;
		
		return new Vector3f(x_, y_, z_);
	}
	public Vector3f normalize()
	{
		float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}
	
	public Vector3f rotate(float angle, Vector3f axis)
	{
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle/2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle/2));
		
		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f add(Vector3f r)
	{
		return new Vector3f(x+r.x, y+r.y, z+r.z);
	}
	public Vector3f add(float f)
	{
		return new Vector3f(x+f, y+f, z+f);
	}
	public Vector3f sub(Vector3f r)
	{
		return new Vector3f(x-r.x, y-r.y, z-r.z);
	}
	public Vector3f sub(float f)
	{
		return new Vector3f(x-f, y-f, z-f);
	}
	public Vector3f mul(Vector3f r)
	{
		return new Vector3f(x*r.x, y*r.y, z*r.z);
	}
	public Vector3f mul(float f)
	{
		return new Vector3f(x*f, y*f, z*f);
	}
	public Vector3f div(Vector3f r)
	{
		return new Vector3f(x/r.x, y/r.y, z/r.z);
	}
	public Vector3f div(float f)
	{
		return new Vector3f(x/f, y/f, z/f);
	}
	
	
	public String toString()
	{
		return "(" + x + " " + y + " " + z + ")";
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

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
