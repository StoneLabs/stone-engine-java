package StoneEngine3D.Core;

public class Transform 
{
	private static float zNear;
	private static float zFar;
	private static float width;
	private static float height;
	private static float fov;
	
	private static Camera camera;
	
	public static void setFov(float fov) {
		Transform.fov = fov;
	}
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
	
	public Matrix4f getProjectedTransformation()
	{
		Matrix4f transformationMatrix = getTanformation();
		Matrix4f projectionMatrix = Matrix4f.projection(fov, width, height, zNear, zFar);
		Matrix4f cameraRotation = Matrix4f.camera(camera.getForward(), camera.getUp());
		Matrix4f cameraTranslation = Matrix4f.translation(-camera.getPos().getX(), -camera.getPos().getY(), -camera.getPos().getZ());
		 
		return projectionMatrix.mul(
				cameraRotation.mul(
						cameraTranslation.mul(
								transformationMatrix
								)));
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
	public static float getzNear() {
		return zNear;
	}
	public static void setProjection(float fov, float width, float height, float zNear, float zFar) {
		Transform.fov 		= fov;
		Transform.width 	= width;
		Transform.height 	= height;
		Transform.zNear 	= zNear;
		Transform.zFar 		= zFar;
	}
	public static void setzNear(float zNear) {
		Transform.zNear = zNear;
	}
	public static float getzFar() {
		return zFar;
	}
	public static void setzFar(float zFar) {
		Transform.zFar = zFar;
	}
	public static float getWidth() {
		return width;
	}
	public static void setWidth(float width) {
		Transform.width = width;
	}
	public static float getHeight() {
		return height;
	}
	public static void setHeight(float height) {
		Transform.height = height;
	}
	public static float getFov() {
		return fov;
	}
	public static Camera getCamera() {
		return camera;
	}
	public static void setCamera(Camera camera) {
		Transform.camera = camera;
	}
}
