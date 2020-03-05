package pt.picaponto.app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aminography.primecalendar.PrimeCalendar
import kotlinx.android.synthetic.main.activity_options.vacationsBtn
import kotlinx.android.synthetic.main.activity_vacations.*
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet
import kotlinx.android.synthetic.main.registo_item.*
import pt.picaponto.app.Database.DatabaseHelper
import pt.picaponto.app.Models.Ferias
import java.util.*


class VacationsActivity : AppCompatActivity() {

    val calendar = CivilCalendar()
    var startDate = ""
    var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacations)

        val minDate = CivilCalendar()


        val datePicker = PrimeDatePickerBottomSheet.newInstance(calendar, minDate, null, PickType.RANGE_START, null, null, null)


        periodoLbl.setOnClickListener {

            datePicker.showNow(supportFragmentManager, "SOME_TAG")

        }

        datePicker.setOnDateSetListener(object : PrimeDatePickerBottomSheet.OnDayPickedListener {

            override fun onSingleDayPicked(singleDay: PrimeCalendar) {
                // TODO
            }

            override fun onRangeDaysPicked(startDay: PrimeCalendar, endDay: PrimeCalendar) {

                startDate = startDay.year.toString() + "-" + startDay.month.toString() + "-" + startDay.dayOfMonth.toString()
                endDate =  endDay.year.toString() + "-" + endDay.month.toString() + "-" + endDay.dayOfMonth.toString()

                periodoLbl.setText(startDay.year.toString() + "-" + startDay.month.toString() + "-" + startDay.dayOfMonth.toString()
                        + " - " + endDay.year.toString() + "-" + endDay.month.toString() + "-" + endDay.dayOfMonth.toString())

            }
        })


        pedirBtn.setOnClickListener {

            var sharedPreferences =
                getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)
            var DH = DatabaseHelper(this)


            if(startDate != "" && endDate != ""){

                if(verifyAvailableNetwork(this)){

                    DH.addFerias(Ferias(sharedPreferences.getInt("userID", 0)!!, startDate, endDate, obsLbl.text.toString(), "Pendente", 1, sharedPreferences.getString("user", "")!!))

                    val empresaActivity = EmpresaActivity()
                    empresaActivity.sendVacation(Ferias(sharedPreferences.getInt("userID", 0)!!, startDate, endDate, obsLbl.text.toString(), "Pendente", 1, sharedPreferences.getString("user", "")!!))
                } else {

                    DH.addFerias(Ferias(sharedPreferences.getInt("userID", 0)!!, startDate, endDate, obsLbl.text.toString(), "Pendente", 0, sharedPreferences.getString("user", "")!!))


                }

                val intentList = Intent(this, InfoAuthActivityManual::class.java)
                intentList.putExtra("date", "Desde " + startDate)
                intentList.putExtra("time", "Até " + endDate)
                startActivity(intentList)

            } else {

                Toast.makeText(this, "Por favor indique as datas.", Toast.LENGTH_LONG).show()

            }

        }

        listFeriasBtn.setOnClickListener {

            val intentList = Intent(this, ListVacationsActivity::class.java)
            startActivity(intentList)

        }

        periodoLbl.setOnClickListener {
            Log.d("TAG", "message")
            datePicker.showNow(supportFragmentManager, "SOME_TAG")
        }

        backBtn.setOnClickListener {
            val intentBack = Intent(this, OptionsActivity::class.java)
            startActivity(intentBack)
        }


    }

    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean {
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

}
