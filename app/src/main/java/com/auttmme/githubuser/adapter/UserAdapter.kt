package com.auttmme.githubuser.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.auttmme.githubuser.model.User
import com.auttmme.githubuser.databinding.ItemUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter() : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    var mData = ArrayList<User>()
        set(mData) {
            this.mData.clear()
            this.mData.addAll(mData)
            notifyDataSetChanged()
        }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(user: User) {
        this.mData.add(user)
        notifyItemInserted(this.mData.size - 1)
    }

    fun removeItem(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding){
                Glide.with(itemView.context)
                    .load(user.photo)
                    .apply(RequestOptions().override(85,85))
                    .into(imgPhoto)

                txtUsername.text = user.username

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
                //
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}