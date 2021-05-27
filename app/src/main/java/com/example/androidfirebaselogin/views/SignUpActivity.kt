package com.example.androidfirebaselogin.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.emailInputEditText
import kotlinx.android.synthetic.main.activity_signup.passwordInputEditText

class SignUpActivity : AppCompatActivity() {
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var signUpInputArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signUpInputArray = arrayOf(nameInputEditText, emailInputEditText, passwordInputEditText, confirmPasswordInputEditText)
        signUpButton.setOnClickListener {
            signUp()
        }

        signInButton.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
            toast("Por-Favor faça o login na sua conta")
            finish()
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

    private fun notEmpty(): Boolean = emailInputEditText.text.toString().trim().isNotEmpty() &&
            nameInputEditText.text.toString().trim().isNotEmpty() &&
            passwordInputEditText.text.toString().trim().isNotEmpty() && confirmPasswordInputEditText.text.toString().trim().isNotEmpty()

    private fun isEqualPassword(): Boolean {
        var isEqual = false
        if (notEmpty() && passwordInputEditText.text.toString().trim() == confirmPasswordInputEditText.text.toString().trim()
        ) {
            isEqual = true
        } else if (!notEmpty()){
            signUpInputArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint}"
                }
            }
        } else {
            toast("Senhas não conferem!")
        }
        return isEqual
    }

    private fun signUp(){
        if (isEqualPassword()){
            userEmail = emailInputEditText.text.toString().trim()
            userPassword = passwordInputEditText.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Conta criada com sucesso !")
                    sendEmailVerification()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    toast("Falha ao criar conta !")
                }
            }
        }
    }

    private fun sendEmailVerification(){
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email enviado para $userEmail")
                }
            }
        }
    }
}