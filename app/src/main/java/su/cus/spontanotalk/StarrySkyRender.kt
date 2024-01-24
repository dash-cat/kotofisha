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
    private val numStars = 500 // Number of stars
    private var time = 0f

    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private lateinit var vertexBuffer: FloatBuffer
    private var program: Int = 0
    private fun checkGlError(op: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            Log.e("GLRenderer", "$op: glError $error")
            throw RuntimeException("$op: glError $error")
        }
    }
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        setupStarBuffer()

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)


        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
            checkGlError("glLinkProgram")
        }

        GLES20.glUseProgram(program)
        checkGlError("glUseProgram")

        for (i in 0 until numStars ) {
            spawnStar()
        }
    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 0.1f, 100f)
    }
    override fun onDrawFrame(gl: GL10?) {
        time += 0.02f
        val cameraZ = 1f - time * 0.1f

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, cameraZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Добавляем новые звёзды если нужно
        while (stars.size < numStars) {
            spawnStar()
        }

        val iterator = stars.iterator()
        while (iterator.hasNext()) {
            val star = iterator.next()
            star.updatePosition(time)
            drawStar(star)

            // Удаляем звёзды, которые стали слишком маленькими или достигли центра
            if (star.radius < 0.01f || star.pointSize < 0.5f) {
                iterator.remove()
            }
        }
    }






    private fun spawnStar() {
        val intensity = Math.random().toFloat() * 0.5f + 0.5f
        val color = FloatArray(3).apply {
            this[0] = intensity // Red
            this[1] = intensity // Green
            this[2] = intensity // Blue
        }
        val angle = Math.random().toFloat() * 2 * Math.PI.toFloat()
        val initialRadius = Math.random().toFloat() * 0.2f + 0.1f // Начальный радиус от 0.1 до 0.3

        stars.add(
            Star(
                radius = initialRadius,
                angle = angle,
                speed = Math.random().toFloat() * 0.05f + 0.02f,
                color = color,
                pointSize = Math.random().toFloat() * 3 + 1
            )
        )
    }


    // В методе drawStar, исправьте использование vertexAttribPointer
    private fun drawStar(star: Star) {
        val starVertices = floatArrayOf(
            star.x,
            star.y,
            0f,
            1f,
            star.color[0],
            star.color[1],
            star.color[2],
            star.pointSize
        )
        // Применение матрицы трансформации
        val finalPosition = FloatArray(4)
        Matrix.multiplyMV(finalPosition, 0, viewProjectionMatrix, 0, starVertices, 0)

        // Использование finalPosition для отрисовки звезды
        vertexBuffer.clear()
        vertexBuffer.put(finalPosition, 0, 4) // Только координаты x, y, z, w
        vertexBuffer.put(star.color) // Добавление цвета
        vertexBuffer.put(star.pointSize) // Добавление размера точки
        vertexBuffer.position(0)

        val stride = 8 * 4 // Количество байтов на одну вершину (x, y, z, w, r, g, b, pointSize)

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            4,
            GLES20.GL_FLOAT,
            false,
            stride,
            vertexBuffer
        )

        val colorHandle = GLES20.glGetAttribLocation(program, "vColor")
        GLES20.glEnableVertexAttribArray(colorHandle)
        GLES20.glVertexAttribPointer(
            colorHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            stride,
            vertexBuffer.position(4)
        )

        val pointSizeHandle = GLES20.glGetAttribLocation(program, "vPointSize")
        GLES20.glEnableVertexAttribArray(pointSizeHandle)
        GLES20.glVertexAttribPointer(
            pointSizeHandle,
            1,
            GLES20.GL_FLOAT,
            false,
            stride,
            vertexBuffer.position(7)
        )

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
            Log.e("GLRenderer", "Ошибка при отрисовке звезд")
        }


        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)
        GLES20.glDisableVertexAttribArray(pointSizeHandle)
    }


    private fun setupStarBuffer() {
        vertexBuffer = ByteBuffer.allocateDirect(numStars * 7 * 4) // x, y, r, g, b, a, point size
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
                Log.e(
                    "GLRenderer",
                    "Could not compile shader: " + GLES20.glGetShaderInfoLog(shader)
                )
                GLES20.glDeleteShader(shader)
                return 0
            }
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
        
            vec3 finalColor = mix(fColor.rgb, rainbowColor, 0.3); // Уменьшите коэффициент смешивания для более тонкого радужного эффекта
        
            gl_FragColor = vec4(finalColor, alpha);
        }
        """.trimIndent()
    }
}
class Star(var radius: Float, var angle: Float, var speed: Float, val color: FloatArray, var pointSize: Float = 10f) {
    var x: Float = Math.cos(angle.toDouble()).toFloat() * radius
    var y: Float = Math.sin(angle.toDouble()).toFloat() * radius

    fun updatePosition(time: Float) {
        angle += speed
        radius *= (0.99f - time * 0.0005f)
        x = Math.cos(angle.toDouble()).toFloat() * radius
        y = Math.sin(angle.toDouble()).toFloat() * radius
        pointSize = Math.max(pointSize * (1f - time * 0.0005f), 1f)
    }
}

