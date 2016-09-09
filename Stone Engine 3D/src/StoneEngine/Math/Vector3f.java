package StoneEngine.Math;

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
	
	public float max()
	{
		return Math.max(x, Math.max(y, z));
	}
	public float min()
	{
		return Math.min(x, Math.max(y, z));
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
		Quaternion rotation = Quaternion.rotation(axis, angle);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.mul(this).mul(conjugate);
		
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f lerp(Vector3f dest, float lerpFactor)
	{
		return dest.sub(this).mul(lerpFactor).add(this);
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
	
	public Vector2f getXY()	{ return new Vector2f(x, y); } public Vector2f getXZ()	{ return new Vector2f(x, z); }
	public Vector2f getYX()	{ return new Vector2f(y, x); } public Vector2f getYZ()	{ return new Vector2f(y, z); }
	public Vector2f getZX()	{ return new Vector2f(z, x); } public Vector2f getZY()	{ return new Vector2f(z, y); }
	
	
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

	public void set(float x, float y, float z)
	{
		this.x = x; this.y = y; this.z = z;
	}
	
	public boolean equals(Vector3f r)
	{
		return  x == r.x &&
				y == r.y &&
				z == r.z ;
	}
}
