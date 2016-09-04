package StoneEngine.Core;

import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Camera;

public class Transform 
{	
	private Vector3f translation;
	private Vector3f rotation;
	private Vector3f scale;


	public Transform()
	{
		translation = new Vector3f(0,0,0);
		rotation	= new Vector3f(0,0,0);
		scale		= new Vector3f(1,1,1);
	}
	
	public Matrix4f getTanformation() //is it really?
	{
		Matrix4f translation = Matrix4f.translation(
				this.translation.getX(), 
				this.translation.getY(), 
				this.translation.getZ())
				;
		Matrix4f rotation = Matrix4f.rotation(
				this.rotation.getX(), 
				this.rotation.getY(),  
				this.rotation.getZ())
				;
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
		this.translation = new Vector3f(x,y,z);
	}
	public Vector3f getRotation() {
		return rotation;
	}
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	public void setRotation(float x, float y, float z) {
		this.rotation = new Vector3f(x,y,z);
	}
	public Vector3f getScale() {
		return scale;
	}
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	public void setScale(float x, float y, float z) {
		this.scale = new Vector3f(x,y,z);
	}
}
