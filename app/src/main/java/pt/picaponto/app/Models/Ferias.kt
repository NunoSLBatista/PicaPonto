package pt.picaponto.app.Models

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Ferias {

    var colaboradorID: Int = 0
    var codigo: String = ""
    var periodoInicio: String = ""
    var periodoFim: String = ""
    var observacoes: String = ""
    var estado: String = ""
    var sync: Int = 0


    constructor(colaboradorID: Int, periodoInicio: String, periodoFim: String, observacoes: String) {

        this.colaboradorID = colaboradorID
        this.periodoInicio = periodoInicio
        this.periodoFim = periodoFim
        this.observacoes = observacoes

    }

    constructor(colaboradorID: Int, periodoInicio: String, periodoFim: String, observacoes: String, estado: String) {

        this.colaboradorID = colaboradorID
        this.periodoInicio = periodoInicio
        this.periodoFim = periodoFim
        this.observacoes = observacoes
        this.estado = estado

    }

    constructor(colaboradorID: Int, periodoInicio: String, periodoFim: String, observacoes: String, estado: String, sync:  Int, codigo: String) {

        this.colaboradorID = colaboradorID
        this.periodoInicio = periodoInicio
        this.periodoFim = periodoFim
        this.observacoes = observacoes
        this.estado = estado
        this.sync = sync
        this.codigo = codigo

    }



    fun toJSON(): JSONObject {

        val jo = JSONObject()
        jo.put("periodo_inicio", periodoInicio)
        jo.put("periodo_fim", periodoFim)
        jo.put("codigo", codigo)
        jo.put("observacoes", observacoes)

        return jo
    }

}