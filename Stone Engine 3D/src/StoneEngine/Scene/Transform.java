package StoneEngine.Scene;

import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Quaternion;
import StoneEngine.Math.Vector3f;
import StoneEngine.Scene.Rendering.Camera;

public class Transform 
{	
	private Vector3f translation;
	private Quaternion rotation;
	private Vector3f scale;


	public Transform()
	{
		translation = new Vector3f(0,0,0);
		rotation	= new Quaternion(0,0,0,1);
		scale		= new Vector3f(1,1,1);
	}
	
	public Matrix4f getTanformation() //is it really?
	{
		Matrix4f translation = Matrix4f.translation(
				this.translation.getX(), 
				this.translation.getY(), 
				this.translation.getZ())
				;
		
		Matrix4f rotation = this.rotation.toRotationMatrix();
		
		Matrix4f scale = Matrix4f.scale(
				this.scale.getX(), 
				this.scale.getY(),  
				this.scale.getZ())
				;
		
		return translation.mul(rotation.mul(scale));
	}

	public Vector3f getTranslation() {
		return translation;
	}
	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}
	public void setTranslation(float x, float y, float z) {
		this.translation.set(x,y,z);
	}
	public Quaternion getRotation() {
		return rotation;
	}
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
//	public void setRotation(float x, float y, float z) {
//		this.rotation = new Vector3f(x,y,z);
//	}
	public Vector3f getScale() {
		return scale;
	}
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	public void setScale(float x, float y, float z) {
		this.scale.set(x,y,z);
	}
}
