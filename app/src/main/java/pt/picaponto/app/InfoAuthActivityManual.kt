package pt.picaponto.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_info_auth.*
import pt.picaponto.app.Database.DatabaseHelper

class InfoAuthActivityManual : AppCompatActivity() {

    var databaseHelper: DatabaseHelper? = null
    var tipoEntrada: String = ""
    var data: String = ""
    var hora: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_auth_manual)

        getSupportActionBar()!!.hide()


        var sharedPreferences =
            getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)
        databaseHelper = DatabaseHelper(applicationContext)


        userLabel.setText(
            databaseHelper!!.getName(
                sharedPreferences.getString(
                    "user",
                    ""
                )!!.toInt()
            )
        )

        data = intent.getStringExtra("date")
        hora = intent.getStringExtra("time")

        labelDate.setText(data)
        hourLabel.setText(hora)


        val timer = object: CountDownTimer(3400, 500) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                val goMain = Intent(applicationContext, MainActivity::class.java)
                startActivity(goMain)

            }
        }

        timer.start()

    }
}
