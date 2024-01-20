package su.cus.spontanotalk

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

class StarrySkyRenderer : GLSurfaceView.Renderer {
    private val stars = mutableListOf<Star>()
    private val numStars = 1000 // Number of stars

    private lateinit var vertexBuffer: FloatBuffer
    private var program: Int = 0

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        setupStarBuffer()

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // Create OpenGL program and attach shaders
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)

            // Check for linking errors
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(it, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                Log.e("GLRenderer", "Error linking program: " + GLES20.glGetProgramInfoLog(it))
                GLES20.glDeleteProgram(it)
                return
            }
        }

        GLES20.glUseProgram(program)

        // Initialize stars
        for (i in 0 until numStars) {
            spawnStar()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Draw stars
        stars.forEach { star ->
            star.updatePosition()
            drawStar(star)
        }
    }

    private fun spawnStar() {
        stars.add(Star(
            x = (Math.random().toFloat() * 2 - 1) / 8, // Random X in [-1, 1]
            y = (Math.random().toFloat() * 2 - 1) / 8, // Random Y in [-1, 1]
            speed = Math.random().toFloat() * 0.01f // Random speed
        ))
    }

    private fun drawStar(star: Star) {
        val starVertices = floatArrayOf(star.x, star.y, 0.0f, star.alpha)

        vertexBuffer.clear()
        vertexBuffer.put(starVertices)
        vertexBuffer.position(0)

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 4, GLES20.GL_FLOAT, false, 16, vertexBuffer)

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }


    private fun setupStarBuffer() {
        vertexBuffer = ByteBuffer.allocateDirect(numStars * 3 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            // Check for compilation errors
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                Log.e("GLRenderer", "Could not compile shader: " + GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
                return 0
            }
        }
    }

    companion object {
        private val vertexShaderCode = """
            attribute vec4 vPosition;
            varying float vAlpha;
            void main() {
                gl_Position = vec4(vPosition.xy, 0.0, 1.0);
                gl_PointSize = 10.0;
                vAlpha = vPosition.w;
            }
        """.trimIndent()

        private val fragmentShaderCode = """
            precision mediump float;
            varying float vAlpha;
            void main() {
                gl_FragColor = vec4(1.0, 1.0, 1.0, vAlpha);
            }
        """.trimIndent()
    }
}

class Star(var x: Float, var y: Float, var speed: Float, var alpha: Float = 1.0f) {
    fun updatePosition() {
        x += x * speed
        y += y * speed
        alpha = ((Math.random() * 0.5) + 0.5).toFloat() // Random alpha between 0.5 and 1.0
        if (sqrt(x * x + y * y) > 50) {
            resetStar()
        }
    }

    private fun resetStar() {
        x = Math.random().toFloat() * 2 - 1
        y = Math.random().toFloat() * 2 - 1
        alpha = 1.0f
    }
}