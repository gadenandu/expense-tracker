package com.example.moneyminder

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter


class MyDetailBalanceAdapter(ls: ArrayList<DetailBalance>, context: DetailedBalances):Adapter<MyDetailBalanceAdapter.MyViewHolder>() {
    var ls=ls
    var context=context

    var onDataChangeListeners: OnDataChangeListener? = null
    fun setOnDataChangeListener(listener: OnDataChangeListener) {
        this.onDataChangeListeners = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view= View.inflate(parent.context,R.layout.detailbalancerow,null)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ls.size
    }


    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.date.text=ls[position].date
        holder.ammount.text=ls[position].ammount
        Log.e("type",ls[position].type)
        if(ls[position].type.equals("1")){
            holder.ammount.setBackgroundColor(Color.GREEN)
        }
        else{
            holder.ammount.setBackgroundColor(Color.RED)
        }
        holder.itemView.setOnLongClickListener {
            var dialog= AlertDialog.Builder(context)
            dialog.setTitle("Delete")
            dialog.setMessage("Are you sure you want to delete this?")
            dialog.setPositiveButton("Yes"){dialog,which->
                var dbHelper=MyDbHelper(holder.itemView.context)
                dbHelper.deleteDetailBalance(ls[position].id)
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
        var date=view.findViewById<TextView>(R.id.date)
        var ammount=view.findViewById<TextView>(R.id.ammount)
    }


    interface OnDataChangeListener {
        fun datachanged()
    }

}
