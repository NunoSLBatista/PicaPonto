package pt.picaponto.app.Models

class Dia {

    var id: Int = 0
    var periodoNome: String = ""
    var dia: String = ""
    var periodo = ArrayList<Periodo>()

    constructor(id: Int, periodoNome: String, dia: String, periodo: ArrayList<Periodo>){

        this.id = id
        this.periodoNome = periodoNome
        this.dia = dia
        this.periodo = periodo

    }




}