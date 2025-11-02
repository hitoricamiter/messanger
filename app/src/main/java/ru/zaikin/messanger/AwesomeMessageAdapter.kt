package ru.zaikin.messanger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class AwesomeMessageAdapter(
    context: Context,
    private val resource: Int,
    private val messages: MutableList<AwesomeMessage>
) : ArrayAdapter<AwesomeMessage>(context, resource, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val photoImageView = view.findViewById<ImageView>(R.id.photoImageView)
        val textTextView = view.findViewById<TextView>(R.id.textTextView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)

        val message = getItem(position) ?: return view

        if (!message.text.isNullOrEmpty()) {
            textTextView.visibility = View.VISIBLE
            photoImageView.visibility = View.GONE
            textTextView.text = message.text
        }
        else if (!message.imageUrl.isNullOrEmpty()) {
            textTextView.visibility = View.GONE
            photoImageView.visibility = View.VISIBLE
            Glide.with(context)
                .load(message.imageUrl)
                .into(photoImageView)
        }
        else {
            textTextView.visibility = View.GONE
            photoImageView.visibility = View.GONE
        }

        nameTextView.text = message.name ?: "Unknown"

        return view
    }

    fun addMessage(message: AwesomeMessage) {
        messages.add(message)
        notifyDataSetChanged()
    }
}
