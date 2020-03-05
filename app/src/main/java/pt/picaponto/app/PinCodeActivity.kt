package pt.picaponto.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.picaponto.app.Database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_pin_code.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PinCodeActivity : AppCompatActivity() {

    var userCode = ""
    var pinCode = ""
    var numberButtons = arrayOf<Button>()
    var passwordDigits =  arrayOf<EditText>()
    var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)

        getSupportActionBar()!!.hide()

        userCode = getIntent().getStringExtra("codigo")

        numberButtons = arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        passwordDigits =  arrayOf(char1, char2, char3, char4)

        for (i in 0..numberButtons.size - 1){
            numberButtons[i].setOnClickListener {
                newDigit(it)
            }
        }

        deleteBtn.setOnClickListener {
            newDigit(it)
        }
    }

    fun newDigit(v: View){

        if(pinCode.length < 4 && v != deleteBtn){
            checkButtonClicked(v)
            updatePassword()
        }

        if (pinCode.length > 0 && v == deleteBtn){
            pinCode = pinCode.substring(0, pinCode.length - 1);
            updatePassword()
        }

    }

    fun checkButtonClicked(v: View){
        for (i in 0..numberButtons.size - 1){
            if(v == numberButtons[i]){
                pinCode += i.toString()
            }
        }
    }

    fun updatePassword(){

        for(i in 0..passwordDigits.size - 1){
            if(i < pinCode.length){
                passwordDigits[i].setText("1")
            } else {
                passwordDigits[i].setText("")
            }
        }

        if(pinCode.length == 4){

            databaseHelper = DatabaseHelper(applicationContext)

            val colID: Int = databaseHelper!!.checkPin(userCode.toInt(), pinCode.toInt())

            if(colID > -1){

                var sharedPreferences = getSharedPreferences(EmpresaActivity.MY_PREFS_NAME, Context.MODE_PRIVATE)

                sharedPreferences.edit().putString("user", userCode).apply()
                sharedPreferences.edit().putInt("userID", colID).apply()

                val intentApp = Intent(this, MainActivity::class.java)
                startActivity(intentApp)
            } else {
                Toast.makeText(applicationContext, "Funcionario Incorreto", Toast.LENGTH_SHORT).show()
            }


        }

    }

}
