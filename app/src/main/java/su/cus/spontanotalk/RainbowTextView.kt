package su.cus.spontanotalk
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RainbowTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var linearGradient: LinearGradient? = null
    private val gradientMatrix: Matrix = Matrix()
    private var viewWidth = 0
    private var colors: IntArray = intArrayOf(
        Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED
    )
    private var colorIndex = 0
    private val transitionDuration = 2000 // Duration for each color transition in milliseconds

    init {
        init()
    }

    private fun init() {
        // Initialize the paint property
        paint.color = Color.BLACK // You can set the initial color as needed
        startColorTransition()
    }

    private fun startColorTransition() {
        val colorAnimator = ValueAnimator.ofInt(0, 1)
        colorAnimator.duration = transitionDuration.toLong()
        colorAnimator.addUpdateListener { animator ->
            val fraction = animator.animatedFraction
            val nextColorIndex = (colorIndex + 1) % colors.size
            val startColor = colors[colorIndex]
            val endColor = colors[nextColorIndex]
            val interpolatedColor = evaluateColor(fraction, startColor, endColor)
            paint.color = interpolatedColor
            invalidate()
            if (fraction == 1f) {
                colorIndex = nextColorIndex
                startColorTransition()
            }
        }
        colorAnimator.start()
    }

    private fun evaluateColor(fraction: Float, startColor: Int, endColor: Int): Int {
        val startA = Color.alpha(startColor)
        val startR = Color.red(startColor)
        val startG = Color.green(startColor)
        val startB = Color.blue(startColor)

        val endA = Color.alpha(endColor)
        val endR = Color.red(endColor)
        val endG = Color.green(endColor)
        val endB = Color.blue(endColor)

        val alpha = (startA + (fraction * (endA - startA))).toInt()
        val red = (startR + (fraction * (endR - startR))).toInt()
        val green = (startG + (fraction * (endG - startG))).toInt()
        val blue = (startB + (fraction * (endB - startB))).toInt()

        return Color.argb(alpha, red, green, blue)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (viewWidth == 0) {
            viewWidth = measuredWidth
            if (viewWidth > 0) {
                linearGradient = LinearGradient(
                    0f, 0f, viewWidth.toFloat(), 0f,
                    colors, null, Shader.TileMode.MIRROR
                )
                paint.shader = linearGradient
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (paint.shader == null) {
            paint.shader = linearGradient
        }
        gradientMatrix.setTranslate(10f, 0f)
        linearGradient?.setLocalMatrix(gradientMatrix)
    }
}