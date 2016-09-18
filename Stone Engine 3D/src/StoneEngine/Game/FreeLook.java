package StoneEngine.Game;

import StoneEngine.Core.Input;
import StoneEngine.Math.Vector2f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Window;
import StoneEngine.Scene.GameComponent;

//Tmp
public class FreeLook extends GameComponent
{
	private boolean mouseLocked = false;
	private float sensitivity = 0.2f;
	
	@Override
	public void update(float delta)
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
				this.getGameObject().rotate(Vector3f.YAXIS(), (float)Math.toRadians( deltaPos.getX() * sensitivity));
			if(rotX)
				this.getGameObject().rotate(
									this.getGameObject().getRotation().getRight(),
									(float)Math.toRadians(-deltaPos.getY() * sensitivity));
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
		}
	}
}
