package com.example.moneyminder

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MyAdapter(items: ArrayList<Contact>, c: Activity) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(),Filterable {
    var items = items
    var c = c

    private var filteredItems: List<Contact> = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return  MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text=filteredItems[position].name
        holder.phone.text=filteredItems[position].phoneNo
        holder.itemView.setOnClickListener(View.OnClickListener {
            var intent=android.content.Intent(c,Landing::class.java)
            intent.putExtra("name",filteredItems[position].name)
            intent.putExtra("phone",filteredItems[position].phoneNo)
            intent.putExtra("id",filteredItems[position].id)
            c.setResult(RESULT_OK,intent)
            c.finish()
        })

    }

    class MyViewHolder(view: View) : ViewHolder(view) {
        var title=view.findViewById<TextView>(R.id.title)
        var phone=view.findViewById<TextView>(R.id.desc)


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    items
                } else {
                    items.filter { it.name.contains(constraint, ignoreCase = true) }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as? List<Contact> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

}
