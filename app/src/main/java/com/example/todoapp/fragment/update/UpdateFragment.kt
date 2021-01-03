package com.example.todoapp.fragment.update

import android.app.AlertDialog
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.model.Priority
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.fragment.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)
        view.titleUpdate.setText(args.currentItem.title)
        view.descriptionUpdate.setText(args.currentItem.description)
        view.spinnerPriorityUpdate.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        view.spinnerPriorityUpdate.onItemSelectedListener = sharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSave) {
            updateItem()
        }

        if (item.itemId == R.id.menuDelete) {
            confirmDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Wanna delete the ${args.currentItem.title}?")
        builder.setMessage("After that the ${args.currentItem.title} will be gone ")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteItem()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.create().show()
    }

    private fun deleteItem() {
        toDoViewModel.deleteData(args.currentItem)
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        Toast.makeText(
            requireContext(),
            "Successfully deleted! ${args.currentItem.title}",
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun updateItem() {
        val title = titleUpdate.text.toString()
        val description = descriptionUpdate.text.toString()
        val priority = spinnerPriorityUpdate.selectedItem.toString()

        val validation = sharedViewModel.verifyDataFromUser(title, description)

        if (validation) {
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                sharedViewModel.parseToPriority(priority),
                description
            )
            toDoViewModel.updateData(updateItem)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        } else {
            Toast.makeText(requireContext(), "Successfully update!", Toast.LENGTH_SHORT).show()
        }
    }


}