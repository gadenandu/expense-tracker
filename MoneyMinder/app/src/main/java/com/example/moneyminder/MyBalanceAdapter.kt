package com.example.moneyminder

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class MyBalanceAdapter(ls: ArrayList<Balance>, context: Context):Adapter<MyBalanceAdapter.MyViewHolder>() {
    var ls=ls
    var context=context
    var onDataChangeListeners: OnDataChangeListener? = null
    fun setOnDataChangeListener(listener: OnDataChangeListener) {
        this.onDataChangeListeners = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view= View.inflate(parent.context,R.layout.balancerow,null)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ls.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text=ls[position].name
        holder.phone.text=ls[position].phoneNo
        holder.ammount.text=ls[position].ammount.toString()
        if(ls[position].ammount<0)
        {
            holder.amount.setBackgroundColor(Color.RED)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            var intent=android.content.Intent(holder.itemView.context,DetailedBalances::class.java)
            intent.putExtra("name",ls[position].name)
            intent.putExtra("phone",ls[position].phoneNo)
            intent.putExtra("id",ls[position].id)
            Log.e("id IN MAIN ",ls[position].id)
            holder.itemView.context.startActivity(intent)
        })

        holder.itemView.setOnLongClickListener {
            var dialog= AlertDialog.Builder(this.context)
            dialog.setTitle("Delete")
            dialog.setMessage("Are you sure you want to delete this?")
            dialog.setPositiveButton("Yes"){dialog,which->
                var dbHelper=MyDbHelper(holder.itemView.context)
                dbHelper.deletebalance(ls[position].id)
                ls.removeAt(position)
                notifyItemRemoved(position)
                onDataChangeListeners?.datachanged()
            }
            dialog.setNegativeButton("No"){dialog,which->

            }
            dialog.show()
            true
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name=view.findViewById<TextView>(R.id.name)
        var phone=view.findViewById<TextView>(R.id.num)
        var ammount=view.findViewById<TextView>(R.id.ammount)

    }

    interface OnDataChangeListener {
        fun datachanged()
    }

}
