package StoneEngine.Scene;

import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Quaternion;
import StoneEngine.Math.Vector3f;
import StoneEngine.Scene.Rendering.Camera;

public class Transform 
{
	private Transform parent = null;
	private Matrix4f parentMatrix;
	
	private Vector3f translation;
	private Quaternion rotation;
	private Vector3f scale;
	
	private Vector3f old_translation;
	private Quaternion old_rotation;	//Change this method...
	private Vector3f old_scale;


	public Transform()
	{
		translation = new Vector3f(0,0,0);
		rotation	= new Quaternion(0,0,0,1);
		scale		= new Vector3f(1,1,1);

		
		parentMatrix = Matrix4f.identity();
	}
	
	public boolean hasChanged()
	{
		if (old_translation == null)
		{
			old_translation = new Vector3f(0,0,0);
			old_rotation	= new Quaternion(0,0,0,0);
			old_scale		= new Vector3f(0,0,0);
			return true;
		}
		if (parent != null)
		{
			if (parent.hasChanged())
				return true;
		}
		return !(translation.equals(old_translation) && rotation.equals(old_rotation) && scale.equals(old_scale));
	}
	
	public Matrix4f getTransformation() //is it really?
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
		
		
		if (this.old_translation != null)
		{
			this.old_translation = this.translation;
			this.old_rotation = this.rotation;
			this.old_scale = this.scale;
		}
		
		return getParentMatrix().mul(translation.mul(rotation.mul(scale)));
	}
	
	private Matrix4f getParentMatrix()
	{
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
		return parentMatrix;
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

	protected Transform getParent() {
		return parent;
	}

	protected void setParent(Transform parent) {
		this.parent = parent;
	}
}
