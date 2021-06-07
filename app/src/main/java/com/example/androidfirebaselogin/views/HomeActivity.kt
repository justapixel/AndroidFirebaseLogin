
package com.example.androidfirebaselogin.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import com.example.androidfirebaselogin.views.auth.SignInActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let{
            cardTitle.text = user.displayName.toString()
            cardSubTitle.text = user.email.toString()
            Glide.with(this).load(user.photoUrl.toString()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(cardImage)
        }
        homeLogoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            toast("Logout efetuado com sucesso!!")
            finish()
        }
        homeExitButton.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(0)
        }
    }
}
