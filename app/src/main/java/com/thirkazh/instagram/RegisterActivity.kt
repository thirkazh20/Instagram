package com.thirkazh.instagram

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_signin_link.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }

        btn_register.setOnClickListener {
            //method untuk membuat account
            createAccount()
        }
    }

    private fun createAccount() {
        val fullName = fullname_register.text.toString()
        val userName = username_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        when{
            TextUtils.isEmpty(fullName) -> Toast.makeText(this, "Full name is reuired",
                Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(this, "User name is reuired",
                Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "Email is reuired",
                Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "Password is reuired",
                Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Register")
                progressDialog.setMessage("Please Wait...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            seveUserInfo(fullName,userName,email,progressDialog)
                            //method baru untuk menyimpan data user setelah user signup
                        } else{
                            //setelah membuat pesan saat error selanjutnya create proggresDialog
                            val message = task.exception!!.toString()
                            Toast.makeText(this,"Error: $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }

    private fun seveUserInfo(fullName: String, userName: String, email: String,
        progressDialog: ProgressDialog) {

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap = HashMap<String,Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName.toLowerCase()
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "Hey Iam student at IDN Boarding School"
        //create default image profile
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/social-media-6dbee.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=4c663b2e-6712-4f96-8d68-f53bbc63b79e"

        usersRef.child(currentUserID).setValue((userMap))
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this,"Account sudah dibuat", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else{
                    val message = task.exception!!.toString()
                    Toast.makeText(this,"Error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}