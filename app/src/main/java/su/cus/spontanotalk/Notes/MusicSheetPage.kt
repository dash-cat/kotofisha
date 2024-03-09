package su.cus.spontanotalk.Notes
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

class MusicSheetPageFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext().apply {
            setContent {
                MusicSheetPage()
            }
        })
    }
}

@Preview
@Composable
fun MusicSheetPage() {
    val notes = remember { mutableStateListOf<MusicNote>() }

    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val startY = 100f
        val endY = size.width - 100f
        for (i in 0..4) run {
            drawLine(
                color = Color.Black,
                start = Offset(startY, 100f + i * 20f),
                end = Offset(endY, 100f + i * 20f),
                strokeWidth = 2f
            )
        }

        notes.forEach { note ->
            drawCircle(
                color = Color.Black,
                center = Offset(note.xPosition, note.yPosition),
                radius = 10f
            )
        }
    }
    Button(onClick = {
        notes.add(MusicNote(
            xPosition = 150f,
            yPosition = 150f
        ))
    }) {
        Text("Add note")
    }
}

data class MusicNote (
    val xPosition: Float,
    val yPosition: Float
)
