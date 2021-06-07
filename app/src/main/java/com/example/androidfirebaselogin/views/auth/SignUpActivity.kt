package com.example.androidfirebaselogin.views.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import com.example.androidfirebaselogin.views.HomeActivity
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var userName: String
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
            userName = nameInputEditText.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateProfile()
                } else {
                    toast("Falha ao criar conta !")
                }
            }
        }
    }

    private fun updateProfile(){
        firebaseAuth.currentUser?.apply {
            var profileUpdates : UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(
                Uri.parse("https://picsum.photos/200")).build()
            updateProfile(profileUpdates)?.addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> apply {
                        Intent(this@SignUpActivity, HomeActivity::class.java).apply {
                            startActivity(this)
                            toast("Conta criada com sucesso!!!")
                            finish()
                        }
                    }
                    false -> toast("Falha na criação")
                }
            }
        }
    }
}