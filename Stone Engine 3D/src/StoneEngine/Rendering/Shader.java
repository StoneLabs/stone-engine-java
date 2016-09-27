package StoneEngine.Rendering;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static final String KEYWORD_ATTRIBUTE	= "attribute"	;
	private static final String KEYWORD_STRUCT		= "struct"		;
	private static final String KEYWORD_UNIFORM		= "uniform"		;
	
	public static class UnisetCommandGroups
	{
		public static final String CAMERA		= "camera";
		public static final String TRANSFORM	= "transform";
		public static final String RENDERING 	= "rendering";
		public static final String MATERIAL		= "material";

		public static class Camera
		{
			public static final String POSITION 				= "position";
		}
		
		public static class Transform
		{
			public static final String WORLDMATRIX 		= "world_matrix";
			public static final String PROJECTEDMATRIX	= "projected_matrix";
		}

		public static class Rendering
		{
			public static final String LIGHT_DIRECTIONAL	= "directional_light";
			public static final String LIGHT_POINT			= "point_light";
			public static final String LIGHT_SPOT			= "spot_light";
		}
	}

	public static class UnisetCommandTypes
	{
		public static final String FLOAT		= "float";
		public static final String SAMPLER2D	= "texture";
		public static final String VECTOR3F		= "vec3";
	}
	
	private class UnisetCommand
	{
		
		
		private String group;
		private String argument;
		private String type;
		
		public UnisetCommand(String group, String argument, String type)
		{
			this.group = group;
			this.argument = argument;
			this.type = type;
		}

		public String getGroup() {
			return group;
		}

		public String getArgument() {
			return argument;
		}

		public String getType() {
			return type;
		}
	}
	
	private int program;

	private HashMap<String, UnisetCommand> unisetInstructions;
	private HashMap<String, GLSLUniformContainer> uniforms;
	private HashMap<String, String> abstractUniforms;
	
	public Shader()
	{
		program = glCreateProgram();
		uniforms = new HashMap<String, GLSLUniformContainer>();
		unisetInstructions = new HashMap<String, UnisetCommand>();
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
			if (unisetInstructions.containsKey(uniformName))
			{
				UnisetCommand unisetCommand = unisetInstructions.get(uniformName);
				
				switch (unisetCommand.getGroup())
				{
					case UnisetCommandGroups.CAMERA:
						switch (unisetCommand.getArgument())
						{
							case UnisetCommandGroups.Camera.POSITION:
								setUniform(uniformName, renderingEngine.getMainCamera().getGameObject().getTransformedTranslation());
								break;
							default:
								Debug.Error(uniformName + ": Illigal uniset command!");
						}
						break;
					case UnisetCommandGroups.TRANSFORM:
						switch (unisetCommand.getArgument())
						{
							case UnisetCommandGroups.Transform.PROJECTEDMATRIX:
								setUniform(uniformName, projectedMatrix);
								break;
							case UnisetCommandGroups.Transform.WORLDMATRIX:
								setUniform(uniformName, worldMatrix);
								break;
							default:
								Debug.Error(uniformName + ": Illigal uniset command!");
						}
						break;
					case UnisetCommandGroups.RENDERING:
						switch (unisetCommand.getArgument())
						{
							case UnisetCommandGroups.Rendering.LIGHT_DIRECTIONAL:
								setUniformDirectionalLight(uniformName, (DirectionalLight)renderingEngine.getActiveLight());
								break;
							case UnisetCommandGroups.Rendering.LIGHT_POINT:
								setUniformPointLight(uniformName, (PointLight)renderingEngine.getActiveLight());
								break;
							case UnisetCommandGroups.Rendering.LIGHT_SPOT:
								setUniformSpotLight(uniformName, (SpotLight)renderingEngine.getActiveLight());
								break;
							default:
								switch (unisetCommand.getType())
								{
									case UnisetCommandTypes.VECTOR3F:
										setUniform(uniformName, renderingEngine.getVector3f(unisetCommand.getArgument()));
										break;
									case UnisetCommandTypes.FLOAT:
										setUniformf(uniformName, renderingEngine.getFloat(unisetCommand.getArgument()));
										break;
									default:
										Debug.Error(uniformName + ": Uniforms type is not supperted in RenderingEngine (" + unisetCommand.getType() + ")");
								}
								break;
						}
						break;
					case UnisetCommandGroups.MATERIAL:
						switch (unisetCommand.getType())
						{
							case UnisetCommandTypes.VECTOR3F:
								setUniform(uniformName, material.getVector3f(unisetCommand.getArgument()));
								break;
							case UnisetCommandTypes.FLOAT:
								setUniformf(uniformName, material.getFloat(unisetCommand.getArgument()));
								break;
							case UnisetCommandTypes.SAMPLER2D:
								Integer samplerSlot = renderingEngine.getSamplerSlot(unisetCommand.getArgument());
								Texture texture = material.getTexture(unisetCommand.getArgument());
								if (texture == null)
									Debug.Error(uniformName + ": Texture could not be found! (" + unisetCommand.getArgument() + ")");
								
								texture.bind(samplerSlot);
								
								setUniformi(uniformName, samplerSlot);
								break;
							default:
								Debug.Error(uniformName + ": Uniforms type is not supperted in Material (" + unisetCommand.getType() + ")");
						}
						break;
					default:
						Debug.Error(uniformName + ": Illigal uniset command!");
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
	
	private String removeAndRegisterUniset(String text)
	{      
		final String pattern = "((?i)#uniset)\\s+([A-Za-z0-9_-]+)\\s+([A-Za-z0-9_:-]+)([\\r\\t\\t ]+)?\\n";
		//ABOVE REGEX equals any #uniset   var    bla (with uniset case insensitve)
		
		Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(text);
	    
	    while (m.find())
	    {
	    	String[] argsParts = m.group(3).split(":");
	    	if (argsParts.length > 3)
	    		Debug.Error("Uniset argument consists of more than three parts!");
	    	if (argsParts.length < 2)
	    		Debug.Error("Uniset argument consists of less than two arguments!");
	    	
	    	unisetInstructions.put(m.group(2), new UnisetCommand(
	    			argsParts[0], argsParts[1],
	    	    	argsParts.length > 2 ? argsParts[2] : null));
	    }
	    
		return text.replaceAll(pattern, "");
	}
	
	public void addVertexShader(String text)
	{
		text = removeAndRegisterUniset(text);
		addProgram(text, GL_VERTEX_SHADER);
		addAllAttrubutes(text);
	}
	
	public void addGeometryShader(String text)
	{
		text = removeAndRegisterUniset(text);
		addProgram(text, GL_GEOMETRY_SHADER);
		addAllAttrubutes(text);
	}
	
	public void addFragmentShader(String text)
	{
		text = removeAndRegisterUniset(text);
		addProgram(text, GL_FRAGMENT_SHADER);
		addAllAttrubutes(text);
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
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
	}
	public void setUniformSpotLight(String uniformName, SpotLight spotLight)
	{
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getGameObject().getTransformedRotation().getForward());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
}
