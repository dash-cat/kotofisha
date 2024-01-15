package su.cus.announce

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoView = findViewById(R.id.videoView)

        val videoUri = Uri.parse("YOUR_VIDEO_URL_HERE")

        videoView.setVideoURI(videoUri)
        videoView.start() // Start playing the video
    }
}
