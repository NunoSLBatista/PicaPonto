package pt.picaponto.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_vacations.*
import kotlinx.android.synthetic.main.activity_records.*
import kotlinx.android.synthetic.main.activity_records.goBack
import kotlinx.android.synthetic.main.activity_records.listView
import pt.picaponto.app.Database.DatabaseHelper
import pt.picaponto.app.Models.Ferias
import pt.picaponto.app.Models.Registo

class ListVacationsActivity : AppCompatActivity() {

    private var customeAdapter:   CustomAdapterFerias? = null
    var list = ArrayList<Ferias>()
    var databaseHelper: DatabaseHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_vacations)

        getSupportActionBar()!!.hide()

        goBack.setOnClickListener {
            this.finish()
        }

        var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)

        databaseHelper = DatabaseHelper(applicationContext)
        list = databaseHelper!!.getVacations(sharedPreferences.getInt("userID", 0))
        customeAdapter = CustomAdapterFerias(this, list)

        listView.adapter = customeAdapter
        

    }
}
