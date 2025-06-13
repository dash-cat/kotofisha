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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

class HomeFragment : Fragment() {
    // Removed lateinit var glSurfaceView and rendererSet

    // Companion object can be removed if newInstance() is not used elsewhere.
    // For now, let's assume it might be, or remove it if confirmed unused later.
    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels() // ViewModel is currently empty

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       return ComposeView(requireContext()).apply {
           setContent { GLSurfaceComposeLayoutWithLifecycle() } // Changed to new Composable
       }
    }

    @Preview
    @Composable
    fun GLSurfaceComposeLayoutWithLifecycle() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        // Create and remember GLSurfaceView
        val glSurfaceViewInstance = remember {
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(2) // Use OpenGL ES 2.0
                setRenderer(HomeRender()) // Assuming HomeRender is a valid Renderer
                // renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY // Optional: manage render mode
            }
        }

        // Manage GLSurfaceView lifecycle using DisposableEffect
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> glSurfaceViewInstance.onResume()
                    Lifecycle.Event.ON_PAUSE -> glSurfaceViewInstance.onPause()
                    else -> {} // Do nothing for other events
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                // glSurfaceViewInstance.queueEvent { glSurfaceViewInstance.destroyDrawingCache() } // Example cleanup
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { glSurfaceViewInstance }, modifier = Modifier.matchParentSize())
            AnimatedContent()
        }
    }

    @Preview
    @Composable
    fun AnimatedContent() {
        val infiniteTransition = rememberInfiniteTransition(label = "animTransition") // Added label
        val position by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f, // Reduced target value for potentially smoother preview
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing), // Increased duration
                repeatMode = RepeatMode.Reverse
            ), label = "position" // Added label
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = { /* TODO: Handle click */ },
                modifier = Modifier
                    .padding(16.dp)
                    .graphicsLayer {
                        translationY = position
                    }
            ) {
                Text("Кнопка с Анимацией") // Animation Button
            }
        }
    }

    // Removed initViews() as it was empty.
    // Removed Fragment's onPause/onResume as lifecycle is handled by DisposableEffect for the GLSurfaceView.
}