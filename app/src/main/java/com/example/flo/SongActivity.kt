package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.flo.databinding.ActivitySongBinding

// class 상속 받을때는 :를 사용하고 소괄호를 꼭 넣어줘야함
class SongActivity : AppCompatActivity() {

    // lateinit : 전방선언(forward declaration). 나중에 변수 타입을 지정함
    // 일반적으로 변수 지정하는 방법
    // var test : Int = 2
    lateinit var binding : ActivitySongBinding

    private val song : Song = Song()
    private lateinit var player : Player
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflater - xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root) // root는 activity_song에 있는 최상단 view를 가르키고 있음

        initSong()

        player=Player(song.playTime, song.isPlaying)
        player.start() //Thread 시작

        binding.songDownIb.setOnClickListener{
            // method를 끄는 작업
            finish()
        }

        // like imageview
        binding.songLikeOffIv.setOnClickListener {
            setLikeStatus(true)
        }
        binding.songLikeOnIv.setOnClickListener {
            setLikeStatus(false)
        }

        // unlike imageview
        binding.songUnlikeOffIv.setOnClickListener {
            setUnlikeStatus(true)
        }
        binding.songUnlikeOnIv.setOnClickListener {
            setUnlikeStatus(false)
        }

        // play & pause imageview
        binding.songMiniplayerIv.setOnClickListener {
            player.isPlaying = true
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
        }

        // Imageview 둥글게 작업
        Glide.with(this).load(com.example.flo.R.drawable.img_album_exp2)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.songAlbumIv)

    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)

            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            setPlayerStatus(song.isPlaying)
        }
    }

    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        } else {
            binding.songPauseIv.visibility = View.GONE
            binding.songMiniplayerIv.visibility = View.VISIBLE
        }
    }

    fun setLikeStatus(isLike : Boolean) {
        if(isLike) {
            binding.songLikeOffIv.visibility = View.GONE
            binding.songLikeOnIv.visibility = View.VISIBLE
        } else {
            binding.songLikeOnIv.visibility = View.GONE
            binding.songLikeOffIv.visibility = View.VISIBLE
        }
    }

    fun setUnlikeStatus(isUnlike : Boolean) {
        if(isUnlike) {
            binding.songUnlikeOffIv.visibility = View.GONE
            binding.songUnlikeOnIv.visibility = View.VISIBLE
        } else {
            binding.songUnlikeOnIv.visibility = View.GONE
            binding.songUnlikeOffIv.visibility = View.VISIBLE
        }
    }

    inner class Player(private val playTime:Int,var isPlaying: Boolean) : Thread() {
        private var second = 0

        override fun run() {
            try{
                while(true) {
                    if (second >= playTime) {
                        break
                    }
                    if (isPlaying) {
                        sleep(1000)
                        second++
                        // handler 또는 runOnUiThread를 사용
                        handler.post {
                            binding.songPlayerSb.progress = second * 1000 / playTime
                            binding.songNowTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }
                }
            }catch(e: InterruptedException) {
                Log.d("intterrupt", "스레드가 종료되었습니다.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
    }
}