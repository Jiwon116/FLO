package com.example.flo

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var gson: Gson = Gson()
    lateinit var song: Song
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        // 생성할 때 초기화 되는 변수
        song = Song("라일락", "아이유", 0, 215, false, "music_lilac")
        setMiniPlayer(song)

        //Log.d("Log test", binding.mainMiniplayerTitleTv.text.toString() + binding.mainMiniplayerSingerTv.text.toString())

        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            startActivity(intent)
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

        // play & pause imageview
        binding.mainMiniplayerBtn.setOnClickListener {
            binding.mainPauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        }
        binding.mainPauseBtn.setOnClickListener {
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isPlaying) {
            binding.mainPauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        } else {
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song", null) // 아무것도 없을 경우 null로 해줌, json 객체로 받아온거임
        song = if (jsonSong == null) { // sharedPreferences에 저장된 값이 없다면
            Song("라일락", "아이유(IU)", 0, 215, false, "music_lilac")
        } else {
            gson.fromJson(jsonSong, Song::class.java) // 가지고 있던 song에 초기화 해줌
        }
        setMiniPlayer(song)
    }
}

