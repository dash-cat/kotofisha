package su.cus.spontanotalk

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class HomeFragment : Fragment() {
    private lateinit var glSurfaceView: GLSurfaceView
    private var rendererSet = false
    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val composeView = ComposeView(requireContext()).apply {
           setContent { GLSurfaceComposeLayout() }
       }

        return composeView
    }
    @Preview
    @Composable
    fun GLSurfaceComposeLayout() {
        // Context для создания GLSurfaceView
        val context = LocalContext.current

        // Создание GLSurfaceView
        val glSurfaceView = remember {
            GLSurfaceView(context).apply {
                glSurfaceView.setEGLContextClientVersion(2) // Use OpenGL ES 2.0
                glSurfaceView.setRenderer(HomeRender())
                rendererSet = true
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // GLSurfaceView в качестве фонового слоя
            AndroidView(factory = { glSurfaceView }, modifier = Modifier.matchParentSize())

            // Поверх GLSurfaceView можно размещать другие Compose компоненты
            AnimatedContent()
        }
    }

    @Preview
    @Composable
    fun AnimatedContent() {
        // Анимация перемещения
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val position by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 300f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = { /* Обработка нажатия */ },
                modifier = Modifier
                    .padding(16.dp)
                    .graphicsLayer {
                        translationY = position
                    }
            ) {
                Text("Кнопка с Анимацией")
            }
        }
    }
    private fun initViews() {

    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            glSurfaceView.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            glSurfaceView.onResume()
        }
    }
}