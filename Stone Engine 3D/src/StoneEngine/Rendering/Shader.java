package StoneEngine.Rendering;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.ArrayList;
import java.util.HashMap;

import StoneEngine.Core.Util;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector3f;
import StoneEngine.ResourceLoader.Textures.Texture;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.DirectionalLight;
import StoneEngine.Scene.Lighting.PointLight;
import StoneEngine.Scene.Lighting.SpotLight;
import StoneLabs.sutil.Debug;


//TODO: reformat file
public class Shader
{
	private class GLSLVariableContainer
	{
		String type;
		String name;
		
		public GLSLVariableContainer(String name,String type)
		{
			this.type = type;
			this.name = name;
		}
		
		public String getType() {
			return type;
		}
		
		public String getName() {
			return name;
		}
	}
	private class GLSLUniformContainer
	{
		String type;
		Integer location;
		
		public GLSLUniformContainer(Integer location,String type)
		{
			this.type = type;
			this.location = location;
		}
		
		@SuppressWarnings("unused")
		public String getType() {
			return type;
		}
		
		public Integer getLocation() {
			return location;
		}
	}

	private static final String KEYWORD_VEC3	= "vec3"	;
	private static final String KEYWORD_FLOAT	= "float"	;
	
	private static final String STRUCT_DIRECTIONAL	= "DirectionalLight";
	private static final String STRUCT_POINT		= "PointLight"		;
	private static final String STRUCT_SPOT			= "SpotLight"		;
	
	private static final String KEYWORD_ATTRIBUTE	= "attribute"	;
	private static final String KEYWORD_STRUCT		= "struct"		;
	private static final String KEYWORD_UNIFORM		= "uniform"		;
	private static final char UNIFORM_PREFIX_TRANSFORM			= 'T';
	private static final char UNIFORM_PREFIX_RENDERING_ENGINE	= 'R';
	private static final char UNIFORM_PREFIX_MATERIAL			= 'M';
	private static final char UNIFORM_PREFIX_CAMERA				= 'C';

	private static final String UNIFORM_KEY_TRANSFORM_WORLD_MATRIX					= "WORLDMATRIX";
	private static final String UNIFORM_KEY_TRANSFORM_PROJECTED_MATRIX				= "PROJECTEDMATRIX";
	private static final String UNIFORM_KEY_RENDERING_ENGINE_LIGHT					= "CURRENTLIGHT";
	private static final String UNIFORM_KEY_RENDERING_ENGINE_SAMPLER2D				= "SAMPLER2D";
	private static final String UNIFORM_KEY_CAMERA_EYEPOS							= "CAMERAPOS";
	
	private int program;
	
	private HashMap<String, GLSLUniformContainer> uniforms;
	private HashMap<String, String> abstractUniforms;
	
	public Shader()
	{
		program = glCreateProgram();
		uniforms = new HashMap<String, GLSLUniformContainer>();
		abstractUniforms = new HashMap<String, String>();
		
		if (program == 0x0)
			Debug.Error("Shader Creation Failed: Could not find valid memory location in constructor!");
	}
	
	public void bind()
	{
		glUseProgram(program);
	}
		
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
	{
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);

		for (String uniformName : abstractUniforms.keySet())
			if (uniformName.charAt(1) == '_')
			{
				String type = abstractUniforms.get(uniformName);
				char PREFIX = uniformName.charAt(0);
				String[] parts = uniformName.split("_");
				String name = parts[1];
				
				switch (PREFIX)
				{
					case UNIFORM_PREFIX_TRANSFORM:
						switch (name)
						{
							case UNIFORM_KEY_TRANSFORM_WORLD_MATRIX:
								setUniform(uniformName, worldMatrix);
								break;
							case UNIFORM_KEY_TRANSFORM_PROJECTED_MATRIX:
								setUniform(uniformName, projectedMatrix);
								break;
							default:
								Debug.Error(uniformName + ": Unknown transform directive: " + name);
						}
						break;
					case UNIFORM_PREFIX_RENDERING_ENGINE:
						switch (name)
						{
							case UNIFORM_KEY_RENDERING_ENGINE_SAMPLER2D:
								if (parts.length < 3)
									Debug.Error(uniformName + ": Invalid uniform name! Please specify the texture name in the third segment.");
								
								Integer samplerSlot = renderingEngine.getSamplerSlot(parts[2]);
								Texture texture = material.getTexture(parts[2]);
								if (texture == null)
									Debug.Error(uniformName + ": Texture could not be found! (" + parts[2] + ")");
								
								texture.bind(samplerSlot);
								
								setUniformi(uniformName, samplerSlot);
								break;
							case UNIFORM_KEY_RENDERING_ENGINE_LIGHT:
								switch (type)
								{
									case STRUCT_DIRECTIONAL:
										setUniformDirectionalLight(uniformName, (DirectionalLight)renderingEngine.getActiveLight());
										break;
									case STRUCT_POINT:
										setUniformPointLight(uniformName, (PointLight)renderingEngine.getActiveLight());
										break;
									case STRUCT_SPOT:
										setUniformSpotLight(uniformName, (SpotLight)renderingEngine.getActiveLight());
										break;
								}
								break;
							default:
								switch (type)
								{
									case KEYWORD_VEC3:
										setUniform(uniformName, renderingEngine.getVector3f(name));
										break;
									case KEYWORD_FLOAT:
										setUniformf(uniformName, renderingEngine.getFloat(name));
										break;
									default:
										Debug.Error(uniformName + ": Uniforms type is not supperted by the Rendering Engine (" + type + ")");
								}
						}
						break;
					case UNIFORM_PREFIX_CAMERA:
						switch (name)
						{
							case UNIFORM_KEY_CAMERA_EYEPOS:
								setUniform(uniformName, renderingEngine.getMainCamera().getGameObject().getTransformedTranslation());
								break;
							default:
								Debug.Error(uniformName + ": Unknown camera directive: " + name);
						}
						break;
					case UNIFORM_PREFIX_MATERIAL:
						switch (type)
						{
							case KEYWORD_VEC3:
								setUniform(uniformName, material.getVector3f(name));
								break;
							case KEYWORD_FLOAT:
								setUniformf(uniformName, material.getFloat(name));
								break;
							default:
								Debug.Error(uniformName + ": Uniforms type is not supperted in Material (" + type + ")");
						}
						break;
				}
			}
	}

	//TODO: Rewrite parser
	public void addAllAttrubutes(String shaderText)
	{
		int attribLocation = shaderText.indexOf(KEYWORD_ATTRIBUTE);
		
		int attribNumber = 0;
		
		while (attribLocation != -1)
		{
			if ((!Character.isWhitespace(shaderText.charAt(attribLocation-1)) && shaderText.charAt(attribLocation-1) != ';') ||
				 !Character.isWhitespace(shaderText.charAt(attribLocation + KEYWORD_ATTRIBUTE.length()))
				)
			{
				attribLocation = shaderText.indexOf(KEYWORD_ATTRIBUTE, attribLocation + KEYWORD_ATTRIBUTE.length());
				continue;
			}
			
			int start 	= attribLocation + KEYWORD_ATTRIBUTE.length() + 1;
			int end 	= shaderText.indexOf(";", start);
			
			String attrubLine = shaderText.substring(start, end).trim().replaceAll("\\s+", " ");
			String[] parts = attrubLine.split(" ");
			
			if (parts.length == 2)
			{
//				String type = parts[0];
				String name = parts[1];
				setAttribLocation(name, attribNumber++);
			}
			
			attribLocation = shaderText.indexOf(KEYWORD_ATTRIBUTE, end);
		}
	}
	
	//TODO: Rewrite parser
	private HashMap<String, ArrayList<GLSLVariableContainer>> findUniformStructs(String shaderText)
	{
		HashMap<String, ArrayList<GLSLVariableContainer>> result = new HashMap<String, ArrayList<GLSLVariableContainer>>();
		
		int structStartLocation = shaderText.indexOf(KEYWORD_STRUCT);
		
		while (structStartLocation != -1)
		{
			if ((!Character.isWhitespace(shaderText.charAt(structStartLocation-1)) && shaderText.charAt(structStartLocation-1) != ';') ||
				 !Character.isWhitespace(shaderText.charAt(structStartLocation + KEYWORD_STRUCT.length()))
				)
			{
				structStartLocation = shaderText.indexOf(KEYWORD_STRUCT, structStartLocation + KEYWORD_STRUCT.length());
				continue;
			}
			
			int nameStart 	= structStartLocation + KEYWORD_STRUCT.length() + 1;
			int braceStart 	= shaderText.indexOf("{", nameStart);
			int end		 	= shaderText.indexOf("}", braceStart);
			
			String structName = shaderText.substring(nameStart, braceStart).replaceAll("\\s", "");
			ArrayList<GLSLVariableContainer> structComponents = new ArrayList<GLSLVariableContainer>();
			
			int lastComponentSimicolonPos = braceStart;
			int componentSimicolonPos = shaderText.indexOf(";", nameStart);
			while (componentSimicolonPos != -1 && componentSimicolonPos < end)
			{
				String betweenSimicolons = shaderText.substring(lastComponentSimicolonPos+1, componentSimicolonPos);
				betweenSimicolons = betweenSimicolons.trim().replaceAll("\\s+", " ");
				
				String[] parts = betweenSimicolons.split(" ");
				
				if (parts.length == 2)
				{
					String type = parts[0];
					String name = parts[1];
					structComponents.add(new GLSLVariableContainer(name, type));
				}
								
				lastComponentSimicolonPos = componentSimicolonPos;
				componentSimicolonPos = shaderText.indexOf(";", componentSimicolonPos + 1);
			}
			
			result.put(structName, structComponents);
			
			structStartLocation = shaderText.indexOf(KEYWORD_STRUCT, end);
		}
		
		return result;
	}

	//TODO: Rewrite parser
	public void addAllUniforms(String shaderText)
	{
		HashMap<String, ArrayList<GLSLVariableContainer>> structs = findUniformStructs(shaderText);
		
//		GLSL Structs Debug:
//		for (String s : structs.keySet()) {	Debug.Log(s); for (GLSLVariableContainer member : structs.get(s)) Debug.Log(" -> " + member.getName() + " : " + member.getType()); }
		
		int unformStartLocation = shaderText.indexOf(KEYWORD_UNIFORM);
		
		while (unformStartLocation != -1)
		{
			if ((!Character.isWhitespace(shaderText.charAt(unformStartLocation-1)) && shaderText.charAt(unformStartLocation-1) != ';') ||
				 !Character.isWhitespace(shaderText.charAt(unformStartLocation + KEYWORD_UNIFORM.length()))
				)
			{
				unformStartLocation = shaderText.indexOf(KEYWORD_UNIFORM, unformStartLocation + KEYWORD_UNIFORM.length());
				continue;
			}
			
			int start 	= unformStartLocation + KEYWORD_UNIFORM.length() + 1;
			int end 	= shaderText.indexOf(";", start);
			
			String uniformLine = shaderText.substring(start, end).trim().replaceAll("\\s+", " ");
			String[] parts = uniformLine.split(" ");
			
			if (parts.length == 2)
			{
				String type = parts[0];
				String name = parts[1];
				
				addUniform(new GLSLVariableContainer(name, type), structs);
				abstractUniforms.put(name, type);
			}
			
			unformStartLocation = shaderText.indexOf(KEYWORD_UNIFORM, end);
		}
	}
	
	private void addUniform(GLSLVariableContainer uniform, HashMap<String, ArrayList<GLSLVariableContainer>> structs)
	{
		if (!structs.keySet().contains(uniform.getType()))
		{
			int uniformLocation = glGetUniformLocation(program, uniform.getName());
			
			if(uniformLocation == 0xFFFFFFFF)
				Debug.Error("Error: Could not find uniform: " + uniform.getName());
			
			uniforms.put(uniform.getName(), new GLSLUniformContainer(uniformLocation, uniform.getType()));
			
			return;
		}
		
		for (GLSLVariableContainer member : structs.get(uniform.getType()))
		{
			GLSLVariableContainer subUniformForMember = new GLSLVariableContainer(
					uniform.getName() + "." + member.getName(),
					member.getType());
			
			addUniform(subUniformForMember, structs);
		}
	}
	
	public void addUniform(String uniform, String type)
	{
	}
	
	public void addVertexShader(String text)
	{
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String text)
	{
		addProgram(text, GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String text)
	{
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	public void setAttribLocation(String attributeName, int location)
	{
		glBindAttribLocation(program, location, attributeName); //glsl v120
	}
	
	@SuppressWarnings("deprecation")
	public void compileShader()
	{
		glLinkProgram(program);
		if (glGetProgram(program, GL_LINK_STATUS) == 0)
			Debug.Error(glGetProgramInfoLog(program, 1024));
	}
	
	@SuppressWarnings("deprecation")
	private void addProgram(String text, int type)
	{
		int shader = glCreateShader(type);
		
		if (shader == 0)
			Debug.Error("Shader Creation Failed: Could not find valid memory location when adding shader!");
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if (glGetShader(shader, GL_COMPILE_STATUS) == 0)
			Debug.Error(glGetShaderInfoLog(shader, 1024));
		
		glAttachShader(program, shader);
	}
	
	public void setUniformi(String uniformName, int value)
	{
		glUniform1i(uniforms.get(uniformName).getLocation(), value);
	}
	public void setUniformf(String uniformName, float value)
	{
		glUniform1f(uniforms.get(uniformName).getLocation(), value);
	}
	public void setUniform(String uniformName, Vector3f value)
	{
		glUniform3f(uniforms.get(uniformName).getLocation(), value.getX(), value.getY(), value.getZ());
	}
	public void setUniform(String uniformName, Matrix4f value)
	{
		glUniformMatrix4(uniforms.get(uniformName).getLocation(), true, Util.createFlippedBuffer(value));
	}	
	public void setUniformBaseLight(String uniformName, BaseLight baseLight)
	{
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight)
	{
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getGameObject().getTransformedRotation().getForward());
	}
	public void setUniformPointLight(String uniformName, PointLight pointLight)
	{
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniform(uniformName + ".position", pointLight.getGameObject().getTransformedTranslation());
		setUniformf(uniformName + ".range", pointLight.getRange());
		setUniformf(uniformName + ".atten.linear", pointLight.getLinear());
		setUniformf(uniformName + ".atten.constant", pointLight.getConstant());
		setUniformf(uniformName + ".atten.exponent", pointLight.getExponent());
	}
	public void setUniformSpotLight(String uniformName, SpotLight spotLight)
	{
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getGameObject().getTransformedRotation().getForward());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
}
