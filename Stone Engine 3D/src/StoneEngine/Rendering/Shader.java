package StoneEngine.Rendering;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.HashMap;

import StoneEngine.Core.RenderingEngine;
import StoneEngine.Core.Transform;
import StoneEngine.Core.Util;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Math.Vector3f;
import StoneLabs.sutil.Debug;

public class Shader
{
	private RenderingEngine renderingEngine;
	private int program;
	
	private HashMap<String, Integer> uniforms;
	
	public Shader()
	{
		program = glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		
		if (program == 0)
			Debug.Error("Shader Creation Failed: Could not find valid memory location in constrictor!");
	}
	
	public void bind()
	{
		glUseProgram(program);
	}
		
	public void updateUniforms(Transform transform, Material material)
	{
		
	}
		
	public void addUniform(String uniform)
	{
		int uniformLocation = glGetUniformLocation(program, uniform);
		
		if (uniformLocation == 0xFFFFFF)
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
	public RenderingEngine getRenderingEngine() 
	{
		return renderingEngine;
	}
	public void setRenderingEngine(RenderingEngine renderingEngine) 
	{
		this.renderingEngine = renderingEngine;
	}
	
}
