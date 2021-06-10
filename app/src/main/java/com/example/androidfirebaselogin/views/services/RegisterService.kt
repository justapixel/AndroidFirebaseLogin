package com.example.androidfirebaselogin.views.services

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.androidfirebaselogin.R
import kotlinx.android.synthetic.main.activity_register_service.*

class RegisterService : AppCompatActivity() {
    lateinit var serviceType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_service)

        serviceType = intent.getStringExtra("serviceType")!!
        val items = listOf<String>("Eixo e Suspensão", "Alinhamento", "Motor e Eletrica", "Freios e Cambagem")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, items)
        serviceTypeInput.setAdapter(adapter)
        serviceTypeInput.setText(serviceType, false)
        cancelButton.setOnClickListener{
            finish()
        }
    }
}