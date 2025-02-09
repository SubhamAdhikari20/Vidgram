package com.example.vidgram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.model.MessageModel
import com.example.vidgram.R
import com.example.vidgram.model.Message

class MessageAdapter(
    var context: Context,
    val messageModelList: ArrayList<Message>,
    val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewHolder for Sent Messages
    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.sentMessageTextView)
        fun bind(messageModel: Message) {
            messageTextView.text = messageModel.message
        }
    }

    // ViewHolder for Received Messages
    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.receivedMessageTextView)
        fun bind(messageModel: Message) {
            messageTextView.text = messageModel.message
        }
    }

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageModelList[position]
        return if (message.senderId == currentUserId) {
            VIEW_TYPE_SENT
        }
        else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_send, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageModelList[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        }
        else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messageModelList.size

    fun updateData(newMessages: List<Message>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = messageModelList.size
            override fun getNewListSize() = newMessages.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return messageModelList[oldItemPosition].messageId == newMessages[newItemPosition].messageId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return messageModelList[oldItemPosition] == newMessages[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        messageModelList.clear()
        messageModelList.addAll(newMessages)
        diffResult.dispatchUpdatesTo(this)
    }

}
