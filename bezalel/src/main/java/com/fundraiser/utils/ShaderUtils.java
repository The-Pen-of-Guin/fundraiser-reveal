package com.fundraiser.utils;

import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.io.InputStream;

public class ShaderUtils {
    public static int load(InputStream vertStream, InputStream fragStream) {
        int vertShader = compileShader(vertStream, GL_VERTEX_SHADER);
        int fragShader = compileShader(fragStream, GL_FRAGMENT_SHADER);

        int program = glCreateProgram();
        glAttachShader(program, vertShader);
        glAttachShader(program, fragShader);
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(program));
        }

        glDeleteShader(vertShader);
        glDeleteShader(fragShader);
        return program;
    }

    public static int compileShader(InputStream stream, int type) {
        String source = readStream(stream); // load file contents as String
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetShaderInfoLog(shader));
        }
        return shader;
    }

    public static String readStream(InputStream stream) {
        try {
            return new String(stream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
