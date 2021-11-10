package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemLockerBinding

class LockerAlbumRVAdapter(private val lockerAlbumList: ArrayList<LockerAlbum>) : RecyclerView.Adapter<LockerAlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(lockerAlbum : LockerAlbum)
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListener: LockerAlbumRVAdapter.MyItemClickListener

    fun setMyItemClickListener(itemClickListener: LockerAlbumRVAdapter.MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        lockerAlbumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LockerAlbumRVAdapter.ViewHolder {
        val binding: ItemLockerBinding = ItemLockerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(lockerAlbumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(lockerAlbumList[position]) }
        holder.binding.itemLockerAlbumMoreIv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount(): Int = lockerAlbumList.size

    inner class ViewHolder(val binding: ItemLockerBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(lockerAlbum: LockerAlbum) {
            binding.itemLockerAlbumTitleTv.text = lockerAlbum.title
            binding.itemLockerAlbumSingerTv.text = lockerAlbum.singer
            binding.itemLockerAlbumCoverImgIv.setImageResource(lockerAlbum.coverImg!!)
        }
    }
}