package pt.picaponto.app.Models

class Colaborador {

    var id: Int = 0
    var empresaID: Int = 0
    var colaboradorID: Int = 0
    var codigo: Int = 0
    var cartao: String = ""
    var senha: Int = 0
    var dataRegisto: String = ""
    var picagemManual: Int = 0
    var nome: String = ""

    constructor(id: Int, empresaID: Int, colaboradorID: Int, codigo: Int, cartao: String, senha: Int, dataRegisto: String) {
        this.id = id
        this.empresaID = empresaID
        this.colaboradorID = colaboradorID
        this.codigo = codigo
        this.cartao = cartao
        this.senha = senha
        this.dataRegisto = dataRegisto
    }



}