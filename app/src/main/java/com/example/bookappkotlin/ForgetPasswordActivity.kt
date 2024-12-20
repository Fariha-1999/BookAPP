package com.example.bookappkotlin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.bookappkotlin.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityForgetPasswordBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init / setup progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, go back
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //handle click,begin password recovery process
        binding.submitBtn.setOnClickListener {
            validateData()
        }

    }

    private var email = ""
    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()

        //validate
        if (email.isEmpty()){
            Toast.makeText(this, "Enter email... ", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email pattern... ", Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPassword()
        }

    }

    private fun recoverPassword() {
        //show progress
        progressDialog.setMessage("Sending password reset instruction to $email")
        progressDialog.show()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                //sent
                progressDialog.dismiss()
                Toast.makeText(this, "Instruction sent to \n$email ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                //failed
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to send due to ${e.message} ", Toast.LENGTH_SHORT).show()
            }
    }
}