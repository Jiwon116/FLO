package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.bumptech.glide.*

import android.R
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // Home에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        // 넘어온 데이터를 반영
        setInit(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(com.example.flo.R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        // Imageview 둥글게 작업
        Glide.with(this).load(com.example.flo.R.drawable.img_album_exp2)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.albumAlbumIv)

        // Tab layout
        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
                tab, position ->
            tab.text = information[position]
        }.attach()


        // toggle imageview
        binding.albumLikeIv.setOnClickListener {
            setToggleStatus(true)
        }
        binding.albumUnlikeIv.setOnClickListener {
            setToggleStatus(false)
        }

        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
    }

    fun setToggleStatus(isToggle : Boolean) {
        if(isToggle) {
            binding.albumLikeIv.visibility = View.GONE
            binding.albumUnlikeIv.visibility = View.VISIBLE
        } else {
            binding.albumLikeIv.visibility = View.VISIBLE
            binding.albumUnlikeIv.visibility = View.GONE
        }
    }
}