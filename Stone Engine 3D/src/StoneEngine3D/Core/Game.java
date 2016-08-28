package StoneEngine3D.Core;

public class Game 
{
	private Mesh mesh;
	private Shader shader;
	private Transform transform;
	
	private Material material;

	private Camera camera;

	PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1,0,0), 0.8f), new Attenuation(0, 0, 1), new Vector3f(-2,2,5),10);
	PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0,0,1), 0.8f), new Attenuation(0, 0, 1), new Vector3f(2,2,7),10);
	
	public Game()
	{
		mesh = new Mesh();
//		mesh = ResourceLoader.loadMesh("monkey.obj");
		material = new Material(
				ResourceLoader.loadTexture("test.png"),
				new Vector3f(1,1,1),
				0.4f, 8
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
		
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;
		
		Vertex[] vertices = new Vertex[] { 	new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
											new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
											new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
		
		int indices[] = { 0, 1, 2,
					      2, 1, 3};
		
		mesh.addVertices(vertices, indices, true);
		
		Transform.setProjection(70f, MainComponent.WIDTH, MainComponent.HEIGHT, 0.1f, 1000f);
		Transform.setCamera(camera);
		
		PhongShader.setAmbientLight(new Vector3f(0.0f,0.0f,0.0f));
		PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1f,1f,1f), 0.4f), new Vector3f(1,1,1)));
		
		
		PhongShader.setPointLights(new PointLight[] {pLight1, pLight2});
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
//		float sin = (float)Math.sin(tmp);
//		float posSin = Math.abs(sin);
		
		transform.setTranslation(0, -1, 5);
//		transform.setRotation(0,sin*180,0);
//		transform.setScale(posSin,posSin,posSin);
//		transform.setTranslation(0, 0, 5);
		
		pLight1.setPosition(new Vector3f(3,0,8.0f * (float)(Math.sin(tmp) + 0.5f)+10));
		pLight2.setPosition(new Vector3f(3,0,8.0f * (float)(Math.cos(tmp) + 0.5f)+10));
	}
	
	public void render() 
	{
		shader.bind();
		shader.updateUniforms(transform.getTanformation(), transform.getProjectedTransformation(), material);
		mesh.draw();
	}
}
