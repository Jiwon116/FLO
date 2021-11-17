package com.example.flo

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.flo.databinding.ActivitySongBinding
import java.util.concurrent.TimeUnit

// class 상속 받을때는 :를 사용하고 소괄호를 꼭 넣어줘야함
class SongActivity : AppCompatActivity() {

    // lateinit : 전방선언(forward declaration). 나중에 변수 타입을 지정함
    // 일반적으로 변수 지정하는 방법
    // var test : Int = 2
    lateinit var binding : ActivitySongBinding

    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer : Timer

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflater - xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root) // root는 activity_song에 있는 최상단 view를 가르키고 있음

        initPlayList()
        initSong()
        initClickLister()
        seekBarListener()
    }

    override fun onPause() {
        super.onPause()

        songs[nowPos].second = (songs[nowPos].playTime * binding.songPlayerSb.progress) / 1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    // 앱이 종료될 때 리소스 해제
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt() // 스레드 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스를 해제함
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun initSong() {

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songNowTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songPlayerSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        mediaPlayer = MediaPlayer.create(this, music)
    }

    private fun initClickLister() {
        binding.songDownIb.setOnClickListener{
            // method를 끄는 작업
            finish()
        }

        // play & pause imageview
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        // 왼쪽 노래
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        // 오른쪽 노래
        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        // 좋아요
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "First song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "Last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct
        // 스레드 중지 및 다시 시작
        timer.interrupt()
        startTimer()
        // 리소스 해제 및 미디어플레이어 해제
        mediaPlayer?.release()
        mediaPlayer = null
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.SongDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
    }

    fun setPlayerStatus(isPlaying : Boolean) {
        timer.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE

            mediaPlayer?.start()
        } else {
            binding.songPauseIv.visibility = View.GONE
            binding.songMiniplayerIv.visibility = View.VISIBLE

            mediaPlayer?.pause()
        }
    }

    private fun seekBarListener() {
        binding.songPlayerSb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                songs[nowPos].second = progress * songs[nowPos].playTime
                binding.songNowTimeTv.text = String.format("%02d:%02d", (songs[nowPos].second / 1000) / 60, (songs[nowPos].second / 1000) % 60)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mediaPlayer?.seekTo(songs[nowPos].second)
                binding.songPlayerSb.progress = songs[nowPos].second / songs[nowPos].playTime
            }
        })
    }

    inner class Timer(private val playTime:Int = 0, var isPlaying: Boolean = false) : Thread() {
        private var second: Long = 0

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
                            binding.songPlayerSb.progress = (second * 1000 / playTime).toInt()
                            binding.songNowTimeTv.text = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(second), second % 60)
                        }
                    }
                }
            }catch(e: InterruptedException) {
                Log.d("intterrupt", "스레드가 종료되었습니다. ${e.message}")
            }
        }
    }
}