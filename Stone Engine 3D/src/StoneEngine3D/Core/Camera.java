package StoneEngine3D.Core;


public class Camera 
{	
	private Vector3f pos;
	private Vector3f forward;
	private Vector3f up;
		
	public Camera()
	{
		this(Vector3f.NULL(), new Vector3f(0,0,1), Vector3f.YAXIS());
	}
	public Camera(Vector3f pos, Vector3f forward, Vector3f up)
	{
		this.pos = pos;
		this.forward = forward;
		this.up = up;
	}
	
	
	public void move(Vector3f dir, float amnt)
	{
		pos = pos.add(dir.mul(amnt));
	}
	public void move(Vector3f amnt)
	{
		pos = pos.add(amnt);
	}
	
	public Vector3f getLeft()
	{
		return forward.cross(up); //no normalization needed since forward vec is orthogonal to the up vec.
								  // so |A x B| = |A| |B| sin(Theta) with Theta = 90° -> |A| |B| 1 -> One.
	}
	
	public Vector3f getRight()
	{
		return up.cross(forward);
	}
	
	public void rotateX(float angle)
	{
		Vector3f hAxis = Vector3f.YAXIS().cross(forward).normalize();
		forward = forward.rotate(angle, hAxis).normalize();
		
		up = forward.cross(hAxis).normalize();
	}
	public void rotateY(float angle)
	{
		Vector3f hAxis = Vector3f.YAXIS().cross(forward).normalize();
		forward = forward.rotate(angle, Vector3f.YAXIS()).normalize();
		
		up = forward.cross(hAxis).normalize();
	}
	
	public Vector3f getPos() {
		return pos;
	}
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	public Vector3f getForward() {
		return forward;
	}
	public void setForward(Vector3f forward) {
		this.forward = forward;
	}
	public Vector3f getUp() {
		return up;
	}
	public void setUp(Vector3f up) {
		this.up = up;
	}
}
