package pt.picaponto.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import kotlinx.android.synthetic.main.activity_face_detection.*
import pt.picaponto.app.FaceDetection.RectOverlay

class FaceDetectionActivity : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog
    lateinit var alertDialog2: AlertDialog
    lateinit var alertDialog3: AlertDialog

    var tipoEntrada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)

        tipoEntrada = intent.getStringExtra("tipoEntrada")!!

        alertDialog = AlertDialog.Builder(this)
            .setMessage("Por favor aguarde...")
            .setCancelable(false)
            .create();

        alertDialog2 = AlertDialog.Builder(this)
            .setMessage("Certo.")
            .setCancelable(false)
            .create();

        alertDialog3 = AlertDialog.Builder(this)
            .setMessage("Erro. Tente novamente.")
            .setCancelable(false)
            .create();

        btn_detect.setOnClickListener {
            camera_view.captureImage { cameraKitView, byteArray ->
                //camera_view.onStop()
                alertDialog.show()
                var bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
                bitmap = Bitmap.createScaledBitmap(bitmap, camera_view?.width ?: 0, camera_view?.height ?: 0, false)
                runDetector(bitmap)
            }
            graphic_overlay.clear()
        }

    }

    private fun runDetector(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .build()

        val detector = FirebaseVision.getInstance()
            .getVisionFaceDetector(options)

        detector.detectInImage(image)
            .addOnSuccessListener { faces ->
                processFaceResult(faces)

            }.addOnFailureListener {
                it.printStackTrace()
            }

    }

    private fun processFaceResult(faces: MutableList<FirebaseVisionFace>) {

        var i: Int = 0

        faces.forEach {
            val bounds = it.boundingBox
            val rectOverLay = RectOverlay(graphic_overlay, bounds)
            graphic_overlay.add(rectOverLay)
            i += 1
        }

        alertDialog.dismiss()

        if(i > 0){
            val intent = Intent(applicationContext, InfoAuthActivity::class.java)
            intent.putExtra("tipoEntrada", tipoEntrada)
            startActivity(intent)
        } else {
           Toast.makeText(this, "Erro. Tente Novamente.", Toast.LENGTH_LONG).show()
        }

    }

    override fun onResume() {
        super.onResume()
        camera_view.onResume()
    }
    override fun onPause() {
        super.onPause()
        camera_view.onPause()
    }
    override fun onStart() {
        super.onStart()
        camera_view.onStart()
    }
    override fun onStop() {
        super.onStop()
        camera_view.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera_view.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
