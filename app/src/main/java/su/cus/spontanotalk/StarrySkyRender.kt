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
import kotlin.random.Random

class StarrySkyRenderer : GLSurfaceView.Renderer {
    private val stars = mutableListOf<Star>()
    private val numStars = 500
    private var time = 0f

    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private lateinit var vertexBuffer: FloatBuffer
    private var program: Int = 0

    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var pointSizeHandle: Int = 0

    private val random = Random(System.currentTimeMillis()) // Initialize random generator

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

        stars.forEach {
            it.updatePosition(time)
            if (it.isActive) {
                drawStar(it)
            }
        }
    }

    private fun refreshStars() {
        while (stars.size < numStars) { spawnStar() }
        stars.removeAll { it.radius < 0.01f || it.pointSize < 0.5f }
    }

    private fun spawnStar() {
        val baseIntensity = (random.nextDouble() * 0.5f + 0.5f).toFloat()
        val baseColor = floatArrayOf(baseIntensity, baseIntensity, baseIntensity)
        val angle = (random.nextDouble() * 2 * Math.PI).toFloat()
        val initialRadius = (random.nextDouble() * 0.2f + 0.1f).toFloat()

        stars.add(
            Star(
                radius = initialRadius,
                angle = angle,
                speed = (random.nextDouble() * 0.05f + 0.02f).toFloat(),
                baseColor = baseColor,
                twinkleFactor = random.nextFloat(),
                pointSize = (random.nextDouble() * 3 + 1).toFloat()
            )
        )
    }

    private fun drawStar(star: Star) {
        if (!star.isActive) return // Skip drawing if the star is not active

        star.updatePosition(time)
        val starVertices = floatArrayOf(
            star.x, star.y, 0f, 1f, // Position
            star.currentColor[0], star.currentColor[1], star.currentColor[2], // Color
            star.pointSize // Point size
        )

        val finalPosition = FloatArray(4).also {
            Matrix.multiplyMV(it, 0, viewProjectionMatrix, 0, starVertices, 0)
        }

        vertexBuffer.clear()
        vertexBuffer.put(finalPosition, 0, 4) // Position
        vertexBuffer.put(star.currentColor, 0, 3) // Color
        vertexBuffer.put(star.pointSize) // Point size
        vertexBuffer.position(0)

        setVertexAttribPointerAndEnable(positionHandle, 4, 0)
        setVertexAttribPointerAndEnable(colorHandle, 3, 4)
        setVertexAttribPointerAndEnable(pointSizeHandle, 1, 7)

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
        checkGlError("drawStar")

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)
        GLES20.glDisableVertexAttribArray(pointSizeHandle)
    }

    private fun setVertexAttribPointerAndEnable(handle: Int, size: Int, offset: Int) {
        GLES20.glVertexAttribPointer(handle, size, GLES20.GL_FLOAT, false, 8 * 4, vertexBuffer.position(offset))
        GLES20.glEnableVertexAttribArray(handle)
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


class Star(
    var radius: Float,
    var angle: Float,
    var speed: Float,
    private val baseColor: FloatArray, // Use a base color to preserve the original color
    private val twinkleFactor: Float,
    var pointSize: Float = 10f,
    var isActive: Boolean = true,
    private var brightness: Float = 1f
) {
    var x: Float = kotlin.math.cos(angle) * radius
    var y: Float = kotlin.math.sin(angle) * radius
    var currentColor: FloatArray = baseColor.copyOf() // Current color, affected by brightness and twinkle

    fun updatePosition(time: Float) {
        updateMovement(time)
        updateBrightness()
        updateTwinkle()
        updateActiveState()
    }

    private fun updateMovement(time: Float) {
        angle += speed
        radius *= (0.99f - time * 0.0005f)
        x = kotlin.math.cos(angle) * radius
        y = kotlin.math.sin(angle) * radius
        pointSize = kotlin.math.max(pointSize * (1f - time * 0.0005f), 1f)
    }

    private fun updateBrightness() {
        if (Math.random() < 0.01) { // 1% chance to modify brightness
            val brightnessChange = 0.1f
            brightness += if (Math.random() < 0.5) -brightnessChange else brightnessChange
            brightness = kotlin.math.max(0f, kotlin.math.min(brightness, 1f))
        }
        currentColor = baseColor.map { it * brightness }.toFloatArray()
    }

    private fun updateTwinkle() {
        val twinkleAdjustment = (Math.random() * twinkleFactor * 2 - twinkleFactor).toFloat()
        currentColor = currentColor.map {
            kotlin.math.max(0f, kotlin.math.min(it + twinkleAdjustment, 1f))
        }.toFloatArray()
    }

    private fun updateActiveState() {
        if (Math.random() < 0.01) { // 1% chance to change active state
            isActive = !isActive
        }
    }
}

