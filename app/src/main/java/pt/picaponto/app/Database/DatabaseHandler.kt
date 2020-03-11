package pt.picaponto.app.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import pt.picaponto.app.Api.PicaPonto
import pt.picaponto.app.Models.*
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_COL)
        db.execSQL(CREATE_TABLE_EMP)
        db.execSQL(CREATE_TABLE_PIC)
        db.execSQL(CREATE_TABLE_FER)
        db.execSQL(CREATE_TABLE_HOR)
        db.execSQL(CREATE_TABLE_PER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS '$CREATE_TABLE_COL'")
        db.execSQL("DROP TABLE IF EXISTS '$CREATE_TABLE_EMP'")
        db.execSQL("DROP TABLE IF EXISTS '$CREATE_TABLE_PIC'")
        db.execSQL("DROP TABLE IF EXISTS '$CREATE_TABLE_FER'")
        db.execSQL("DROP TABLE IF EXISTS '$CREATE_TABLE_HOR'")
        db.execSQL("DROP TABLE IF EXISTS '$CREATE_TABLE_PER'")
        onCreate(db)
    }

    fun updateData(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_PICAGENS")
        db.execSQL("DELETE FROM $TABLE_COL")
        db.execSQL("DELETE FROM $TABLE_EMPRESA")
        db.execSQL("DELETE FROM $TABLE_FERIAS")
        db.execSQL("DELETE FROM $TABLE_FERIAS")

    }

    fun addUser(colaborador: Colaborador) {
        val db = this.writableDatabase
        //adding user name in users table
        val values = ContentValues()
        values.put(COL_ID, colaborador.colaboradorID)
        values.put(COL_EMPRESA, colaborador.empresaID)
        values.put(COL_USER_ID, colaborador.colaboradorID)
        values.put(COL_CODIGO, colaborador.codigo)
        values.put(COL_SENHA, colaborador.senha)
        values.put(COL_DATA, colaborador.dataRegisto)
        values.put(COL_NAME, colaborador.nome)
        values.put(COL_MANUAL, colaborador.picagemManual)

        db.insert(TABLE_COL, null, values);
        //val id = db.insertWithOnConflict(TABLE_COL, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        db.close()

    }


    fun getName(codigo: Int): String{

       val db = this.writableDatabase
       val cusrsor: Cursor
       cusrsor = db.rawQuery("SELECT * FROM $TABLE_COL WHERE $COL_CODIGO = $codigo", null)

        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val name = cusrsor.getString(cusrsor.getColumnIndex(COL_NAME))
                    db.close()
                    return name

                } while (cusrsor.moveToNext())
            }
        }
        db.close()
        return ""
    }

    fun checkColabManual(codigo: Int) : Boolean{

        val db = this.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM $TABLE_COL WHERE $COL_CODIGO = $codigo", null)

        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val manual = cusrsor.getInt(cusrsor.getColumnIndex(COL_MANUAL))
                    if(manual == 0){
                        db.close()
                        return false
                    } else {
                        db.close()
                        return true
                    }

                } while (cusrsor.moveToNext())
            }
        }
        db.close()
        return false
    }

    fun addCompany(empresa: Empresa){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(EMP_ID, empresa.id)
        values.put(EMP_NOME, empresa.nome)

        db.insert(TABLE_EMPRESA, null, values)

        db.close()

    }

    fun getRecords1(codigo: Int) : ArrayList<Registo> {

        val db = this.writableDatabase
        val list = ArrayList<Registo>()
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM $TABLE_PICAGENS WHERE $PIC_COL = $codigo",null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val id = cusrsor.getInt(cusrsor.getColumnIndex(PIC_COL))
                    val cod = cusrsor.getString(cusrsor.getColumnIndex(PIC_COD))
                    val movimento = cusrsor.getString(cusrsor.getColumnIndex(PIC_MOVIMENTO))
                    val data = cusrsor.getString(cusrsor.getColumnIndex(PIC_DATA))
                    val hora = cusrsor.getString(cusrsor.getColumnIndex(PIC_HORA))
                    val newRegisto = Registo(id, movimento, data, hora)
                    newRegisto.codigo = cod
                    list.add(newRegisto)
                } while (cusrsor.moveToNext())
            }
        }
        val newList = ArrayList<Registo>()
        val size = list.size - 1

        for (i in size downTo 0) {
            newList.add(list.get(i))
        }

        db.close()
        return newList
    }

    fun getRecords(codigo: Int) : ArrayList<Registo> {

        val db = this.writableDatabase
        val list = ArrayList<Registo>()
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM $TABLE_PICAGENS WHERE $PIC_COL = $codigo",null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val id = cusrsor.getInt(cusrsor.getColumnIndex(PIC_COL))
                    val movimento = cusrsor.getString(cusrsor.getColumnIndex(PIC_MOVIMENTO))
                    val data = cusrsor.getString(cusrsor.getColumnIndex(PIC_DATA))
                    val hora = cusrsor.getString(cusrsor.getColumnIndex(PIC_HORA))
                    val newRegisto = Registo(id, movimento, data, hora)
                    list.add(newRegisto)
                } while (cusrsor.moveToNext())
            }
        }
        list.add(Registo(1, "first", "Data", "Hora"))
        val newList = ArrayList<Registo>()
        val size = list.size - 1

        for (i in size downTo 0) {
            newList.add(list.get(i))
        }

        db.close()
        return newList
    }

    fun getVacations(codigo: Int) : ArrayList<Ferias> {

        val db = this.writableDatabase
        val list = ArrayList<Ferias>()
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM $TABLE_FERIAS WHERE $FER_ID = $codigo",null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val id = cusrsor.getInt(cusrsor.getColumnIndex(FER_ID))
                    val cod = cusrsor.getString(cusrsor.getColumnIndex(FER_COD))
                    val periodoInicio = cusrsor.getString(cusrsor.getColumnIndex(FER_INICIO))
                    val periodoFim = cusrsor.getString(cusrsor.getColumnIndex(FER_FIM))
                    val observacoes = cusrsor.getString(cusrsor.getColumnIndex(FER_OBS))
                    val estado = cusrsor.getString(cusrsor.getColumnIndex(FER_ESTADO))
                    val newFerias = Ferias(id, periodoInicio, periodoFim, observacoes, estado)
                    newFerias.codigo = cod
                    list.add(newFerias)
                } while (cusrsor.moveToNext())
            }
        }
        list.add(Ferias(0,"first", "Período", "Observações"))
        val newList = ArrayList<Ferias>()
        val size = list.size - 1

        for (i in size downTo 0) {
            newList.add(list.get(i))
        }

        db.close()
        return newList
    }

    fun syncRecords() : ArrayList<Registo> {

        val db = this.writableDatabase
        val list = ArrayList<Registo>()
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM $TABLE_PICAGENS WHERE $PIC_SYNC = 0", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val id = cusrsor.getInt(cusrsor.getColumnIndex(PIC_COL))
                    val cod = cusrsor.getString(cusrsor.getColumnIndex(PIC_COD))
                    val movimento = cusrsor.getString(cusrsor.getColumnIndex(PIC_MOVIMENTO))
                    val data = cusrsor.getString(cusrsor.getColumnIndex(PIC_DATA))
                    val hora = cusrsor.getString(cusrsor.getColumnIndex(PIC_HORA))
                    val lat = cusrsor.getString(cusrsor.getColumnIndex(PIC_LAT))
                    val long = cusrsor.getString(cusrsor.getColumnIndex(PIC_LONG))
                    Log.d("LatLong ", lat + long)
                    val newRegisto =
                        Registo(id, movimento, data, hora, lat, long)
                    newRegisto.codigo = cod
                    list.add(newRegisto)
                } while (cusrsor.moveToNext())
            }
        }
        cusrsor.close()
        return list
    }

    fun syncVacations() : ArrayList<Ferias> {

        val db = this.writableDatabase
        val list = ArrayList<Ferias>()
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM $TABLE_FERIAS WHERE $FER_SYNC = 0", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                cusrsor.moveToFirst()
                do {
                    val id = cusrsor.getInt(cusrsor.getColumnIndex(FER_ID))
                    val codigo = cusrsor.getString(cusrsor.getColumnIndex(FER_COD))
                    val periodoInicio = cusrsor.getString(cusrsor.getColumnIndex(FER_INICIO))
                    val periodoFim = cusrsor.getString(cusrsor.getColumnIndex(FER_FIM))
                    val observacoes = cusrsor.getString(cusrsor.getColumnIndex(FER_OBS))
                    val estado = cusrsor.getString(cusrsor.getColumnIndex(FER_ESTADO))
                    val newFerias = Ferias(id, periodoInicio, periodoFim, observacoes, estado)
                    newFerias.codigo = codigo
                    list.add(newFerias)
                } while (cusrsor.moveToNext())
            }
        }
        cusrsor.close()
        return list
    }

    fun updateSync() {
        val db = this.writableDatabase
        Log.d("Update", "updated")
        db.execSQL("UPDATE " + TABLE_PICAGENS + " SET " + PIC_SYNC + " = 1")
        db.close()
    }

    fun updateSyncVacations() {
        val db = this.writableDatabase
        db.execSQL("UPDATE " + TABLE_FERIAS + " SET " + FER_SYNC + " = 1")
        db.close()
    }

    fun addRegisto(registo: Registo){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(PIC_COL, registo.colaboradorID)
        values.put(PIC_COD, registo.codigo)
        values.put(PIC_MOVIMENTO, registo.tipoMovimento)
        values.put(PIC_DATA, registo.currentDate)
        values.put(PIC_HORA, registo.currentTime)
        values.put(PIC_SYNC, registo.synch)

        db.insert(TABLE_PICAGENS, null, values)

        db.close()
    }

    fun addFerias(ferias: Ferias){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(FER_ID, ferias.colaboradorID)
        values.put(FER_COD, ferias.codigo)
        values.put(FER_INICIO, ferias.periodoInicio)
        values.put(FER_FIM, ferias.periodoFim)
        values.put(FER_OBS, ferias.observacoes)
        values.put(FER_ESTADO, ferias.estado)
        values.put(FER_SYNC, ferias.sync)

        db.insert(TABLE_FERIAS, null, values)
        db.close()
    }

    fun addRegistoLatLong(registo: Registo) : Boolean{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(PIC_COL, registo.colaboradorID)
        values.put(PIC_COD, registo.codigo)
        values.put(PIC_MOVIMENTO, registo.tipoMovimento)
        values.put(PIC_DATA, registo.currentDate)
        values.put(PIC_HORA, registo.currentTime)
        values.put(PIC_SYNC, registo.synch)
        values.put(PIC_LAT, registo.latitude)
        values.put(PIC_LONG, registo.longitude)

        val rowInserted = db.insert(TABLE_PICAGENS, null, values)

        db.close()


        if(rowInserted != -1L){
         return true
        }
        return false


    }

    fun checkPin(codigo: Int, senha: Int) : Int {

        val selectQuery = "SELECT  * FROM $TABLE_COL WHERE $COL_CODIGO = $codigo AND $COL_SENHA = $senha"
        var returnValue = -1
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)

        if (c != null) {
            if (c.count > 0) {
                c.moveToFirst()
                do {
                    val id = c.getInt(c.getColumnIndex(COL_USER_ID))
                    returnValue = id
                } while (c.moveToNext())
            }
        }
        db.close()
        return returnValue
    }

    fun addDia(dia: Dia){

        val db = this.writableDatabase

        val values = ContentValues()
        values.put(HOR_ID, dia.id)
        values.put(HOR_DIA, dia.dia)
        values.put(HOR_TIPO, dia.periodoNome)

        db.insert(TABLE_HORARIO, null, values)

        Log.d("Size: ", dia.periodo.size.toString())

        for(i in 0..dia.periodo.size - 1){
            val values2 = ContentValues()
            values2.put(PER_DIA, dia.dia)
            values2.put(PER_INICIO, dia.periodo[i].horaInicio)
            values2.put(PER_FIM, dia.periodo[i].horaFim)

            db.insert(TABLE_PERIODO, null, values2)

        }

        db.close()

    }

    fun getPeriod(dia: String) : ArrayList<Periodo> {

        val selectQuery = "SELECT * FROM $TABLE_PERIODO WHERE $PER_DIA = $dia"
        val listPeriodos = ArrayList<Periodo>()
        val db = this.readableDatabase

        val c = db.rawQuery(selectQuery, null)

        if(c != null){
            if(c.count > 0){
                c.moveToFirst()
                do {
                    val horaInicio = c.getString(c.getColumnIndex(PER_INICIO))
                    val horaFim = c.getString(c.getColumnIndex(PER_FIM))

                    listPeriodos.add(Periodo(horaInicio, horaFim))

                } while (c.moveToNext())
            }
        }

        db.close()

        return listPeriodos

    }


    fun getDays() : ArrayList<Dia>{

        val selectQuery = "SELECT * FROM $TABLE_HORARIO"
        val listDias = ArrayList<Dia>()
        val db = this.readableDatabase

        val c = db.rawQuery(selectQuery, null)

        if (c != null) {
            if (c.count > 0) {
                c.moveToFirst()
                do {
                    val id = c.getInt(c.getColumnIndex(HOR_ID))
                    val dia = c.getString(c.getColumnIndex(HOR_DIA))
                    val tipo = c.getString(c.getColumnIndex(HOR_TIPO))

                    listDias.add(Dia(id, dia, tipo, getPeriod(dia)))

                } while (c.moveToNext())
            }
        }

        db.close()

        return listDias

    }


    companion object {

        var DATABASE_NAME = "picaponto"
        private val DATABASE_VERSION = 1

        //Tabela Colaboradores
        private val TABLE_COL = "colaboradores"
        private val COL_ID = "id"
        private val COL_EMPRESA = "empresa_id"
        private val COL_USER_ID = "colaborador_id"
        private val COL_CODIGO = "codigo"
        private val COL_CARTAO = "cartao"
        private val COL_SENHA = "senha"
        private val COL_DATA = "data_registo"
        private val COL_NAME = "name"
        private val COL_MANUAL = "picagem_manual"

        private val CREATE_TABLE_COL = ("CREATE TABLE "
                + TABLE_COL + "(" + COL_ID + " INTEGER ," +
                COL_EMPRESA + " TEXT ," +
                COL_USER_ID + " INTEGER ," +
                COL_CODIGO + " INTEGER ," +
                COL_SENHA + " INTEGER ," +
                COL_NAME + " TEXT ," +
                COL_MANUAL + " INTEGER ," +
                COL_DATA + " TEXT" + ");")

        //Tabela Empresa
        private val TABLE_EMPRESA = "empresa"
        private val EMP_ID = "empresa_id"
        private val EMP_NOME = "empresa_nome"

        private val CREATE_TABLE_EMP = ("CREATE TABLE " +
                TABLE_EMPRESA + "(" + EMP_ID + " INTEGER ," +
                EMP_NOME + " TEXT" + ");")


        //Tabela Picagens
        private val TABLE_PICAGENS = "picagens"
        private val PIC_COL = "colaborador_id"
        private val PIC_COD = "codigo"
        private val PIC_MOVIMENTO = "tipo_movimento"
        private val PIC_DATA = "data"
        private val PIC_HORA = "hora"
        private val PIC_LAT = "latitude"
        private val PIC_LONG = "longitude"
        private val PIC_SYNC = "sync"

        private val CREATE_TABLE_PIC = ("CREATE TABLE " +
                TABLE_PICAGENS + "(" +
                PIC_COL + " INTEGER ," +
                PIC_COD + " TEXT ," +
                PIC_MOVIMENTO + " TEXT ," +
                PIC_DATA + " TEXT ," +
                PIC_HORA + " TEXT ," +
                PIC_LAT + " TEXT ," +
                PIC_LONG + " TEXT ," +
                PIC_SYNC + " INTEGER "+ ");")


        // Tabela horario
        private val TABLE_HORARIO = "horario"
        private val HOR_ID = "horario_id"
        private val HOR_DIA = "horario_dia"
        private val HOR_TIPO = "tipo_periodo"

        private val CREATE_TABLE_HOR = ("CREATE TABLE " +
                TABLE_HORARIO + "(" +
                HOR_ID + " INTEGER ," +
                HOR_DIA + " TEXT ," +
                HOR_TIPO + " TEXT " + ");")


        // Tabela Periodos
        private val TABLE_PERIODO = "periodo"
        private val PER_DIA = "horario_dia"
        private val PER_INICIO = "hora_inicio"
        private val PER_FIM = "hora_fim"

        private val CREATE_TABLE_PER = ("CREATE TABLE " +
                TABLE_PERIODO + "(" +
                PER_DIA + " TEXT ," +
                PER_INICIO + " TEXT ," +
                PER_FIM + " TEXT " + ");")


        //Tabela Ferias
        private val TABLE_FERIAS = "ferias"
        private val FER_ID = "colaborador_id"
        private val FER_COD = "codigo"
        private val FER_INICIO = "periodo_incio"
        private val FER_FIM = "periodo_fim"
        private val FER_OBS = "observacoes"
        private val FER_ESTADO = "estado"
        private val FER_SYNC = "sync"

        private val CREATE_TABLE_FER = ("CREATE TABLE " +
                TABLE_FERIAS + "(" +
                FER_ID + " INTEGER ," +
                FER_COD + " TEXT ," +
                FER_INICIO + " TEXT ," +
                FER_FIM + " TEXT ," +
                FER_OBS + " TEXT ," +
                FER_SYNC + " INTEGER ," +
                FER_ESTADO + " TEXT "+ ");")




    }

}