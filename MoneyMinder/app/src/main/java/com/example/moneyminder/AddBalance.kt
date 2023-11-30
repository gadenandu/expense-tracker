package com.example.moneyminder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import java.text.SimpleDateFormat
import java.util.Calendar

class AddBalance : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_balance)
        var ammount=findViewById<EditText>(R.id.ammount)
        var date=findViewById<TextView>(R.id.date)
        var coment=findViewById<EditText>(R.id.comment)
        var type=findViewById<ToggleButton>(R.id.type)
        var add=findViewById<Button>(R.id.add)

        date.setOnClickListener {
            getDate(date,this)

        }

        add.setOnClickListener {
            if(ammount.text.toString().isEmpty()) {
                ammount.setError("Please enter ammount")
                return@setOnClickListener
            }
            else if(date.text.toString().isEmpty()) {
                date.setError("Please enter date")
                return@setOnClickListener
            }
            else{
                var dbHelper=MyDbHelper(this)
                var type1="0"
                if(type.isChecked){
                    type1="1"
                }
                var id=intent.getStringExtra("id").toString()
                Log.e("id IN ADD BALANCE",id)
                var a=dbHelper.insertdetailedbalance(id,ammount.text.toString(),date.text.toString(),coment.text.toString(),type1)
                dbHelper.close()
                if (a>0){
                    finish()
                }
                else{
                    Log.e("error","error")
                }

            }


        }

    }
    fun getDate(textView: TextView, context: Context){

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { timePicker, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)

            textView.text = SimpleDateFormat("dd/MM/yyyy").format(cal.time)
        }

        textView.setOnClickListener {
            DatePickerDialog(context, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }
    }
}