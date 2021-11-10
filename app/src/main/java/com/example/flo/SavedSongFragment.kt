package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedSongBinding
import com.google.gson.Gson

class SavedSongFragment : Fragment() {
    lateinit var binding : FragmentSavedSongBinding

    private var lockeralbumDatas = ArrayList<LockerAlbum>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        // 데이터 리스트 생성 (더미 데이터)
        lockeralbumDatas.apply {
            add(LockerAlbum("Butter", "방탄소년단 (BTS)", R.drawable.img_album_butter))
            add(LockerAlbum("LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
            add(LockerAlbum("Next Level", "에스파 (AESPA)", R.drawable.img_album_nextlevel))
            add(LockerAlbum("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_boywithluv))
            add(LockerAlbum("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_bboombboom))
            add(LockerAlbum("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_weekend))
            add(LockerAlbum("strawberry moon", "아이유 (IU)", R.drawable.img_album_strawberrymoon))
            add(LockerAlbum("나비효과", "볼빨간 사춘기", R.drawable.img_album_butterflyeffect))
            add(LockerAlbum("Savage", "에스파 (AESPA)", R.drawable.img_album_savage))
            add(LockerAlbum("낮 밤", "이영지", R.drawable.img_album_daynight))
            add(LockerAlbum("Cold Blooded", "제시 (Jessi)", R.drawable.img_album_coldblooded))
            add(LockerAlbum("XOXO", "전소미", R.drawable.img_album_xoxo))
        }


        // 더미데이터와 어댑터 연결
        val lockerAlbumRVAdapter = LockerAlbumRVAdapter(lockeralbumDatas)

        // 리사이클러뷰와 어댑터를 연결
        binding.lockerAlbumListRv.adapter = lockerAlbumRVAdapter
        lockerAlbumRVAdapter.setMyItemClickListener(object : LockerAlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(lockerAlbum: LockerAlbum) {
                changeAlbumFragment(lockerAlbum)
            }

            override fun onRemoveAlbum(position: Int) {
                lockerAlbumRVAdapter.removeItem(position)
            }
        })

        // 레이아웃 매니저 설정
        binding.lockerAlbumListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        return binding.root
    }

    private fun changeAlbumFragment(lockerAlbum: LockerAlbum) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(lockerAlbum)
                    putString("lockeralbum", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }
}