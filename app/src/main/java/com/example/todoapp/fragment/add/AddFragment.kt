package com.example.todoapp.fragment.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.todoapp.R
import com.example.todoapp.data.model.Priority
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {

    val toDoViewModel: ToDoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.add_frament_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuAdd) {
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun insertDataToDatabase() {

        val title = titleAdd.text.toString()
        val priority = spinnerPriorityAdd.selectedItem.toString()
        val description = descriptionAdd.text.toString()

        val validation = verifyDataFromUser(title, description)
        if (validation) {
            val newData = ToDoData(0, title, parseToPriority(priority), description)
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Save Sucess in Database", Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(requireContext(), "Save Error in Database", Toast.LENGTH_SHORT)

        }

    }

    private fun verifyDataFromUser(title: String, desciption: String): Boolean {
        return if (TextUtils.isEmpty((title)) || TextUtils.isEmpty((desciption))) {
            false
        } else !(title.isEmpty() || desciption.isEmpty())
    }

    private fun parseToPriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.HIGH
            "Low Priority" -> Priority.HIGH
            else -> Priority.LOW
        }
    }

}