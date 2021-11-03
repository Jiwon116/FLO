package com.example.flo

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import com.google.gson.Gson
import java.util.*

// class 상속 받을때는 :를 사용하고 소괄호를 꼭 넣어줘야함
class SongActivity : AppCompatActivity() {

    // lateinit : 전방선언(forward declaration). 나중에 변수 타입을 지정함
    // 일반적으로 변수 지정하는 방법
    // var test : Int = 2
    lateinit var binding : ActivitySongBinding
    lateinit var player : Player

    private val song : Song = Song()
    private var mediaPlayer: MediaPlayer? = null
    //private val handler = Handler(Looper.getMainLooper())
    private var gson: Gson = Gson() // gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflater - xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root) // root는 activity_song에 있는 최상단 view를 가르키고 있음

        initSong()

        player = Player(song.playTime, song.isPlaying)
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
            song.isPlaying= true
            mediaPlayer?.start()
        }
        binding.songPauseIv.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
            song.isPlaying = false
            mediaPlayer?.pause()
        }

        // Imageview 둥글게 작업
        Glide.with(this).load(com.example.flo.R.drawable.img_album_exp2)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.songAlbumIv)

    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second")
            && intent.hasExtra("playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("music")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.second = intent.getIntExtra("second", 0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.music = intent.getStringExtra("music")!!
            val music = resources.getIdentifier(song.music, "raw", this.packageName)

            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            setPlayerStatus(song.isPlaying)
            mediaPlayer = MediaPlayer.create(this, music)
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

    inner class Player(private val playTime:Int = 0, var isPlaying: Boolean = false) : Thread() {
        private var second = 0

        @SuppressLint("SetTextI18n")
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
                        runOnUiThread {
                            binding.songPlayerSb.progress = (second * 1000 / playTime)
                            binding.songNowTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }
                }
            }catch(e: InterruptedException) {
                Log.d("intterrupt", "스레드가 종료되었습니다.")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause() //미디어 플레이어 중지
        player.isPlaying = false // 스레드 중지
        song.isPlaying = false
        song.second = (binding.songPlayerSb.progress * song.playTime) / 1000
        setPlayerStatus(false) // 재생과 일시정지 이미지가 바뀌는 함수

        // 데이터를 내부 저장소 어딘가에 저장해줌
        // 간단한 설정값들을 저장할 때 도움이 됨
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // sharedPreferences를 조작할 때 사용함
        // json -> 자바 객체를 다른 곳으로 전송할 때 gson 포맷으로 보내게 됨
        // 자바 뿐만 아니라 다른 프로그램 환경으로 보낼 때도 사용함
        // json은 text 포맷으로 되어 있음  ex) 'title':"lilac" ...
        // gson은 객체를 json으로 바꿔주고, json을 객체로 바꿔주는 중간다리 역할임
        val json = gson.toJson(song) // song 데이터 객체를 json으로 변환해줌
        editor.putString("song", json)

        editor.apply() // 적용시키기
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt() // 스레드 해제
        // 필요한 리소스들을 해제해줌
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스를 해제함
        mediaPlayer = null // 미디어 플레이어 해제
    }
}