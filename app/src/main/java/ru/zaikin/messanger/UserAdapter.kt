package ru.zaikin.messanger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listener: OnUserClickListener? = null

    interface OnUserClickListener {
        fun onUserClick(position: Int)
    }

    fun setOnUserClickListener(listener: OnUserClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = users[position]
        holder.avatarImageView.setImageResource(currentUser.avatarMockUpResource)
        holder.userName.text = currentUser.name
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View, listener: OnUserClickListener?) :
        RecyclerView.ViewHolder(itemView) {

        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        val userName: TextView = itemView.findViewById(R.id.userNameTextView)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onUserClick(pos)
                }
            }
        }
    }
}
