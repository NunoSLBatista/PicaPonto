package pt.picaponto.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.picaponto.app.Database.DatabaseHelper
import pt.picaponto.app.Models.Registo
import kotlinx.android.synthetic.main.activity_records.*

class RecordsActivity : AppCompatActivity() {

    private var customeAdapter:   CustomAdapterRecords? = null
    var list = ArrayList<Registo>()
    var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        getSupportActionBar()!!.hide()

        goBack.setOnClickListener {
            val intentGoBack = Intent(this, OptionsActivity::class.java)
            startActivity(intentGoBack)
        }

            var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)

            databaseHelper = DatabaseHelper(applicationContext)
            list = databaseHelper!!.getRecords(sharedPreferences.getInt("userID", 0))
            customeAdapter = CustomAdapterRecords(this, list)

       listView.adapter = customeAdapter
    }
}
