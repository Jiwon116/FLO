package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        // toggle imageview
        binding.albumBtnToggleOffIv.setOnClickListener {
            setToggleStatus(true)
        }
        binding.albumBtnToggleOnIv.setOnClickListener {
            setToggleStatus(false)
        }

        return binding.root
    }

    fun setToggleStatus(isToggle : Boolean) {
        if(isToggle) {
            binding.albumBtnToggleOffIv.visibility = View.GONE
            binding.albumBtnToggleOnIv.visibility = View.VISIBLE
        } else {
            binding.albumBtnToggleOffIv.visibility = View.VISIBLE
            binding.albumBtnToggleOnIv.visibility = View.GONE
        }
    }
}