package pt.picaponto.app.Models

import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONObject


    class Registo {

        var colaboradorID: Int = 0
        var tipoMovimento: String = ""
        var codigo: String = ""
        var currentDate: String = ""
        var currentTime: String = ""
        var synch: Int = 0
        var latitude: String = ""
        var longitude: String = ""


        constructor(colaboradorID: Int, tipoMovimento: String, latitude: String, longitude: String, sync: Int) {

            this.colaboradorID = colaboradorID
            this.tipoMovimento = tipoMovimento

            var sdf_date = SimpleDateFormat("yyyy/M/dd")
            currentDate = sdf_date.format(Date())

            var sdf_time = SimpleDateFormat("HH:mm")
            currentTime = sdf_time.format(Date())

            this.latitude = latitude
            this.longitude = longitude
            this.synch = sync


        }

        constructor(colaboradorID: Int, tipoMovimento: String, latitude: String, longitude: String, sync: Int, codigo: String) {

            this.colaboradorID = colaboradorID
            this.tipoMovimento = tipoMovimento

            var sdf_date = SimpleDateFormat("yyyy/M/dd")
            currentDate = sdf_date.format(Date())

            var sdf_time = SimpleDateFormat("HH:mm")
            currentTime = sdf_time.format(Date())

            this.latitude = latitude
            this.longitude = longitude
            this.synch = sync
            this.codigo = codigo


        }

        constructor(colaboradorID: Int, tipoMovimento: String, data: String, hora: String, latitude: String, longitude: String) {

            this.colaboradorID = colaboradorID
            this.tipoMovimento = tipoMovimento

            this.colaboradorID = colaboradorID
            this.tipoMovimento = tipoMovimento

            this.currentDate = data
            this.currentTime = hora

            this.latitude = latitude
            this.longitude = longitude


        }


        constructor(colaboradorID: Int, tipoMovimento: String, data: String, hora: String) {

            this.colaboradorID = colaboradorID
            this.tipoMovimento = tipoMovimento

            this.currentDate = data
            this.currentTime = hora

            this.synch = 1

        }

        constructor(){

            var sdf_date = SimpleDateFormat("yyyy-M-dd")
            currentDate = sdf_date.format(Date())

            var sdf_time = SimpleDateFormat("HH:mm")
            currentTime = sdf_time.format(Date())

        }

        fun toJSON(): JSONObject {

            val jo = JSONObject()
            jo.put("id", "")
            jo.put("tipo_movimento", tipoMovimento)
            jo.put("datahora", currentDate + " " + currentTime)
            jo.put("codigo", codigo.toInt())
            jo.put("latitude", latitude)
            jo.put("longitude", longitude)
            jo.put("dispositivo", "android")

            return jo
        }

    }