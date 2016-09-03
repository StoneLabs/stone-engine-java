package StoneEngine.Rendering;

import StoneEngine.Core.Input;
import StoneEngine.Core.Time;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;

public class Camera 
{	
	private Vector3f pos;
	private Vector3f forward;
	private Vector3f up;
	private Matrix4f projection;
		
	public Camera(float fov, float aspect, float zNear, float zFar)
	{
		this.pos = new Vector3f(0,0,0);
		this.forward = new Vector3f(0,0,1);
		this.up = new Vector3f(0,1,0);
		this.projection = Matrix4f.perspective(fov, aspect, zNear, zFar);
	}

	public Matrix4f getViewProjection()
	{
		Matrix4f cameraRotation = Matrix4f.rotation(forward, up);
		Matrix4f cameraTranslation = Matrix4f.translation(-pos.getX(), -pos.getY(), -pos.getZ());
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	private boolean mouseLocked = false;
	public void input(float delta)
	{
		Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
		
		float moveAmnt = (float)(10 * delta);
		float rotAmnt = (float)(100 * delta);
		float sensitivity = 0.2f;
		
		if (Input.getKey(Input.Keys.KEY_W))
			this.move(this.getForward(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_S))
			this.move(this.getForward(), -moveAmnt);
		if (Input.getKey(Input.Keys.KEY_A))
			this.move(this.getLeft(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_D))
			this.move(this.getRight(), moveAmnt);
		
		if (Input.getKey(Input.Keys.KEY_UP))
			this.rotateX(-rotAmnt);
		if (Input.getKey(Input.Keys.KEY_DOWN))
			this.rotateX(rotAmnt);
		if (Input.getKey(Input.Keys.KEY_LEFT))
			this.rotateY(-rotAmnt);
		if (Input.getKey(Input.Keys.KEY_RIGHT))
			this.rotateY(rotAmnt);
		

		if(Input.getKey(Input.Keys.KEY_ESCAPE))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}
//		Debug.Log("MB 0: " + Input.getMouse(0));
//		Debug.Log("MB 1: " + Input.getMouse(1));
//		Debug.Log("MB 2: " + Input.getMouse(2));
//		Debug.Log("MB 3: " + Input.getMouse(3));
//		Debug.Log("MB 4: " + Input.getMouse(4));
		if(Input.getMouseDown(0))
		{
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}
		if(mouseLocked)
		{
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				this.rotateY(deltaPos.getX() * sensitivity);
			if(rotX)
				this.rotateX(-deltaPos.getY() * sensitivity);
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
		}
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
