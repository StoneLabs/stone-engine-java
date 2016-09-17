package StoneEngine.Rendering.Shading;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.ArrayList;
import java.util.HashMap;

import StoneEngine.Core.Util;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Scene.Transform;
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

		@SuppressWarnings("unused")
		public String getType() {
			return type;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}
	}
	
	private int program;
	
	private HashMap<String, Integer> uniforms;
	
	public Shader()
	{
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		
		if (program == 0x0)
			Debug.Error("Shader Creation Failed: Could not find valid memory location in constructor!");
	}
	public Shader(String vertexShader)
	{
		this(vertexShader, null, null);
	}
	public Shader(String vertexShader, String fragmentShader)
	{
		this(vertexShader, fragmentShader, null);
	}
	public Shader(String vertexShader, String fragmentShader, String geometryShader)
	{
		this();
		
		if (vertexShader != null) addVertexShader(vertexShader);
		if (fragmentShader != null) addFragmentShader(fragmentShader);
		if (geometryShader != null) addGeometryShader(geometryShader);

		if (vertexShader != null) addAllAttrubutes(vertexShader);
		if (fragmentShader != null) addAllAttrubutes(fragmentShader);
		if (geometryShader != null) addAllAttrubutes(geometryShader);
		
		compileShader();
		
		if (vertexShader != null) addAllUniforms(vertexShader);
		if (fragmentShader != null) addAllUniforms(fragmentShader);
		if (geometryShader != null) addAllUniforms(geometryShader);
	}
	
	public void bind()
	{
		glUseProgram(program);
	}
		
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
	{
		
	}

	//TODO: Rewrite parser
	public void addAllAttrubutes(String shaderText)
	{
		final String ATTRIBUTE_KEYWORD = "attribute";
		int attribLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		
		int attribNumber = 0;
		
		while (attribLocation != -1)
		{
			if ((!Character.isWhitespace(shaderText.charAt(attribLocation-1)) && shaderText.charAt(attribLocation-1) != ';') ||
				 !Character.isWhitespace(shaderText.charAt(attribLocation + ATTRIBUTE_KEYWORD.length()))
				)
			{
				attribLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attribLocation + ATTRIBUTE_KEYWORD.length());
				continue;
			}
			
			int start 	= attribLocation + ATTRIBUTE_KEYWORD.length() + 1;
			int end 	= shaderText.indexOf(";", start);
			
			String attrubLine = shaderText.substring(start, end).trim().replaceAll("\\s+", " ");
			String[] parts = attrubLine.split(" ");
			
			if (parts.length == 2)
			{
//				String type = parts[0];
				String name = parts[1];
				setAttribLocation(name, attribNumber++);
			}
			
			attribLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, end);
		}
	}
	
	//TODO: Rewrite parser
	private HashMap<String, ArrayList<GLSLVariableContainer>> findUniformStructs(String shaderText)
	{
		HashMap<String, ArrayList<GLSLVariableContainer>> result = new HashMap<String, ArrayList<GLSLVariableContainer>>();
		
		final String STRUCT_KEYWORD = "struct";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		
		while (structStartLocation != -1)
		{
			if ((!Character.isWhitespace(shaderText.charAt(structStartLocation-1)) && shaderText.charAt(structStartLocation-1) != ';') ||
				 !Character.isWhitespace(shaderText.charAt(structStartLocation + STRUCT_KEYWORD.length()))
				)
			{
				structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
				continue;
			}
			
			int nameStart 	= structStartLocation + STRUCT_KEYWORD.length() + 1;
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
			
			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, end);
		}
		
		return result;
	}

	//TODO: Rewrite parser
	public void addAllUniforms(String shaderText)
	{
		HashMap<String, ArrayList<GLSLVariableContainer>> structs = findUniformStructs(shaderText);
		
//		GLSL Structs Debug:
//		for (String s : structs.keySet()) {	Debug.Log(s); for (GLSLVariableContainer member : structs.get(s)) Debug.Log(" -> " + member.getName() + " : " + member.getType()); }
		
		final String UNIFORM_KEYWORD = "uniform";
		int unformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		
		while (unformStartLocation != -1)
		{
			if ((!Character.isWhitespace(shaderText.charAt(unformStartLocation-1)) && shaderText.charAt(unformStartLocation-1) != ';') ||
				 !Character.isWhitespace(shaderText.charAt(unformStartLocation + UNIFORM_KEYWORD.length()))
				)
			{
				unformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, unformStartLocation + UNIFORM_KEYWORD.length());
				continue;
			}
			
			int start 	= unformStartLocation + UNIFORM_KEYWORD.length() + 1;
			int end 	= shaderText.indexOf(";", start);
			
			String uniformLine = shaderText.substring(start, end).trim().replaceAll("\\s+", " ");
			String[] parts = uniformLine.split(" ");
			
			if (parts.length == 2)
			{
				String type = parts[0];
				String name = parts[1];
				addUniformWhithStructCheck(new GLSLVariableContainer(name, type), structs);
			}
			
			unformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, end);
		}
	}
	
	private void addUniformWhithStructCheck(GLSLVariableContainer uniform, HashMap<String, ArrayList<GLSLVariableContainer>> structs)
	{
		if (!structs.keySet().contains(uniform.getType()))
		{
			addUniform(uniform.getName());
			return;
		}
		
		for (GLSLVariableContainer member : structs.get(uniform.getType()))
		{
			GLSLVariableContainer subUniformForMember = new GLSLVariableContainer(
					uniform.getName() + "." + member.getName(),
					member.getType());
			
			addUniformWhithStructCheck(subUniformForMember, structs);
		}
	}
	
	public void addUniform(String uniform)
	{
		int uniformLocation = glGetUniformLocation(program, uniform);
		
		if(uniformLocation == 0xFFFFFFFF)
			Debug.Error("Error: Could not find uniform: " + uniform);
		
		uniforms.put(uniform, uniformLocation);
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
		glUniform1i(uniforms.get(uniformName), value);
	}
	public void setUniformf(String uniformName, float value)
	{
		glUniform1f(uniforms.get(uniformName), value);
	}
	public void setUniform(String uniformName, Vector3f value)
	{
		glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
	}
	public void setUniform(String uniformName, Matrix4f value)
	{
		glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
	}	
}
