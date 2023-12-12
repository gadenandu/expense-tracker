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

class DetailedBalances : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        var context= DetailedBalances()
        var ls=ArrayList<DetailBalance>()
        var adapter=MyDetailBalanceAdapter(ls, context)
        var id=""
        var ammount:TextView?=null
    }

    @SuppressLint("Range", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_balances)
        ammount=findViewById<TextView>(R.id.ammount)
        id=intent.getStringExtra("id").toString()




        var add=findViewById<RelativeLayout>(R.id.add)
        add.setOnClickListener {
            var intent= Intent(this, AddBalance::class.java)
            intent.putExtra("id",id)

            startActivity(intent)
        }

    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        ls=ArrayList<DetailBalance>()
        var dbHelper= MyDbHelper(this)
        ls=dbHelper.selectDetailedBalance(id)
        dbHelper.close()
        var rv=findViewById<RecyclerView>(R.id.rv)
        adapter= MyDetailBalanceAdapter(ls,this)
        adapter.setOnDataChangeListener(object : MyDetailBalanceAdapter.OnDataChangeListener {
            override fun datachanged() {
                Log.e("data","changed")
                onResume()
            }
        })
        var lm= LinearLayoutManager(this)
        rv.layoutManager=lm
        rv.adapter=adapter


        var balance=dbHelper.sumofbalance(id)
        ammount?.text =balance.toString()
        dbHelper.close()
        if(balance<1)
        {
            ammount?.setBackgroundColor(Color.RED)
        }
        else
        {
            ammount?.setBackgroundColor(Color.GREEN)
        }
    }

}