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


class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding

    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(com.example.flo.R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        // Imageview 둥글게 작업
        Glide.with(this).load(com.example.flo.R.drawable.img_album_exp2)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.albumAlbumIv)

        /*// Toast 작업
        binding.albumSongLilacLayout01.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout02.setOnClickListener {
            Toast.makeText(activity, "Flu", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout03.setOnClickListener {
            Toast.makeText(activity, "Coin", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout04.setOnClickListener {
            Toast.makeText(activity, "봄 안녕 봄", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout05.setOnClickListener {
            Toast.makeText(activity, "Celebrity", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout06.setOnClickListener {
            Toast.makeText(activity, "돌림노래", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout07.setOnClickListener {
            Toast.makeText(activity, "빈 컵(Empty Cup)", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout08.setOnClickListener {
            Toast.makeText(activity, "아이와 나의 바다", Toast.LENGTH_SHORT).show()
        }

        binding.albumSongLilacLayout09.setOnClickListener {
            Toast.makeText(activity, "어푸(Ah Puh)", Toast.LENGTH_SHORT).show()
        }
        binding.albumSongLilacLayout10.setOnClickListener {
            Toast.makeText(activity, "에필로그", Toast.LENGTH_SHORT).show()
        }*/


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