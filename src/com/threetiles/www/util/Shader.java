package com.threetiles.www.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.lwjgl.opengl.ARBShaderObjects;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

	int shaderProgram;

	public Shader(String vert, String frag) {
		shaderProgram = load("/shaders/" + vert, "/shaders/" + frag);
	}

	public void destroy() {
		glDeleteProgram(shaderProgram);
	}

	public void bind() {
		glUseProgram(shaderProgram);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public int getUniformLocation(String name) {
		return ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram, name);
	}

	private static int load(String vertexShaderLocation, String fragmentShaderLocation) {
		int shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();

		DataInputStream in = new DataInputStream(Shader.class.getResourceAsStream(vertexShaderLocation));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				vertexShaderSource.append(line).append('\n');
			}

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

		in = new DataInputStream(Shader.class.getResourceAsStream(fragmentShaderLocation));
		br = new BufferedReader(new InputStreamReader(in));

		try {
			String line;
			while ((line = br.readLine()) != null) {
				fragmentShaderSource.append(line).append('\n');
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex shader wasn't able to be compiled correctly. Error log:");
			System.err.println(glGetShaderInfoLog(vertexShader, glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)));
			return -1;
		}
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader wasn't able to be compiled correctly. Error log:");
			System.err.println(glGetShaderInfoLog(fragmentShader, glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)));
		}
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Shader program wasn't linked correctly.");
			System.err.println(glGetProgramInfoLog(shaderProgram, 1024));
			return -1;
		}
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		return shaderProgram;
	}

	public int get() {
		return this.shaderProgram;
	}
}
