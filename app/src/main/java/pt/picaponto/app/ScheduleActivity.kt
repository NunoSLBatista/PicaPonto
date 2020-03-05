package pt.picaponto.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_schedule.*
import pt.picaponto.app.Database.DatabaseHelper
import pt.picaponto.app.Models.Dia
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager



class ScheduleActivity : AppCompatActivity() {

    var databaseHelper: DatabaseHelper? = null
    lateinit var days : ArrayList<Dia>

    var currentPage : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        databaseHelper = DatabaseHelper(applicationContext)

        val empresaActivity = EmpresaActivity()
        empresaActivity.getHorario()

        days = databaseHelper!!.getDays()

        updateLists()


        back.setOnClickListener {
           if(currentPage > 0){
               currentPage = currentPage - 1
           }
            updateLists()
        }

        front.setOnClickListener {
            if(currentPage < 5){
                currentPage = currentPage + 1
            }
            updateLists()
        }



    }

    fun updateLists(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        scheduleList.setLayoutManager(layoutManager)
        scheduleList.adapter = ScheduleAdapter(applicationContext, currentPage * 7, days)

        val layoutManagerDay = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dayScheduleList.setLayoutManager(layoutManagerDay)
        dayScheduleList.adapter = DayScheduleAdapter(applicationContext, currentPage * 7)
    }

}
