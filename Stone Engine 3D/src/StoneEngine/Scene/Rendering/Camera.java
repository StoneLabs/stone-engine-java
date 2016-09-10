package StoneEngine.Scene.Rendering;

import StoneEngine.Core.Input;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Quaternion;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
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
		Matrix4f cameraRotation = this.getGameObject().getRotation().toRotationMatrix();
		Matrix4f cameraTranslation = Matrix4f.translation(
				-this.getGameObject().getTranslation().getX(), 
				-this.getGameObject().getTranslation().getY(), 
				-this.getGameObject().getTranslation().getZ()
				);
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	//Temporary solution!
	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine)
	{
		renderingEngine.addCamera(this);
	}
	
	private boolean mouseLocked = false;
	private float sensitivity = -0.2f;
	@Override
	public void input(float delta)
	{
		Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
		
		float moveAmnt = (float)(10 * delta);
		
		if (Input.getKey(Input.Keys.KEY_W))
			this.getGameObject().move(this.getGameObject().getRotation().getForward(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_S))
			this.getGameObject().move(this.getGameObject().getRotation().getForward(), -moveAmnt);
		if (Input.getKey(Input.Keys.KEY_A))
			this.getGameObject().move(this.getGameObject().getRotation().getLeft(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_D))
			this.getGameObject().move(this.getGameObject().getRotation().getRight(), moveAmnt);


		if(Input.getKey(Input.Keys.KEY_ESCAPE))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}

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
				this.getGameObject().setRotation(
					this.getGameObject().getRotation().mul(
							Quaternion.rotation(
									Vector3f.YAXIS(),
									(float)Math.toRadians( deltaPos.getX() * sensitivity))).normalize());
			if(rotX)
				this.getGameObject().setRotation(
					this.getGameObject().getRotation().mul(
							Quaternion.rotation(
									this.getGameObject().getRotation().getRight(),
									(float)Math.toRadians(-deltaPos.getY() * sensitivity))).normalize());
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
		}
	}
}
