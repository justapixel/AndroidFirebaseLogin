package com.example.androidfirebaselogin.views.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import com.example.androidfirebaselogin.views.HomeActivity
import com.google.android.material.chip.Chip
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
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

        signUpBackButton.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
            toast("Faça o seu login")
            finish()
        }
        var lastCheckedId = View.NO_ID
        accountTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == View.NO_ID) {
                // User tried to uncheck, make sure to keep the chip checked
                group.check(lastCheckedId)
                return@setOnCheckedChangeListener
            }
            lastCheckedId = checkedId
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
                    input.error = "Não pode ser vazio"
                }
            }
        } else {
            toast("Senhas não conferem!")
            confirmPasswordInputEditText.error = "Senha não confere!"
        }
        return isEqual
    }

    private fun signUp(){
        val accountTypeValue = accountTypeChipGroup.findViewById<Chip>(accountTypeChipGroup.checkedChipId).text.toString()
        if (isEqualPassword()){
            userEmail = emailInputEditText.text.toString().trim()
            userPassword = passwordInputEditText.text.toString().trim()
            userName = nameInputEditText.text.toString().trim()
            val store = FirebaseFirestore.getInstance()
            store.collection("users").whereEqualTo("email", userEmail).get().addOnCompleteListener {
                if (it.isSuccessful){
                    val isEmpty = it.result?.isEmpty
                    if (isEmpty!!){
                        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = hashMapOf<String, Any>(
                                    "id" to firebaseAuth.uid.toString(),
                                    "name" to userName,
                                    "email" to userEmail,
                                    "type" to accountTypeValue
                                )
                                store.collection("users")
                                    .document(firebaseAuth.uid.toString())
                                    .set(user)
                                    .addOnCompleteListener { storeTask ->
                                        if(storeTask.isSuccessful){
                                            updateProfile()
                                            finish()
                                        }
                                        else {
                                            toast("Falha ao criar conta!")
                                        }
                                    }
                            } else {
                                toast("Falha ao criar conta !")
                            }
                        }
                    }
                    else {
                        toast("Usuario já existe")
                        emailInputEditText.error = "Usuario já existe!"
                    }
                }
            }
        }
    }

    private fun updateProfile(){
        firebaseAuth.currentUser?.apply {
            val profileUpdates : UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(
                Uri.parse("https://picsum.photos/200")).build()
            updateProfile(profileUpdates).addOnCompleteListener {
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