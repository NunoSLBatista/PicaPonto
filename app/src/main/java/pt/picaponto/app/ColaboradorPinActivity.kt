package pt.picaponto.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_colaborador_pin.*

class ColaboradorPinActivity : AppCompatActivity() {

    var colCode = ""
    var numberButtons2 = arrayOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colaborador_pin)

        getSupportActionBar()!!.hide()

        numberButtons2 = arrayOf(btn0v2, btn1v2, btn2v2, btn3v2, btn4v2, btn5v2, btn6v2, btn7v2, btn8v2, btn9v2, dotBtnv2)


        for (i in 0..numberButtons2.size - 1){
            numberButtons2[i].setOnClickListener {
                newDigit2(it)
            }
        }

        deleteBtnv2.setOnClickListener {
            newDigit2(it)
        }

        confirmButton.setOnClickListener {
            val intent = Intent(this, PinCodeActivity::class.java)
            intent.putExtra("codigo", colCode)
            startActivity(intent)
        }

    }

    fun newDigit2(v: View){

        if(v != deleteBtnv2){
            checkButtonClicked(v)
            updateColCode()
        }

        if (colCode.length > 0 &&v == deleteBtnv2){
            colCode = colCode.substring(0, colCode.length - 1);
            updateColCode()
        }
    }

    fun checkButtonClicked(v: View){
        for (i in 0..numberButtons2.size - 1){
            if(v == numberButtons2[i] && v != dotBtnv2){
                colCode += i.toString()
            } else if (v == dotBtnv2){
                colCode += "."
                break
            }
        }

    }

    fun updateColCode(){

        editText.setText(colCode)

    }

}
