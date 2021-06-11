package com.example.androidfirebaselogin.views.services

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidfirebaselogin.R
import com.example.androidfirebaselogin.extensions.Extensions.toast
import com.example.androidfirebaselogin.utils.FirebaseUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_service.*
import java.text.SimpleDateFormat
import java.util.*


// https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/

class RegisterService : AppCompatActivity() {
    lateinit var serviceType: String
    lateinit var selectedServiceType: String
    lateinit var descService: String
    lateinit var dateService: String
    lateinit var clientSelect: String
    private val calend: Calendar = Calendar.getInstance()
    lateinit var signUpInputArray: Array<EditText>
    private val store = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_service)
        signUpInputArray = arrayOf(serviceTypeInput, descInputEditText, datePickerInputEditText, clientInputEditText)
        datePickerInputEditText.setText("--/--/----")
        serviceType = intent.getStringExtra("serviceType")!!
        val items = listOf<String>("Eixo e Suspensão", "Alinhamento", "Motor e Eletrica", "Freios e Cambagem")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, items)
        serviceTypeInput.setAdapter(adapter)
        serviceTypeInput.setText(serviceType, false)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year, monthOfYear, dayofMonth ->
                calend.set(Calendar.YEAR, year)
                calend.set(Calendar.MONTH, monthOfYear)
                calend.set(Calendar.DAY_OF_MONTH, dayofMonth)
                updateDateInView()
            }

        datePickerInputEditText.setOnClickListener {
            DatePickerDialog(
                this@RegisterService,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calend.get(Calendar.YEAR),
                calend.get(Calendar.MONTH),
                calend.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        store.collection("users").whereEqualTo("type", "Cliente").get().addOnSuccessListener { documents ->
            val arrayClientList = ArrayList<String>()
            for(document in documents){
                arrayClientList.add(document.get("email") as String)
            }
            val clientAdapter = ArrayAdapter(this, R.layout.dropdown_item, arrayClientList)
            clientInputEditText.setAdapter(clientAdapter)
            clientInputEditText.threshold = 3
        }.addOnFailureListener {
            toast("Falha ao buscar clientes")
        }

        cancelButton.setOnClickListener{
            finish()
        }
        createServiceButton.setOnClickListener {
            createService()
        }
    }


    private fun updateDateInView(){
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        datePickerInputEditText.setText(sdf.format(calend.time))
    }

    private fun notEmpty(): Boolean = descInputEditText.text.toString().trim().isNotEmpty() &&
            datePickerInputEditText.text.toString().trim().isNotEmpty() &&
            clientInputEditText.text.toString().trim().isNotEmpty() &&
            serviceTypeInput.text.toString().trim().isNotEmpty()

    private fun createService (){
        if (notEmpty()){
            signUpInputArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "Não pode ser vazio"
                }
            }
        }

        selectedServiceType = serviceTypeInput.text.toString()
        descService = descInputEditText.text.toString()
        dateService = datePickerInputEditText.text.toString()
        clientSelect = clientInputEditText.text.toString()

        val service = hashMapOf<String, Any>(
            "id" to FirebaseUtils.firebaseAuth.uid.toString(),
            "type" to selectedServiceType,
            "description" to descService,
            "date" to dateService,
            "userId" to clientSelect,
            "status" to "Em andamento"
        )
        store.collection("services")
            .document()
            .set(service)
            .addOnCompleteListener { storeTask ->
                if(storeTask.isSuccessful){
                    finish()
                }
                else {
                    toast("Falha ao adicionar serviço!")
                }
            }
    }
}