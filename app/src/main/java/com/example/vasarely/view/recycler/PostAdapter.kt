package com.example.vasarely.view.recycler

import android.content.res.Resources
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vasarely.databinding.PostImageBinding

class PostAdapter(private val postsBitmaps: List<Bitmap>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var onItemClick: ((Bitmap) -> Unit)? = null

    val width = Resources.getSystem().displayMetrics.widthPixels / 3 -
            (12 * Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = PostImageBinding.inflate(from, parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsBitmaps[position]
        holder.bindImage(post)
    }

    override fun getItemCount(): Int = postsBitmaps.size


    inner class PostViewHolder(private val postImageBinding: PostImageBinding) :
        RecyclerView.ViewHolder(postImageBinding.root) {

        fun bindImage(imageBitmap: Bitmap) {
            postImageBinding.postImageView.setImageBitmap(imageBitmap)
            postImageBinding.postImageView.layoutParams.height = width

            postImageBinding.postImageView.setOnClickListener {
                onItemClick?.invoke(imageBitmap)
            }
        }

    }
}