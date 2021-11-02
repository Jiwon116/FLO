package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BackgroundViewpagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment)  {

    //데이터를 외부에서 접근하지 못하게 하기 위해서 private를 사용함!
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    // add : 추가
    // size : 갯수
    override fun getItemCount(): Int = fragmentlist.size

    // fragment를 생성함
    // position - 0부터 시작해서 개수가 있는 -1만큼 실행함
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    // home fragment에 있는 list에 추가해주는 역할
    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1) // 배열은 0부터 숫자를 셈. index값을 넣어주기 때문에 -1을 함
    }
}