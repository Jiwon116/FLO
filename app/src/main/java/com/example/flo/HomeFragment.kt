package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayMusicExp01Iv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
        }

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


}