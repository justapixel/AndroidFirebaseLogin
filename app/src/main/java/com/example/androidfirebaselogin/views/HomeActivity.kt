
package com.example.androidfirebaselogin.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Glide.with(this).load("https://picsum.photos/200").into(cardImage)
        cardTitle.text = "AAA"
        homeLogoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignUpActivity::class.java))
            toast("Logout efetuado com sucesso!!")
            finish()
        }
        homeExitButton.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(0)
        }
    }
}
