package com.yhkim.myyoutube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.yhkim.myyoutube.databinding.ActivityYoutubeDetailBinding

class YouTubeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYoutubeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("video_url")
        val mediaController = MediaController(this@YouTubeDetailActivity) // 영상 재생 컨트롤러

        binding.videoView.setVideoPath(url)

        binding.videoView.requestFocus() // 화면이 포커스를 가져감.
        binding.videoView.start() // 영상 재생시키기
        mediaController.show() // 영상 보여준다.

        // 영상 중지 => binding.videoView.stop()

        // 좀 더 전문적인 영상 재생 하려면 -> 구글의 Exoplayer(https://developer.android.com/guide/topics/media/exoplayer?hl=ko)
        // DRM -> Digital rights management

    }
}