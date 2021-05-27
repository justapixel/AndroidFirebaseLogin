package com.example.androidfirebaselogin.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*
import kotlin.system.exitProcess

class SignInActivity : AppCompatActivity() {
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var signInInputArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        signInInputArray = arrayOf(emailInputEditText, passwordInputEditText)

        signUpButton.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        signInButton.setOnClickListener{
            signInUser()
        }

        exitButton.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(0)
        }
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, HomeActivity::class.java))
            toast("Bem-Vindo!!!")
        }
    }

    private fun notEmpty(): Boolean = userEmail.isNotEmpty() && userPassword.isNotEmpty()

    private fun signInUser(){
        userEmail = emailInputEditText.text.toString().trim()
        userPassword = passwordInputEditText.text.toString().trim()

        if(notEmpty()){
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { signIn ->
                if(signIn.isSuccessful){
                    startActivity(Intent(this, HomeActivity::class.java))
                    toast("Login efetuado com sucesso!")
                    finish()
                }else {
                    toast("Falha ao fazer login")
                }
            }
        }else {
            signInInputArray.forEach { input ->
                if(input.text.toString().trim().isEmpty()){
                    input.error = "${input.hint}"
                }
            }
        }
    }
}