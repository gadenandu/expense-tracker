package com.example.moneyminder

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Landing : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        var ammount:TextView?=null
        var context= Landing()
        var ls=ArrayList<Balance>()
        var adapter=MyBalanceAdapter(ls, context)
    }
    @SuppressLint("Range", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        ammount=findViewById<TextView>(R.id.ammount)

        var add=findViewById<RelativeLayout>(R.id.add)
        add.setOnClickListener {
            var intent= Intent(this,MainActivity::class.java)
            startActivityForResult(intent,1)
        }

    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()

        ls=ArrayList<Balance>()
        var rv=findViewById<RecyclerView>(R.id.rv)
        var dbHelper=MyDbHelper(this)
        var db=dbHelper.readableDatabase
        var cursor=db.rawQuery("SELECT * FROM balance ORDER BY name ASC",null)
        if(cursor.moveToFirst()){
            do{
                var name=cursor.getString(cursor.getColumnIndex("name"))
                var phone=cursor.getString(cursor.getColumnIndex("phone"))
                var id=cursor.getString(cursor.getColumnIndex("id"))

                ls.add(Balance(name,phone,id,dbHelper.sumofbalance(id)))
            }while (cursor.moveToNext())
        }
        cursor.close()
        dbHelper.close()

        adapter=MyBalanceAdapter(ls,this)

        var sum=0
        for (i in 0 until adapter.ls.size){
            sum+= adapter.ls[i].ammount
            ammount?.text=sum.toString()
            if(sum<0)
            {
                ammount?.setBackgroundColor(Color.RED)
            }
            else{
                ammount?.setBackgroundColor(Color.GREEN)
            }
        }


        adapter.setOnDataChangeListener(object : MyBalanceAdapter.OnDataChangeListener {
            override fun datachanged() {
                Log.e("data","changed")
                onResume()
            }
        })
        var lm= LinearLayoutManager(this)
        rv.layoutManager=lm
        rv.adapter=adapter


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && resultCode== RESULT_OK)
        {
            var name= data?.getStringExtra("name")
            var phone= data?.getStringExtra("phone")
            var id= data?.getStringExtra("id")
            var dbHelper=MyDbHelper(this)

            if(!dbHelper.checknamenum(name!!,phone!!)){
                dbHelper.insertbalance(name!!,phone!!)
                onResume()

            }
        }




    }
    fun refresh(){
        onResume()
    }
}