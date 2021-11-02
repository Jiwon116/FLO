package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.flo.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        // Imageview 둥글게 작업
        Glide.with(this).load(com.example.flo.R.drawable.album_video_exp_02)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.videoExp01)

        Glide.with(this).load(com.example.flo.R.drawable.album_video_exp)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.videoExp02)

        Glide.with(this).load(com.example.flo.R.drawable.album_video_exp_03)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.videoExp03)

        Glide.with(this).load(com.example.flo.R.drawable.album_video_exp_04)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(binding.videoExp04)


        return binding.root
    }
}