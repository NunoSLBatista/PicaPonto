package pt.picaponto.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class Fingerprint extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private static final String TAG = MainActivity.class.getName();


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    String tipoEntrada = "";

    FingerprintManager _fingerprintManager;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint);

        _fingerprintManager = (FingerprintManager) this.getSystemService(FINGERPRINT_SERVICE);

        tipoEntrada = getIntent().getStringExtra("tipoEntrada");


          BiometricCallback callback =  new BiometricCallback() {
                @Override
                public void onSdkVersionNotSupported() {
                    /*
                     *  Will be called if the device sdk version does not support Biometric authentication
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }

                @Override
                public void onBiometricAuthenticationNotSupported() {
                    /*
                     *  Will be called if the device does not contain any fingerprint sensors
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }

                @Override
                public void onBiometricAuthenticationNotAvailable() {
                    /*
                     *  The device does not have any biometrics registered in the device.
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }

                @Override
                public void onBiometricAuthenticationPermissionNotGranted() {
                    /*
                     *  android.permission.USE_BIOMETRIC permission is not granted to the app
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onBiometricAuthenticationInternalError(String error) {
                    /*
                     *  This method is called if one of the fields such as the title, subtitle,
                     * description or the negative button text is empty
                     */
                }

                @Override
                public void onAuthenticationFailed() {
                    /*
                     * When the fingerprint doesn’t match with any of the fingerprints registered on the device,
                     * then this callback will be triggered.
                     */
                }

                @Override
                public void onAuthenticationCancelled() {
                    /*
                     * The authentication is cancelled by the user.
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onAuthenticationSuccessful() {
                    /*
                     * When the fingerprint is has been successfully matched with one of the fingerprints
                     * registered on the device, then this callback will be triggered.
                     */

                    mGoogleApiClient = new GoogleApiClient.Builder(Fingerprint.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(Fingerprint.this)
                            .addOnConnectionFailedListener(Fingerprint.this).build();
                    mGoogleApiClient.connect();


                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    /*
                     * This method is called when a non-fatal error has occurred during the authentication
                     * process. The callback will be provided with an help code to identify the cause of the
                     * error, along with a help message.
                     */

                }

                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    /*
                     * When an unrecoverable error has been encountered and the authentication process has
                     * completed without success, then this callback will be triggered. The callback is provided
                     * with an error code to identify the cause of the error, along with the error message.
                     */
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            };

        if(fingerprintEnabled()){
            new BiometricManager.BiometricBuilder(Fingerprint.this)
                    .setTitle("PicaPonto")
                    .setSubtitle("Submeter Picagem")
                    .setDescription("Verifique a sua identidade para realizar a picagem")
                    .setNegativeButtonText("Cancelar")
                    .build()
                    .authenticate(callback);
        }



        }


    public boolean fingerprintEnabled()
    {
        if (!_fingerprintManager.isHardwareDetected())
        {
            // Device doesn't support fingerprint authentication
            mGoogleApiClient = new GoogleApiClient.Builder(Fingerprint.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(Fingerprint.this)
                    .addOnConnectionFailedListener(Fingerprint.this).build();
            mGoogleApiClient.connect();

            return false;
        }
        else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(Fingerprint.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(Fingerprint.this)
                    .addOnConnectionFailedListener(Fingerprint.this).build();
            mGoogleApiClient.connect();

            return false;
        }
        else if (!_fingerprintManager.hasEnrolledFingerprints())
        {
            changeScreenFace();
            return false;
        }
        else
        {
            // Everything is ready for fingerprint authentication
            return true;
        }
    }

    public void changeScreen(){
        Intent intent = new Intent(getApplicationContext(), InfoAuthActivity.class);
        intent.putExtra("tipoEntrada", tipoEntrada);
        startActivity(intent);
    }

    public void changeScreenFace(){
        Intent intent = new Intent(getApplicationContext(), FaceDetectionActivity.class);
        intent.putExtra("tipoEntrada", tipoEntrada);
        startActivity(intent);
    }



    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        changeScreen();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    Fingerprint.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made
                       this.changeScreen();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(Fingerprint.this, "Localização é obrigatória.", Toast.LENGTH_LONG).show();
                        this.finish();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



}