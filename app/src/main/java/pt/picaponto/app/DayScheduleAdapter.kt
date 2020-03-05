package pt.picaponto.app

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class DayScheduleAdapter(private val context: Context, private val days : Int) : RecyclerView.Adapter<DayScheduleAdapter.ViewHolder>() {

    var monthName = arrayOf(
        "Jan",
        "Fev",
        "Mar",
        "Abr",
        "Mai",
        "Jun",
        "Jul",
        "Ago",
        "Set",
        "Out",
        "Nov",
        "Dez"
    )

    var dayWeek = arrayOf(
        "Dom",
        "Seg",
        "Ter",
        "Qua",
        "Qui",
        "Sex",
        "Sáb"
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.day_schedule_item, parent, false)
        return DayScheduleAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return 8
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days + position)
        holder.dateTextView.text = calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + dayWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]

        holder.setIsRecyclable(false)

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView = itemView.findViewById(R.id.typeText) as TextView
        val linearLayoutBox = itemView.findViewById(R.id.boxLayout) as LinearLayout
    }


}