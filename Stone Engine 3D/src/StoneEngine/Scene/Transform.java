package StoneEngine.Scene;

import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Quaternion;
import StoneEngine.Math.Vector3f;

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

	private boolean hasChanged = true;

	public Transform()
	{
		translation = new Vector3f(0,0,0);
		rotation	= new Quaternion(0,0,0,1);
		scale		= new Vector3f(1,1,1);
		
		this.old_translation 	= new Vector3f(0,0,0);
		this.old_rotation 		= new Quaternion(0,0,0,1);
		this.old_scale 			= new Vector3f(1,1,1);

		
		parentMatrix = Matrix4f.identity();
	}
	
	protected void update()
	{
		hasChanged = false;
		if (parent != null)
		{
			if (parent.hasChanged())
				hasChanged = true;
		}
		
		if (!translation.equals(old_translation))
			hasChanged = true;
		if (!rotation.equals(old_rotation))
			hasChanged = true;
		if (!scale.equals(old_scale))
			hasChanged = true;
		
		this.old_translation = this.translation;
		this.old_rotation = this.rotation;
		this.old_scale = this.scale;
		
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
	}
	
	public void rotate(Vector3f axis, float angle)
	{
		rotation = Quaternion.rotation(axis, angle).mul(rotation).normalize();
	}

	public boolean hasChanged() { return hasChanged; }
	
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
				
		return parentMatrix.mul(translation.mul(rotation.mul(scale)));
	}
	
	private Matrix4f getParentMatrix()
	{
		return parentMatrix;
	}

	public Vector3f getTransformedTranslation()
	{
		return getParentMatrix().transform(translation);
	}
	public Quaternion getTransformedRotation()
	{
		Quaternion parentRotation = new Quaternion(0,0,0,1);
		
		if (parent != null)
			parentRotation = parent.getTransformedRotation();
		
		return parentRotation.mul(rotation);
	}

	public void lookAt(Vector3f target, Vector3f up)
	{
		rotation = getLookAtDirection(target, up);
	}
	public Quaternion getLookAtDirection(Vector3f target, Vector3f up)
	{
		return new Quaternion(Matrix4f.rotation(target.sub(translation).normalize(), up));
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
