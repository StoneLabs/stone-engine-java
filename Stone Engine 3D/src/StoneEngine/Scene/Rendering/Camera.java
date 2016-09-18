package StoneEngine.Scene.Rendering;

import StoneEngine.Core.CoreEngine;
import StoneEngine.Core.Input;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Window;
import StoneEngine.Scene.GameComponent;

public class Camera extends GameComponent
{	
	private Matrix4f projection;
		
	public Camera(float fov, float aspect, float zNear, float zFar)
	{
		this.projection = Matrix4f.perspective(fov, aspect, zNear, zFar);
	}

	public Matrix4f getViewProjection()
	{
		Matrix4f cameraRotation = this.getGameObject().getTransformedRotation().conjugate().toRotationMatrix();
		Vector3f cameraPosition = this.getGameObject().getTransformedTranslation();
		
		Matrix4f cameraTranslation = Matrix4f.translation(
				-cameraPosition.getX(), 
				-cameraPosition.getY(), 
				-cameraPosition.getZ()
				);
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	@Override
	public void addToEngine(CoreEngine engine)
	{
		engine.getRenderingEngine().addCamera(this);
	}
}
