package StoneEngine.Core;

public class Matrix4f 
{
	private float[][] m;
	
	public Matrix4f()
	{
		m = new float[4][4];
	}
	public Matrix4f(float[][] m)
	{
		this.m = m;
	}

	public static Matrix4f identity()
	{
		float[][] m = new float[][] {
				{1,0,0,0},
				{0,1,0,0},
				{0,0,1,0},
				{0,0,0,1}};

		return new Matrix4f(m);
	}
	public static Matrix4f translation(float x, float y, float z)
	{
		float[][] m = new float[][] {
				{1,0,0,x},
				{0,1,0,y},
				{0,0,1,z},
				{0,0,0,1}};

		return new Matrix4f(m);
	}
	public static Matrix4f scale(float x, float y, float z)
	{
		float[][] m = new float[][] {
				{x,0,0,0},
				{0,y,0,0},
				{0,0,z,0},
				{0,0,0,1}};

		return new Matrix4f(m);
	}
	public static Matrix4f projection(float fov, float width, float height, float zNear, float zFar)
	{
		float aspectRatio = width/height;
		float tanHalfFOV = (float)Math.tan(Math.toRadians(fov/2));
		float zRange = zNear - zFar;
		
		
		float[][] m = new float[][] {
				{1 / (tanHalfFOV*aspectRatio)		,0									,0						,0							},
				{0									,1 / (tanHalfFOV*aspectRatio)		,0						,0							},
				{0									,0									,(-zNear - zFar)/zRange	,2 * zFar * zNear / zRange	},
				{0									,0									,1						,0							}
			};

		return new Matrix4f(m);
	}
	public static Matrix4f camera(Vector3f forward, Vector3f up)
	{
		Vector3f f = forward.normalize();
		
		Vector3f r = up.normalize();
		r = r.cross(f);
		
		Vector3f u = f.cross(r);
		
		
		float[][] m = new float[][] {
				{r.getX()	,r.getY()	,r.getZ()	,0},
				{u.getX()	,u.getY()	,u.getZ()	,0},
				{f.getX()	,f.getY()	,f.getZ()	,0},
				{0			,0			,0			,1}};

		return new Matrix4f(m);
	}
	public static Matrix4f rotation(float x, float y, float z)
	{
		
		final float x_ = (float)Math.toRadians(x);
		final float y_ = (float)Math.toRadians(y);
		final float z_ = (float)Math.toRadians(z);
		
		float[][] rx = new float[][] {
				{1,	0,						0,						0},
				{0,	(float)Math.cos(x_),	-(float)Math.sin(x_),	0},
				{0,	(float)Math.sin(x_),	 (float)Math.cos(x_),	0},
				{0,	0,						0,						1}};
				float[][] ry = new float[][] {
					{(float)Math.cos(y_),	0,	-(float)Math.sin(y_),	0},
					{0,						1,	0,						0},
					{(float)Math.sin(y_),	0,	 (float)Math.cos(y_),	0},
					{0,						0,	0,						1}};
					float[][] rz = new float[][] {
						{(float)Math.cos(z_),	-(float)Math.sin(z_),	0,0},
						{(float)Math.sin(z_),	 (float)Math.cos(z_),	0,0},
						{0,						0,						1,0},
						{0,						0,						0,1}};
		Matrix4f rx_ = new Matrix4f(rx);
		Matrix4f ry_ = new Matrix4f(ry);
		Matrix4f rz_ = new Matrix4f(rz);
		return rz_.mul(ry_.mul(rx_));
	}
	
	public Matrix4f mul(Matrix4f r)
	{
		Matrix4f res = new Matrix4f();
		
		for (int i = 0; i < 4; i ++)
			for (int j = 0; j < 4; j ++)
				res.set(i, j, 	m[i][0] * r.m[0][j] +
								m[i][1] * r.m[1][j] +
								m[i][2] * r.m[2][j] +
								m[i][3] * r.m[3][j]);
		return res;
	}
	
	public float[][] getM() {
		return m.clone();
	}

	public float get(int x, int y)
	{
		return m[x][y];
	}
	
	public void setM(float[][] m) {
		this.m = m;
	}

	public void set(int x, int y, float val)
	{
		m[x][y] = val;
	}
	
}
