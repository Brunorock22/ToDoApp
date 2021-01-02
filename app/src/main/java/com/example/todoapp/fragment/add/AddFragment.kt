package com.example.todoapp.fragment.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.fragment.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    val toDoViewModel: ToDoViewModel by viewModels()
    val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)
        view.spinnerPriorityAdd.onItemSelectedListener = sharedViewModel.listener
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

        val validation = sharedViewModel.verifyDataFromUser(title, description)
        if (validation) {
            val newData = ToDoData(0, title, sharedViewModel.parseToPriority(priority), description)
            toDoViewModel.insertData(newData)
            findNavController().popBackStack()

            Toast.makeText(context, "Save Sucess in Database", Toast.LENGTH_LONG)

        } else {
            Toast.makeText(context, "Save Error in Database", Toast.LENGTH_SHORT)

        }

    }


}