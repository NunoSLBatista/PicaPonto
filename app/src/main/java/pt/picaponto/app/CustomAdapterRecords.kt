package pt.picaponto.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.picaponto.app.Models.Ferias
import pt.picaponto.app.Models.Registo

class CustomAdapterRecords(private val context: Context, private val registosArrayList: ArrayList<Registo>) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return registosArrayList.size
    }

    override fun getItem(position: Int): Any {
        return registosArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.registo_item, null, true)

            holder.typeName = convertView!!.findViewById(R.id.type) as TextView
            holder.image = convertView.findViewById(R.id.imgView) as ImageView
            holder.dateLabel = convertView.findViewById(R.id.date) as TextView
            holder.timeLabel = convertView.findViewById(R.id.hour) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }


        if(registosArrayList[position].tipoMovimento == "first"){

            holder.image!!.setImageResource(0)
            holder.typeName!!.setText("Tipo")
            holder.dateLabel!!.setText("Data")
            holder.timeLabel!!.setText("Hora")

        } else {

            holder.typeName!!.setText(registosArrayList[position].tipoMovimento)

            if(registosArrayList[position].tipoMovimento == "Entrada"){
                holder.image!!.setImageResource(R.drawable.entry)
            } else {
                holder.image!!.setImageResource(R.drawable.exit)
            }

            holder.dateLabel!!.setText(registosArrayList[position].currentDate)
            holder.timeLabel!!.setText(registosArrayList[position].currentTime)

        }

        return convertView
    }

    private inner class ViewHolder {

        var typeName: TextView? = null
        var dateLabel: TextView? = null
        var timeLabel: TextView? = null
        internal var image: ImageView? = null

    }

}

class CustomAdapterFerias(private val context: Context, private val feriasArrayList: ArrayList<Ferias>) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return feriasArrayList.size
    }

    override fun getItem(position: Int): Any {
        return feriasArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.registo_item, null, true)

            holder.typeName = convertView!!.findViewById(R.id.type) as TextView
            holder.image = convertView.findViewById(R.id.imgView) as ImageView
            holder.dateLabel = convertView.findViewById(R.id.date) as TextView
            holder.timeLabel = convertView.findViewById(R.id.hour) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }


        if(feriasArrayList[position].periodoInicio == "first"){

            holder.image!!.setImageResource(0)
            holder.typeName!!.setText("Estado")
            holder.dateLabel!!.setText("Período")
            holder.timeLabel!!.setText("Observações")

        } else {

            holder.typeName!!.setText(feriasArrayList[position].estado)

            if(feriasArrayList[position].estado == "Pendente"){
                holder.image!!.setImageResource(R.drawable.entry)
            } else {
                holder.image!!.setImageResource(R.drawable.exit)
            }

            holder.dateLabel!!.setText(feriasArrayList[position].periodoInicio + " - " + feriasArrayList[position].periodoFim)
            holder.timeLabel!!.setText(feriasArrayList[position].observacoes)

        }

        return convertView
    }

    private inner class ViewHolder {

        var typeName: TextView? = null
        var dateLabel: TextView? = null
        var timeLabel: TextView? = null
        internal var image: ImageView? = null

    }

}