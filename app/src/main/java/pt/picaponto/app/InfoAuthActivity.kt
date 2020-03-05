package pt.picaponto.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import pt.picaponto.app.Database.DatabaseHelper
import pt.picaponto.app.EmpresaActivity.MY_PREFS_NAME
import pt.picaponto.app.Models.Registo
import kotlinx.android.synthetic.main.activity_info_auth.*
import java.util.*


class InfoAuthActivity : AppCompatActivity() {


    var databaseHelper: DatabaseHelper? = null
    var tipoEntrada: String = ""

     var latitude: String = ""
     var longitude: String = ""

    private var mFusedLocationClient: FusedLocationProviderClient? = null


    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_auth)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        var locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        this.locationRequest = locationRequest

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult!!.getLocations()) {
                    if (location != null) {
                        wayLatitude = location!!.getLatitude()
                        wayLongitude = location!!.getLongitude()
                            latitude = wayLatitude.toString()
                            longitude = wayLongitude.toString()
                    }
                }
            }
        }


        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

        } else {

            getLocation()

        }


        var newPicagem: Registo = Registo()
        var sharedPreferences =
            getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        databaseHelper = DatabaseHelper(applicationContext)


        userLabel.setText(
            databaseHelper!!.getName(
                sharedPreferences.getString(
                    "user",
                    ""
                )!!.toInt()
            )
        )

        labelDate.setText(newPicagem.currentDate)
        hourLabel.setText(newPicagem.currentTime)

        getSupportActionBar()!!.hide()



        val timer = object: CountDownTimer(3500, 500) {
            override fun onTick(millisUntilFinished: Long) {

                getLocation()

            }

            override fun onFinish() {


                tipoEntrada = intent.getStringExtra("tipoEntrada")!!

                var sharedPreferences =
                    getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)

                val empresaActivity: EmpresaActivity =
                    EmpresaActivity()
                var newPicagem: Registo;

                if (verifyAvailableNetwork(this@InfoAuthActivity)) {
                    empresaActivity.sendPicagem(
                        Registo(
                            sharedPreferences.getInt("userID", 0)!!,
                            tipoEntrada,
                            latitude,
                            longitude,
                            1,
                            sharedPreferences.getString("user", "")!!
                        )
                    )
                    newPicagem = Registo(sharedPreferences.getInt("userID", 0)!!, tipoEntrada, latitude, longitude, 1,  sharedPreferences.getString("user", "")!!)
                } else {
                    newPicagem = Registo(sharedPreferences.getInt("userID", 0)!!, tipoEntrada, latitude, longitude, 0, sharedPreferences.getString("user", "")!!)
                }

                databaseHelper = DatabaseHelper(applicationContext);

                databaseHelper!!.addRegistoLatLong(newPicagem).toString()

                finishApp()

            }
        }


        timer.start()


    }

    fun finishApp(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean {
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    private fun getLocation() {

                mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)

                mFusedLocationClient!!.getLastLocation()
                    .addOnSuccessListener(this@InfoAuthActivity) { location ->
                        if (location != null) {
                            wayLatitude = location.getLatitude()
                            wayLongitude = location.getLongitude()

                            this@InfoAuthActivity.latitude = wayLatitude.toString()
                            this@InfoAuthActivity.longitude = wayLongitude.toString()


                            Log.i("Location: ", wayLatitude.toString() + wayLongitude.toString())
                        } else {
                            mFusedLocationClient!!.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                null
                            )
                        }
                    }
    }
}

