package com.morrisware.opengl.demo

import android.content.Context
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author qinyu.miao
 * @date 2021/9/15
 */
class GameGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(
    context,
    attrs
), GLSurfaceView.Renderer {

    var mProgram: Int = 0
    var mVAO = intArrayOf(1)
    var mVBO = intArrayOf(1)

    companion object {
        const val TAG = "GameGLSurfaceView"

        const val vertexShaderSource = "#version 300 es\n" +
                "layout (location = 0) in vec3 aPos;\n" +
                "void main()\n" +
                "{\n" +
                "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
                "}"
        const val fragmentShaderSource = "#version 300 es\n" +
                "out vec4 FragColor;\n" +
                "void main()\n" +
                "{\n" +
                "   FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                "}\n";
    }

    init {
        setEGLContextClientVersion(3)
        setRenderer(this)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val intBuffer = IntBuffer.allocate(512)

        val vertexShader = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vertexShader, vertexShaderSource)
        glCompileShader(vertexShader)
        glGetShaderiv(vertexShader, GL_COMPILE_STATUS, intBuffer)
//        if (intBuffer.get() != 0) {
//            Log.d(TAG, "vertexShader: ${glGetShaderInfoLog(vertexShader)}")
//        }

        val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fragmentShader, fragmentShaderSource)
        glCompileShader(fragmentShader)
        glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, intBuffer)
//        if (intBuffer.get() != 0) {
//            Log.d(TAG, "fragmentShader: ${glGetShaderInfoLog(fragmentShader)}")
//        }

        val program = glCreateProgram()
        mProgram = program
        glAttachShader(program, vertexShader)
        glAttachShader(program, fragmentShader)
        glLinkProgram(program)
        glGetProgramiv(program, GL_LINK_STATUS, intBuffer)
//        if (intBuffer.get() != 0) {
//            Log.d(TAG, "program: ${glGetProgramInfoLog(program)}")
//        }

        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)


        val aVAO = intArrayOf(1)
        val aVBO = intArrayOf(1)
        mVAO = aVAO
        mVBO = aVBO

        glGenVertexArrays(1, aVAO, 0)
        glGenBuffers(1, aVBO, 0)
        glBindVertexArray(aVAO[0])

        val vertices = FloatBuffer.wrap(
            floatArrayOf(
                -0.5f, -0.5f, 0.0f, // left
                0.5f, -0.5f, 0.0f, // right
                0.0f, 0.5f, 0.0f  // top
            )
        )
        glBindBuffer(GL_ARRAY_BUFFER, aVBO[0])
        glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), vertices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, true,3 * 4, 0)
        glEnableVertexAttribArray(0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glBindVertexArray(0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClearColor(0.5f, 0.5f, 0.5f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glUseProgram(mProgram)
        glBindVertexArray(mVAO[0])
        glDrawArrays(GL_TRIANGLES, 0 , 3)
    }

}