package com.aya.post5

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aya.post5.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storyList = mutableListOf<Story>()
    private val postList = mutableListOf<Post>()
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter
    private var selectedImageUri: Uri? = null
    private var dialogImagePreview: ImageView? = null
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            dialogImagePreview?.setImageURI(it)
            dialogImagePreview?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        loadDummyData()

        binding.fabAddPost.setOnClickListener {
            showAddEditDialog(null, -1)
        }
    }

    private fun setupAdapters() {
        storyAdapter = StoryAdapter(storyList)
        binding.rvStories.adapter = storyAdapter
        postAdapter = PostAdapter(
            this,
            postList,
            onEditClick = { post, position ->
                showAddEditDialog(post, position)
            },
            onDeleteClick = { post, position ->
                showDeleteDialog(post, position)
            }
        )
        binding.rvPosts.adapter = postAdapter
    }
    private fun loadDummyData() {
        storyList.add(Story("katarinabluu", R.drawable.karina))
        storyList.add(Story("for_everyoung10", R.drawable.wony1))
        storyList.add(Story("secretlifeofwony", R.drawable.wony2))
        storyList.add(Story("wony_official", R.drawable.wony3))
        storyList.add(Story("enamiasa", R.drawable.asa))
        storyList.add(Story("enami.desu", R.drawable.asa1))
        storyList.add(Story("ace_a", R.drawable.asa2))

        storyAdapter.notifyDataSetChanged()
        postList.add(Post(
            id = 1L,
            username = "katarinabluu",
            caption = "ke pantai dulu ceunah 🏖️",
            postImageResId = R.drawable.katarinabluu,
            profilePicResId = R.drawable.karina
        ))
        postList.add(Post(
            id = 2L,
            username = "enamiasa",
            caption = "えなみです",
            postImageResId = R.drawable.enamiasa,
            profilePicResId = R.drawable.asa1
        ))
        postList.add(Post(
            id = 3L,
            username = "for_everyoung10",
            caption = "장원영",
            postImageResId = R.drawable.wony,
            profilePicResId = R.drawable.wony1
        ))
        postList.add(Post(
            id = 4L,
            username = "jennierubyjane",
            caption = "ruby jane",
            postImageResId = R.drawable.jennie,
            profilePicResId = R.drawable.jennie1
        ))

        postAdapter.notifyDataSetChanged()
    }

    private fun showAddEditDialog(post: Post?, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_post, null)
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etCaption = dialogView.findViewById<EditText>(R.id.etCaption)
        val btnSelectImage = dialogView.findViewById<Button>(R.id.btnSelectImage)
        dialogImagePreview = dialogView.findViewById(R.id.ivImagePreview)

        selectedImageUri = null
        dialogImagePreview?.visibility = View.GONE

        if (post != null) {
            tvDialogTitle.text = "Edit Post"
            etUsername.setText(post.username)
            etCaption.setText(post.caption)

            if (post.postImageUri != null) {
                selectedImageUri = post.postImageUri
                dialogImagePreview?.setImageURI(selectedImageUri)
                dialogImagePreview?.visibility = View.VISIBLE
            } else if (post.postImageResId != null) {
                dialogImagePreview?.setImageResource(post.postImageResId!!)
                dialogImagePreview?.visibility = View.VISIBLE
            }

        } else {
            tvDialogTitle.text = "Tambah Post Baru"
        }

        btnSelectImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .show()

        val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val caption = etCaption.text.toString().trim()

            val isImageMissing: Boolean
            if (post == null) {
                isImageMissing = (selectedImageUri == null)
            } else {
                isImageMissing = (selectedImageUri == null && post.postImageUri == null && post.postImageResId == null)
            }

            if (username.isEmpty() || caption.isEmpty() || isImageMissing) {
                Toast.makeText(this, "Isi semua kolom dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (post == null) {
                val newPost = Post(
                    id = Date().time,
                    username = username,
                    caption = caption,
                    postImageUri = selectedImageUri,
                    postImageResId = null,
                    profilePicResId = R.drawable.karinul
                )

                postList.add(0, newPost)
                postAdapter.notifyItemInserted(0)
                binding.rvPosts.scrollToPosition(0)

                Toast.makeText(this, "Post berhasil ditambahkan", Toast.LENGTH_SHORT).show()

            } else {
                post.username = username
                post.caption = caption

                if (selectedImageUri != null) {
                    post.postImageUri = selectedImageUri
                    post.postImageResId = null
                }

                postAdapter.notifyItemChanged(position)

                Toast.makeText(this, "Post diperbarui", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }
    }
    private fun showDeleteDialog(post: Post, position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus Post")
            .setMessage("Apakah kamu yakin ingin menghapus post dari '${post.username}'?")
            .setPositiveButton("Hapus") { dialog, _ ->
                postList.removeAt(position)
                postAdapter.notifyItemRemoved(position)

                Toast.makeText(this, "Hapus ${post.username}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}