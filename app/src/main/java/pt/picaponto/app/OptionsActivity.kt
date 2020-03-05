package pt.picaponto.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pt.picaponto.app.Database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.backBtn
import kotlinx.android.synthetic.main.activity_main.picagensBtn
import kotlinx.android.synthetic.main.activity_options.*

class OptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        picagensBtn.setOnClickListener {
            val goPicagens = Intent(this, RecordsActivity::class.java)
            startActivity(goPicagens)
        }

        backBtn.setOnClickListener {
            val goBack = Intent(this, MainActivity::class.java)
            startActivity(goBack)
        }

        manualBtn.setOnClickListener {


            val databaseHelper = DatabaseHelper(this)
            var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)


            if(databaseHelper.checkColabManual(sharedPreferences.getString("user", "")!!.toInt())){
                val goManual = Intent(this, PicagemManualActivity::class.java)
                startActivity(goManual)
            } else {
                Toast.makeText(this, "A Picagem manual não está ativa.", Toast.LENGTH_LONG).show()
            }


        }

        vacationsBtn.setOnClickListener{
           val goVacations = Intent(this, VacationsActivity::class.java)
           startActivity(goVacations)
        }

        scheduleBtn.setOnClickListener {
            Toast.makeText(this, "O horário não está disponível.", Toast.LENGTH_LONG).show()
            //val goHorario = Intent(this, ScheduleActivity::class.java)
            //startActivity(goHorario)
        }


    }
}
