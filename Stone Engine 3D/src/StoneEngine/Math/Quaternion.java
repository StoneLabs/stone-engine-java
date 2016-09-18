package StoneEngine.Math;

public class Quaternion 
{
	private float x, y, z, w;
		
	public Quaternion()
	{
		this(0,0,0,1);
	}
	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public Quaternion(Matrix4f rot)
	{
		//From Ken Shoemake's article "Quaternion Calculus and Fast Animation"
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

		if(trace > 0)
		{
			float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		}
		else
		{
			if(rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2))
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			}
			else if(rot.get(1, 1) > rot.get(2, 2))
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			}
			else
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0) ) / s;
				x = (rot.get(2, 0) + rot.get(0, 2) ) / s;
				y = (rot.get(1, 2) + rot.get(2, 1) ) / s;
				z = 0.25f * s;
			}
		}

		float length = (float)Math.sqrt(x * x + y * y + z * z + w * w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}

	public static Quaternion rotation(Vector3f axis, float angle)
	{
		float sinHalfAngle = (float)Math.sin(angle/2);
		float cosHalfAngle = (float)Math.cos(angle/2);
		
		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;
		
		return new Quaternion(rX, rY, rZ, rW);
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y + z*z + w*w);
	}
	public Quaternion normalize()
	{
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(float r)
	{
		return new Quaternion(x * r, y * r, z * r, w * r);
	}
	
	public Quaternion mul(Quaternion r)
	{
		float w_ = w * r.w - x * r.x - y * r.y - z * r.z;
		float x_ = x * r.w + w * r.x + y * r.z - z * r.y;
		float y_ = y * r.w + w * r.y + z * r.x - x * r.z;
		float z_ = z * r.w + w * r.z + x * r.y - y * r.x;
				
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion mul(Vector3f r)
	{
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ =  w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ =  w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ =  w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion nLerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		Quaternion correctedDest = dest;

		if(shortest && dot(dest) < 0)
			correctedDest = new Quaternion(-dest.x, -dest.y, -dest.z, -dest.w);

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalize();
	}

	public Quaternion sLerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		final float EPSILON = 1e3f;

		float cos = dot(dest);
		Quaternion correctedDest = dest;

		if(shortest && cos < 0)
		{
			cos = -cos;
			correctedDest = new Quaternion(-dest.x, -dest.y, -dest.z, -dest.w);
		}

		if(Math.abs(cos) >= 1 - EPSILON)
			return nLerp(correctedDest, lerpFactor, false);

		float sin = (float)Math.sqrt(1.0f - cos * cos);
		float angle = (float)Math.atan2(sin, cos);
		float invSin =  1.0f/sin;

		float srcFactor = (float)Math.sin((1.0f - lerpFactor) * angle) * invSin;
		float destFactor = (float)Math.sin((lerpFactor) * angle) * invSin;

		return mul(srcFactor).add(correctedDest.mul(destFactor));
	}
	
	public float dot(Quaternion r)
	{
		return x * r.x + y * r.y + z * r.z + w * r.w;
	}
	
	public Quaternion add(Quaternion r)
	{
		return new Quaternion(x + r.x, y + r.y, z + r.z, w + r.w);
	}
	public Quaternion sub(Quaternion r)
	{
		return new Quaternion(x - r.x, y - r.y, z - r.z, w - r.w);
	}
	
	public Vector3f getForward()
	{
		return Vector3f.ZAXIS().rotate(this);
	}

	public Vector3f getBack()
	{
		return Vector3f.ZAXIS().mul(-1).rotate(this);
	}

	public Vector3f getUp()
	{
		return Vector3f.YAXIS().rotate(this);
	}

	public Vector3f getDown()
	{
		return Vector3f.YAXIS().mul(-1).rotate(this);
	}

	public Vector3f getRight()
	{
		return Vector3f.XAXIS().rotate(this);
	}

	public Vector3f getLeft()
	{
		return Vector3f.XAXIS().mul(-1).rotate(this);
	}
	
	public Matrix4f toRotationMatrix()
	{
		Vector3f forward 	= new Vector3f(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y));
		Vector3f up 		= new Vector3f(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x));
		Vector3f right	 	= new Vector3f(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y));
		
		return Matrix4f.rotation(forward, up, right);
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

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public String toString()
	{
		return "(" + x + " " + y + " " + z + " " + w + ")";
	}
	
	public boolean equals(Quaternion r)
	{
		return  x == r.x &&
				y == r.y &&
				z == r.z &&
				w == r.w;
	}
}
