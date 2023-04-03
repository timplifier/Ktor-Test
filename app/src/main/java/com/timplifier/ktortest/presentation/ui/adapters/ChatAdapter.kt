package com.timplifier.ktortest.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.timplifier.ktortest.domain.models.Message
import com.timplifier.ktortest.databinding.ItemMessageBinding

class ChatAdapter : PagingDataAdapter<Message, ChatAdapter.ChatViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder(
        ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class ChatViewHolder(private val binding: ItemMessageBinding) : ViewHolder(binding.root) {
        fun onBind(message: Message) {
            binding.tvMessage.text = message.message
        }
    }

    companion object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
    }
}