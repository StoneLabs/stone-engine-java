package StoneEngine3D.Core;

import StoneLabs.sutil.Debug;

public class Game 
{
	private Mesh mesh;
	private Shader shader;
	private Transform transform;
	
	private Material material;

	private Camera camera;
	
	public Game()
	{
//		mesh = new Mesh();
		mesh = ResourceLoader.loadMesh("monkey.obj");
		material = new Material(
				ResourceLoader.loadTexture("white_pixel.png"),
				new Vector3f(1,1,1)
				);
		shader = PhongShader.getInstance();
		camera = new Camera();
		transform = new Transform();
		
//		Vertex[] vertices = new Vertex[] {	
//										new Vertex(	new Vector3f(-1, -1, 0), new Vector2f(0		,0		)),
//										new Vertex(	new Vector3f( 0,  1, 0), new Vector2f(0.5f	,0		)),
//										new Vertex(	new Vector3f( 1, -1, 0), new Vector2f(1.0f	,0		)),
//										new Vertex( new Vector3f( 0, -1, 1), new Vector2f(0.5f	,1.0f	)),
//										};
//		
//		int[] indices = new int[] {	3,1,0,
//									2,1,3,
//									0,1,2,
//									0,2,3};
//		
//		mesh.addVertices(vertices, indices, true);
		
		Transform.setProjection(70f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 1000f);
		Transform.setCamera(camera);
		
		PhongShader.setAmbientLight(new Vector3f(0.1f,0.1f,0.1f));
		PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 0.8f), new Vector3f(1,1,1)));
	}
	
	public void input()
	{
		float moveAmnt = (float)(10 * Time.getDelta());
		float rotAmnt = (float)(100 * Time.getDelta());
		
		if (Input.getKey(Input.Keys.KEY_W))
			camera.move(camera.getForward(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_S))
			camera.move(camera.getForward(), -moveAmnt);
		if (Input.getKey(Input.Keys.KEY_A))
			camera.move(camera.getLeft(), moveAmnt);
		if (Input.getKey(Input.Keys.KEY_D))
			camera.move(camera.getRight(), moveAmnt);
		
		if (Input.getKey(Input.Keys.KEY_UP))
			camera.rotateX(-rotAmnt);
		if (Input.getKey(Input.Keys.KEY_DOWN))
			camera.rotateX(rotAmnt);
		if (Input.getKey(Input.Keys.KEY_LEFT))
			camera.rotateY(-rotAmnt);
		if (Input.getKey(Input.Keys.KEY_RIGHT))
			camera.rotateY(rotAmnt);
	}

	
	float tmp = 0.0F;
	public void update()
	{
		tmp += Time.getDelta();
//
		float sin = (float)Math.sin(tmp);
//		float posSin = Math.abs(sin);
		
		transform.setTranslation(0, 0, 5);
		transform.setRotation(0,sin*180,0);
//		transform.setScale(posSin,posSin,posSin);
//		transform.setTranslation(0, 0, 5);
	}
	
	public void render() 
	{
		shader.bind();
		shader.updateUniforms(transform.getTanformation(), transform.getProjectedTransformation(), material);
		mesh.draw();
	}
}
