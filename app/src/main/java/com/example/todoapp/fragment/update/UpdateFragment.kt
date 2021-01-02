package com.example.todoapp.fragment.update

import android.os.Bundle
import android.renderscript.RenderScript
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.model.Priority
import com.example.todoapp.fragment.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)
        view.titleUpdate.setText(args.currentItem.title)
        view.descriptionUpdate.setText(args.currentItem.description)
        view.spinnerPriorityUpdate.setSelection(parsePriority(args.currentItem.priority))
        view.spinnerPriorityUpdate.onItemSelectedListener = sharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    private fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }

    }

}