package com.example.todoapp.fragment.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Priority
import com.example.todoapp.data.model.ToDoData
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataList = emptyList<ToDoData>()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.titleTxt.text = dataList[position].title
        holder.itemView.descriptionTxt.text = dataList[position].description

        val priority = dataList[position].priority
        when (priority.name) {
            Priority.LOW.name -> holder.itemView.statusDot.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.low_status
                )
            )
            Priority.MEDIUM.name -> holder.itemView.statusDot.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.mediu_status
                )
            )
            Priority.HIGH.name -> holder.itemView.statusDot.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.high_status
                )
            )
        }

        holder.itemView.row_background.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem = dataList[position])
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = dataList.size

    fun setdata(toDoData: List<ToDoData>) {
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}