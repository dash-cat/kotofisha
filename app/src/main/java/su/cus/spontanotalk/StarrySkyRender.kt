package su.cus.spontanotalk

import android.opengl.GLES20
import android.opengl.GLSurfaceView
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
        time += 0.2f // Меняйте это значение для регулировки скорости анимации

        val uTimeLocation = GLES20.glGetUniformLocation(program, "uTime")
        GLES20.glUniform1f(uTimeLocation, time)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Draw stars
        stars.forEach { star ->
            star.updatePosition()
            drawStar(star)
        }
    }

    private fun spawnStar() {
        val intensity = Math.random().toFloat() * 0.5f + 0.5f // Интенсивность цвета от 0.5 до 1.0
        val color = FloatArray(3).apply {
            this[0] = intensity // Red
            this[1] = intensity // Green
            this[2] = intensity // Blue
        }
        stars.add(Star(
            x = (Math.random().toFloat() * 2 - 1),
            y = (Math.random().toFloat() * 2 - 1),
            speed = Math.random().toFloat() * 0.01f,
            color = color,
            pointSize = Math.random().toFloat() * 5 + 2 // Размер точки от 2 до 7
        ))
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

        vertexBuffer.clear()
        vertexBuffer.put(starVertices)
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
                rainbowColor.r = 0.5 + 0.5 * cos(distance * 6.28318 + 0.0);
                rainbowColor.g = 0.5 + 0.5 * cos(distance * 6.28318 + 2.09439);
                rainbowColor.b = 0.5 + 0.5 * cos(distance * 6.28318 + 4.18879);
        
                vec3 finalColor = mix(fColor.rgb, rainbowColor, 0.5); // Смешиваем основной цвет звезды с радужным цветом
        
                gl_FragColor = vec4(finalColor, alpha);
            }
        """.trimIndent()
    }

    class Star(
        var x: Float,
        var y: Float,
        var speed: Float,
        val color: FloatArray,
        var pointSize: Float = 10f
    ) {
        fun updatePosition() {
            x += speed
            y += speed

            // Проверка границ и сброс позиции
            if (x > 1f || y > 1f || x < -1f || y < -1f) {
                resetStar()
            }
        }

        private fun resetStar() {
            x = Math.random().toFloat() * 2 - 1
            y = Math.random().toFloat() * 2 - 1
            speed = Math.random().toFloat() * 0.01f
            pointSize = Math.random().toFloat() * 5 + 2 // Случайный размер от 2 до 7
        }
    }
}