package com.thirkazh.instagram

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri : Uri? = null
    private var storagePostPitcureRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        //create folder post Pitcure di database storage
        storagePostPitcureRef = FirebaseStorage.getInstance().reference.child("Post Picture")

        save_new_post_btn.setOnClickListener { uploadImage() } //create method upload image

        CropImage.activity()
            .setAspectRatio(2,1) //ukuran post
            .start(this@AddPostActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK
            && data!=null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_post.setImageURI(imageUri)
        }
    }

    private fun uploadImage() {

        when {
            imageUri == null -> Toast.makeText(this,"Please Select Image", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(deskripsi_post.text.toString()) -> Toast.makeText(this,"Please write the deskripsi",Toast.LENGTH_LONG).show()

            else ->{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Add New Post")
                progressDialog.setMessage("Please Wait, we are adding your picture...")
                progressDialog.show()

                val fileRef = storagePostPitcureRef!!.child(System.currentTimeMillis().toString())

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if (!task.isSuccessful) {

                        task.exception.let {
                            throw it!!
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener( OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                        val postId = ref.push().key

                        val postMap = HashMap<String, Any>()
                        //Sesuai dengan Firebas Database
                        postMap["postId"]       = postId!!
                        postMap["description"]  = deskripsi_post.text.toString().toLowerCase()
                        postMap["publisher"]    = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["postimage"]    = myUrl

                        ref.child(postId).updateChildren(postMap)

                        Toast.makeText(this,"Post Success...", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@AddPostActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }
                })
            }
        }
    }
}