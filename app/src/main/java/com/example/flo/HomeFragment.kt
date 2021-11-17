package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albums = ArrayList<Album>();

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        // songDB에서 album list를 가져옴
        albums.addAll(songDB.AlbumDao().getAlbums())

        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 더미데이터와 어댑터 연결
        val albumRVAdapter = AlbumRVAdapter(albums)

        // 리사이클러뷰와 어댑터를 연결
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp)) // 프래그먼트가 들어가야함
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))


        binding.homeViewpagerExpVp.adapter = bannerAdapter
        binding.homeViewpagerExpVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val backgroundAdapter = BackgroundViewpagerAdapter(this)
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_default_4_x_1))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.home_background_exp2))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.home_background_exp3))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_default_4_x_1))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.home_background_exp2))


        binding.homeBackgroundVp.adapter = backgroundAdapter
        binding.homeBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }


}