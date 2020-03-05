package pt.picaponto.app

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pt.picaponto.app.Models.Dia
import java.util.*
import kotlin.collections.ArrayList
import java.nio.file.Files.delete



class ScheduleAdapter(private val context: Context, private val days : Int, private val daysType : ArrayList<Dia>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.day_schedule_item, parent, false)
        return ScheduleAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 8
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days + position)
        val dateCalendar : String = calendar.get(Calendar.YEAR).toString() + "-" + 11 + "-" + calendar.get(Calendar.DAY_OF_MONTH)

        for (x in 0..daysType.size - 1){
            if(dateCalendar == daysType[x].periodoNome){
                holder.dateTextView.text = daysType[x].dia
            } else {
                holder.dateTextView.text = ""
            }
        }

        holder.setIsRecyclable(false)

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView = itemView.findViewById(R.id.typeText) as TextView
        val linearLayoutBox = itemView.findViewById(R.id.boxLayout) as LinearLayout
    }


}