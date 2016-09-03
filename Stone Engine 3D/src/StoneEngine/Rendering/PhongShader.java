package StoneEngine.Rendering;

import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Transform;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector3f;
import StoneLabs.sutil.Debug;

public class PhongShader extends Shader
{
	private static PhongShader instance = new PhongShader();
	private static final int MAX_SPOT_LIGHTS = 4;
	private static final int MAX_POINT_LIGHTS = 4;
	
	public static PhongShader getInstance()
	{
		return instance;
	}
	
	private static Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
	private static DirectionalLight directionalLight = new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 0), new Vector3f(0,0,0));
	private static PointLight[] pointLights = new PointLight[0];
	private static SpotLight[] spotLights = new SpotLight[0];
	
	
	public PhongShader()
	{
		super();
				
		addVertexShader(ResourceLoader.loadShader("phongVertex.vs"));
		addFragmentShader(ResourceLoader.loadShader("phongFragment.fs"));
		compileShader();
		
		addUniform("transform");
		addUniform("transformProjected");
		addUniform("baseColor");
		addUniform("ambientLight");
		
		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("eyePos");
		
		addUniform("directionalLight.base.color");
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");
		
		for (int i = 0; i < MAX_POINT_LIGHTS; i++)
		{
			addUniform("pointLights["+i+"].base.color");
			addUniform("pointLights["+i+"].base.intensity");
			addUniform("pointLights["+i+"].atten.constant");
			addUniform("pointLights["+i+"].atten.linear");
			addUniform("pointLights["+i+"].atten.exponent");
			addUniform("pointLights["+i+"].position");
			addUniform("pointLights["+i+"].range");
		}
		
		for (int i = 0; i < MAX_SPOT_LIGHTS; i++)
		{
			addUniform("spotLights["+i+"].pointLight.base.color");
			addUniform("spotLights["+i+"].pointLight.base.intensity");
			addUniform("spotLights["+i+"].pointLight.atten.constant");
			addUniform("spotLights["+i+"].pointLight.atten.linear");
			addUniform("spotLights["+i+"].pointLight.atten.exponent");
			addUniform("spotLights["+i+"].pointLight.position");
			addUniform("spotLights["+i+"].pointLight.range");
			addUniform("spotLights["+i+"].direction");
			addUniform("spotLights["+i+"].cutoff");
		}
	}
	
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material)
	{
		material.getTexture().bind();
		
		setUniform("transform", worldMatrix);
		setUniform("transformProjected", projectedMatrix);
		setUniform("baseColor", material.getColor());
		
		setUniform("ambientLight", ambientLight);
		setUniform("directionalLight", directionalLight);
		

		for (int i = 0; i < pointLights.length; i++)
			setUniform("pointLights["+i+"]", pointLights[i]);
		for (int i = 0; i < spotLights.length; i++)
			setUniform("spotLights["+i+"]", spotLights[i]);
		
		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularExponent", material.getSpecularExponent());
		
		setUniform("eyePos", Transform.getCamera().getPos());
	}

	public static Vector3f getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(Vector3f ambientLight) {
		PhongShader.ambientLight = ambientLight;
	}

	public static DirectionalLight getDirectionalLight() {
		return directionalLight;
	}

	public static void setDirectionalLight(DirectionalLight directionalLight) {
		PhongShader.directionalLight = directionalLight;
	}

	public void setUniform(String uniformName, BaseLight baseLight)
	{
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	public void setUniform(String uniformName, DirectionalLight directionalLight)
	{
		setUniform(uniformName + ".base", directionalLight.getBase());
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
	
	public void setUniform(String uniformName, PointLight pointLight)
	{
		setUniform(uniformName + ".base", pointLight.getBaseLight());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
		setUniformf(uniformName + ".atten.linear", pointLight.getAtten().getLinear());
		setUniformf(uniformName + ".atten.constant", pointLight.getAtten().getConstant());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAtten().getExponent());
	}
	public void setUniform(String uniformName, SpotLight spotLight)
	{
		setUniform(uniformName + ".pointLight", spotLight.getPointLight());
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}

	public static PointLight[] getPointLights() {
		return pointLights;
	}

	public static void setPointLights(PointLight[] pointLights) {
		if (pointLights.length > MAX_POINT_LIGHTS) Debug.Error("Too many point lights! Max. " + MAX_POINT_LIGHTS);
		PhongShader.pointLights = pointLights;
	}

	public static SpotLight[] getSpotLights() {
		return spotLights;
	}

	public static void setSpotLights(SpotLight[] spotLights) {
		if (spotLights.length > MAX_SPOT_LIGHTS) Debug.Error("Too many spot lights! Max. " + MAX_SPOT_LIGHTS);
		PhongShader.spotLights = spotLights;
	}
	
	
}
