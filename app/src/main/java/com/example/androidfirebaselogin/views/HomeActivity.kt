
package com.example.androidfirebaselogin.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils.firebaseAuth
import com.example.androidfirebaselogin.views.auth.SignInActivity
import com.example.androidfirebaselogin.views.services.RegisterService
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeLogoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            toast("Logout efetuado com sucesso!!")
            finish()
        }

        cardCarTires.setOnClickListener {
            val intent = Intent(this, RegisterService::class.java)
            intent.putExtra("serviceType", "Freios e Cambagem")
            startActivity(intent)
        }
        cardCarAlign.setOnClickListener {
            val intent = Intent(this, RegisterService::class.java)
            intent.putExtra("serviceType", "Alinhamento")
            startActivity(intent)
        }
        cardCarEngine.setOnClickListener {
            val intent = Intent(this, RegisterService::class.java)
            intent.putExtra("serviceType", "Motor e Eletrica")
            startActivity(intent)
        }
        cardCarSuspension.setOnClickListener {
            val intent = Intent(this, RegisterService::class.java)
            intent.putExtra("serviceType", "Eixo e Suspensão")
            startActivity(intent)
        }
    }
}
