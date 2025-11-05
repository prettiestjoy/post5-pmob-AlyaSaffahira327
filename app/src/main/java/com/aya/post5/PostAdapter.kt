package com.aya.post5

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.aya.post5.databinding.ItemPostBinding

class PostAdapter(
    private val context: Context,
    private val postList: MutableList<Post>,
    private val onEditClick: (Post, Int) -> Unit,
    private val onDeleteClick: (Post, Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        holder.binding.tvPostUsername.text = post.username
        holder.binding.tvPostCaption.text = post.caption
        holder.binding.ivPostProfile.setImageResource(post.profilePicResId)

        if (post.postImageUri != null) {
            holder.binding.ivPostImage.setImageURI(post.postImageUri)
        } else if (post.postImageResId != null) {
            holder.binding.ivPostImage.setImageResource(post.postImageResId!!)
        } else {
            holder.binding.ivPostImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.binding.ivPostMenu.setOnClickListener {
            val popup = PopupMenu(context, it)

            popup.menu.add("Edit Post")
            popup.menu.add("Hapus Post")

            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit Post" -> {
                        onEditClick(post, position)
                        true
                    }
                    "Hapus Post" -> {
                        onDeleteClick(post, position)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}