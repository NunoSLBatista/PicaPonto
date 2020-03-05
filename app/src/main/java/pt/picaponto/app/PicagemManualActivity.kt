package pt.picaponto.app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pt.picaponto.app.Database.DatabaseHelper
import pt.picaponto.app.Models.Registo
import kotlinx.android.synthetic.main.activity_picagem_manual.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class PicagemManualActivity : AppCompatActivity() {


    val months = arrayOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")
    private var mYear: Int = 0;
    private var mMonth:Int = 0;
    private var mDay:Int = 0;
    private var mHour:Int = 0;
    private var mMinute:Int = 0;
    var databaseHelper: DatabaseHelper? = null

    var date: String = ""
    var time: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picagem_manual)

        lblDate.setOnClickListener {

            showDatePicker()

        }


        picagensBtn.setOnClickListener {

            if (date != "" && time != "") {

                var sharedPreferences =
                    getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)
                var colabID = sharedPreferences.getInt("userID", 0)

                var registo = Registo(colabID, "Entrada", date, time)
                registo.codigo = sharedPreferences.getString("user", "")!!

                val infoAuthActivity = InfoAuthActivity()
                val empresaActivity: EmpresaActivity =
                    EmpresaActivity()

                if (infoAuthActivity.verifyAvailableNetwork(this)) {
                    registo.synch = 1
                    empresaActivity.sendPicagem(registo)
                } else {
                    registo.synch = 0
                }

                databaseHelper = DatabaseHelper(applicationContext)
                databaseHelper!!.addRegisto(registo)

                val intent = Intent(this, InfoAuthActivityManual::class.java)
                intent.putExtra("date", date)
                intent.putExtra("time", time)
                startActivity(intent)


            } else {
                Toast.makeText(this, "Por favor escolha uma data", Toast.LENGTH_LONG).show()
            }
        }


        scheduleBtn.setOnClickListener {
            var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)
            var colabID = sharedPreferences.getString("user", "")!!.toInt()

            var registo = Registo(colabID, "Entrada", date, time)
            registo.codigo = sharedPreferences.getString("user", "")!!

            val infoAuthActivity = InfoAuthActivity()
            val empresaActivity: EmpresaActivity =
                EmpresaActivity()

            if(infoAuthActivity.verifyAvailableNetwork(this)){
                registo.synch = 1
                empresaActivity.sendPicagem(registo)
            } else {
                registo.synch = 0
            }

            databaseHelper = DatabaseHelper(applicationContext)
            databaseHelper!!.addRegisto(registo)

            val intent = Intent(this, InfoAuthActivityManual::class.java)
            intent.putExtra("date", date)
            intent.putExtra("time", time)
            startActivity(intent)

        }


        vacationsBtn.setOnClickListener {
            val goOptions = Intent(this, OptionsActivity::class.java)
            startActivity(goOptions)
        }

        backBtn.setOnClickListener {
            val leave = Intent(this, PinCodeActivity::class.java)
            startActivity(leave)
        }

        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)

        showDatePicker()

    }

    private fun showDatePicker(){
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            lblDate.setText("" + dayOfMonth + ", " + months[monthOfYear] + ", " + year)
            date = year.toString() + "/" + String.format("%02d", monthOfYear + 1) + "/" + String.format("%02d", dayOfMonth)
            showTimePicker()
        }, mYear, mMonth, mDay)

        dpd.datePicker.maxDate = Calendar.getInstance().timeInMillis

        dpd.show()
    }

    private fun showTimePicker() {
        val currentString : String = lblDate.text.toString()
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->; lblDate.setText(currentString + " " + hourOfDay.toString() + ":" + String.format("%02d",minute))
                time = hourOfDay.toString() + ":" + String.format("%02d",minute)
            }, mHour, mMinute, false)
        timePickerDialog.show()


    }


}
