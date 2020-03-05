package pt.picaponto.app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_empresa.*

class Empresa : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_CODE = "codigo-empresa"
    val sharedPref: SharedPreferences = getSharedPreferences(PREF_CODE, PRIVATE_MODE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa)

        if(sharedPref.contains(PREF_CODE)){

            val intentGo = Intent(this, ColaboradorPinActivity::class.java)
            startActivity(intentGo)

        }

        submitEmpresa.setOnClickListener {



        }



    }
}
