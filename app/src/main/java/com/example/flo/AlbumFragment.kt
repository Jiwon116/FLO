package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // Home에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.id)

        // 넘어온 데이터를 반영
        setInit(album)

        setClickListeners(album)

        // 앨범 안에 있는 수록곡들 불러오기
        getSongs(album.id)

        // 뒤로 가기 버튼
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(com.example.flo.R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        // Tab layout
        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
        binding.albumAlbumIv.setImageResource(album.coverImg!!)

        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album) {
        val userId: Int = getUserIdx(requireContext())

        binding.albumLikeIv.setOnClickListener {
            if(isLiked) {
                disLikedAlbum(userId, album.id)
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
            } else {
                likeAlbum(userId, album.id)
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
            }
        }
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.AlbumDao().likeAlbum((like))
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx(requireContext())

        val likeId: Int? = songDB.AlbumDao().isLikeAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikedAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.AlbumDao().disLikeAlbum(userId, albumId)
    }

    /*private fun getUserIdx(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }*/

    private fun getSongs(albumIdx: Int): ArrayList<Song> {
        val songDB = SongDatabase.getInstance(requireContext())!!

        return songDB.SongDao().getSongsInAlbum(albumIdx) as ArrayList
    }
}