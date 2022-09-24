package com.hibisco.kitsune

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
    }

     override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        // if(currentUser != null){  reload();  }
    }

    fun cadastro(view: View) {
        val etEmail: EditText = findViewById(R.id.tv_email)
        val etPassWOrd: EditText = findViewById(R.id.tv_password)
        val email = etEmail.text.toString()
        val password = etPassWOrd.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(baseContext, "Usuario criado ${user.email.toString()}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed. $",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun login (View: View) {
        val etEmail: EditText = findViewById(R.id.tv_email)
        val etPassWOrd: EditText = findViewById(R.id.tv_password)
        val email = etEmail.text.toString()
        val password = etPassWOrd.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("", "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(
                            baseContext,
                            "Usuario logado ${user.email.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}