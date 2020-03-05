package pt.picaponto.app

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import pt.picaponto.app.Database.DatabaseHelper
import com.google.android.gms.location.*
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker
import com.treebo.internetavailabilitychecker.InternetConnectivityListener
import kotlinx.android.synthetic.main.activity_main.*
import pt.picaponto.app.Models.Registo

class MainActivity : AppCompatActivity(), InternetConnectivityListener {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var databaseHelper: DatabaseHelper? = null

    val context: Context = this
    var codigo: Int = 0
    var mInternetAvailabilityChecker: InternetAvailabilityChecker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)
        //Log.d("SharedUser", sharedPreferences.getString("user", ""))

        var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)

        databaseHelper = DatabaseHelper(applicationContext)
        codigo = sharedPreferences.getInt("userID", 0)!!
        var listItems = ArrayList<Registo>()

        listItems = databaseHelper!!.getRecords1(codigo)

            if(listItems.size > 0 && listItems[0].currentDate.length > 7){
                messageHome.setText("A sua última picagem foi no dia " + listItems[0].currentDate.substring(8) + " às " + listItems[0].currentTime)
                labelTipoHome.setText("Entrada")
                imagemHome.setImageDrawable(getResources().getDrawable(R.drawable.entry))
            }

            if(listItems.size > 0 && listItems[0].tipoMovimento != "Entrada"){
                labelTipoHome.setText("Saída")
                imagemHome.setImageDrawable(getResources().getDrawable(R.drawable.exit))
            }

        if(listItems.size == 0){

            messageHome.setText("Nenhuma entrada")
            labelTipoHome.setText("")
            imagemHome.setImageDrawable(null)

        }

        checkLocationPermission()


        optionsBtn.setOnClickListener {
            val intentSettings = Intent(this, OptionsActivity::class.java)
            startActivity(intentSettings)
        }

        picagensBtn.setOnClickListener( View.OnClickListener {
            mInternetAvailabilityChecker!!.removeAllInternetConnectivityChangeListeners()
            val intentFace = Intent(this, Fingerprint::class.java)
            intentFace.putExtra("tipoEntrada", "Entrada")
            startActivity(intentFace)
        })


        scheduleBtn.setOnClickListener {
            mInternetAvailabilityChecker!!.removeAllInternetConnectivityChangeListeners()
            val intentFace = Intent(this, Fingerprint::class.java)
            intentFace.putExtra("tipoEntrada", "Saída")
            startActivity(intentFace)
        }

        backBtn.setOnClickListener {
            this.finishAffinity()
        }

    }


    override fun onResume() {
        // 1
        super.onResume()
        // 2
        updateAll()

        InternetAvailabilityChecker.init(this)
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance()
        mInternetAvailabilityChecker!!.removeAllInternetConnectivityChangeListeners()
        mInternetAvailabilityChecker!!.addInternetConnectivityListener(this)

    }


    fun updateAll(){

        var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)

        databaseHelper = DatabaseHelper(applicationContext)
        codigo = sharedPreferences.getInt("userID", 0)!!
        var listItems = ArrayList<Registo>()

        listItems = databaseHelper!!.getRecords1(codigo)


        if(listItems.size > 0 && listItems[0].currentDate.length > 7){
            messageHome.text = "A sua última picagem foi no dia " + listItems[0].currentDate.substring(8) + " às " + listItems[0].currentTime
            labelTipoHome.text = "Entrada"
            imagemHome.setImageDrawable(getResources().getDrawable(R.drawable.entry))
        }

        if(listItems.size > 0 && listItems[0].tipoMovimento != "Entrada"){
            labelTipoHome.text = "Saída"
            imagemHome.setImageDrawable(getResources().getDrawable(R.drawable.exit))
        }


    }

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Teste")
                    .setMessage("teste")
                    .setPositiveButton("ok") { dialogInterface, i ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
        }
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        if (isConnected) {
            val empresaActivity = EmpresaActivity()
            Log.d("Status", "Connected")
            empresaActivity.syncRecords()
            empresaActivity.syncVacations()
           // empresaActivity.updateData()

        }

    }

}

