package su.cus.spontanotalk

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class StarrySkyRenderer : GLSurfaceView.Renderer {
    private val stars = mutableListOf<Star>()
    private val numStars = 1000
    private var time = 0f

    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private lateinit var vertexBuffer: FloatBuffer
    private var program: Int = 0

    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var pointSizeHandle: Int = 0

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        setupStarBuffer()

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().apply {
            GLES20.glAttachShader(this, vertexShader)
            GLES20.glAttachShader(this, fragmentShader)
            GLES20.glLinkProgram(this)
            checkGlError("glLinkProgram")
            GLES20.glUseProgram(this)
            checkGlError("glUseProgram")
        }

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        colorHandle = GLES20.glGetAttribLocation(program, "vColor")
        pointSizeHandle = GLES20.glGetAttribLocation(program, "vPointSize")

        repeat(numStars) { spawnStar() }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 0.1f, 100f)
    }

    override fun onDrawFrame(gl: GL10?) {
        time += 0.01f
        val cameraZ = 1f - time * 0.02f
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, cameraZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        refreshStars()
        stars.forEach { drawStar(it) }
    }

    private fun refreshStars() {
        while (stars.size < numStars) { spawnStar() }
        stars.removeAll { it.radius < 0.01f || it.pointSize < 0.5f }
    }

    private fun spawnStar() {
        val intensity = (Math.random() * 0.5f + 0.5f).toFloat()
        val color = floatArrayOf(intensity, intensity, intensity)
        val angle = (Math.random() * 2 * Math.PI).toFloat()
        val initialRadius = (Math.random() * 0.2f + 0.1f).toFloat()

        stars.add(Star(initialRadius, angle, (Math.random() * 0.05f + 0.02f).toFloat(), color, (Math.random() * 3 + 1).toFloat()))
    }

    private fun drawStar(star: Star) {
        star.updatePosition(time)
        val starVertices = floatArrayOf(star.x, star.y, 0f, 1f, star.color[0], star.color[1], star.color[2], star.pointSize)

        val finalPosition = FloatArray(4).also {
            Matrix.multiplyMV(it, 0, viewProjectionMatrix, 0, starVertices, 0)
        }

        vertexBuffer.clear()
        vertexBuffer.put(finalPosition, 0, 4)
        vertexBuffer.put(star.color)
        vertexBuffer.put(star.pointSize)
        vertexBuffer.position(0)

        val stride = 8 * 4
        GLES20.glVertexAttribPointer(positionHandle, 4, GLES20.GL_FLOAT, false, stride, vertexBuffer)
        GLES20.glEnableVertexAttribArray(positionHandle)

        vertexBuffer.position(4)
        GLES20.glVertexAttribPointer(colorHandle, 3, GLES20.GL_FLOAT, false, stride, vertexBuffer)
        GLES20.glEnableVertexAttribArray(colorHandle)

        vertexBuffer.position(7)
        GLES20.glVertexAttribPointer(pointSizeHandle, 1, GLES20.GL_FLOAT, false, stride, vertexBuffer)
        GLES20.glEnableVertexAttribArray(pointSizeHandle)

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
        checkGlError("drawStar")

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)
        GLES20.glDisableVertexAttribArray(pointSizeHandle)
    }

    private fun setupStarBuffer() {
        vertexBuffer = ByteBuffer.allocateDirect(numStars * 7 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            checkShaderError(shader, "Shader Compilation")
        }
    }

    private fun checkShaderError(shader: Int, op: String) {
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.e("GLRenderer", "$op: ${GLES20.glGetShaderInfoLog(shader)}")
            GLES20.glDeleteShader(shader)
        }
    }
    private fun checkGlError(op: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            Log.e("GLRenderer", "$op: glError $error")
            throw RuntimeException("$op: glError $error")
        }
    }

    companion object {
        private val vertexShaderCode = """
            attribute vec4 vPosition;
            attribute vec4 vColor;
            attribute float vPointSize;
            varying vec4 fColor;
            void main() {
                gl_Position = vPosition;
                gl_PointSize = vPointSize;
                fColor = vColor;
            }
        """.trimIndent()

        private val fragmentShaderCode = """
            precision mediump float;
            varying vec4 fColor;
            void main() {
                float distance = length(gl_PointCoord - vec2(0.5, 0.5));
                float alpha = 1.0 - smoothstep(0.0, 0.5, distance);
                vec3 rainbowColor = vec3(0.0);
                rainbowColor.r = 0.5 + 0.5 * cos(distance * 12.56636 + 0.0);
                rainbowColor.g = 0.5 + 0.5 * cos(distance * 12.56636 + 2.09439);
                rainbowColor.b = 0.5 + 0.5 * cos(distance * 12.56636 + 4.18879);
                vec3 finalColor = mix(fColor.rgb, rainbowColor, 0.3);
                gl_FragColor = vec4(finalColor, alpha);
            }
        """.trimIndent()
    }
}

class Star(var radius: Float, var angle: Float, var speed: Float, val color: FloatArray, var pointSize: Float = 10f) {
    var x: Float = kotlin.math.cos(angle) * radius
    var y: Float = kotlin.math.sin(angle) * radius

    fun updatePosition(time: Float) {
        angle += speed
        radius *= (0.99f - time * 0.0005f)
        x = kotlin.math.cos(angle) * radius
        y = kotlin.math.sin(angle) * radius
        pointSize = kotlin.math.max(pointSize * (1f - time * 0.0005f), 1f)
    }
}

