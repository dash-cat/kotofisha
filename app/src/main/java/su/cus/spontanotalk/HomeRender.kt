package su.cus.spontanotalk

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class HomeRender : GLSurfaceView.Renderer {
    private var program: Int = 0
    private var timeUniform: Int = 0
    private var resolutionUniform: Int = 0
    private var startTime: Long = 0
    private val vertices = floatArrayOf(
        -1f, -1f, 0f,  // bottom left
        1f, -1f, 0f,  // bottom right
        -1f, 1f, 0f,  // top left
        1f, 1f, 0f   // top right
    )

    private val vertexBuffer: FloatBuffer

    init {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
        vertexBuffer.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        startTime = System.currentTimeMillis()

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode))
            GLES20.glAttachShader(it, loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode))
            GLES20.glLinkProgram(it)
            GLES20.glUseProgram(it)
        }
        val aPositionLocation = GLES20.glGetAttribLocation(program, "aPosition")
        GLES20.glEnableVertexAttribArray(aPositionLocation)
        GLES20.glVertexAttribPointer(aPositionLocation, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer)
        // Check for linking errors
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            Log.e("OpenGL", "Error linking program: " + GLES20.glGetProgramInfoLog(program))
            GLES20.glDeleteProgram(program)
            return
        }
        timeUniform = GLES20.glGetUniformLocation(program, "time")
        resolutionUniform = GLES20.glGetUniformLocation(program, "resolution")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glUniform2f(resolutionUniform, width.toFloat(), height.toFloat())
    }

    override fun onDrawFrame(unused: GL10) {
        val time = (System.currentTimeMillis() - startTime) / 1000.0f
        GLES20.glUniform1f(timeUniform, time)

        // Here add code to create and draw a full-screen quad if you haven't already

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(program)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        // Check for compile errors
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.e("OpenGL", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader))
            GLES20.glDeleteShader(shader)
            return 0
        }
        return shader
    }
}
private const val vertexShaderCode = """
    attribute vec4 aPosition;
    void main() {
        gl_Position = aPosition;
    }
"""
private const val fragmentShaderCode = """
    precision mediump float;
    uniform float time;
    uniform vec2 resolution;

    void main() {
        gl_FragColor = vec4(0.5, 0.0, 0.0, 1.0); // Red color for testing
    }
"""
