package com.example.todoapp.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.fragment.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : Fragment() {
    private val adapter: ListAdapter by lazy { ListAdapter() }

    private val toDoViewModel: ToDoViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        toDoViewModel.getAllData?.observe(viewLifecycleOwner, Observer { data ->
        sharedViewModel.checkIfDatabaEmpty(data)
            adapter.setdata(data)
        })
        sharedViewModel.emptyDataBase.observe(viewLifecycleOwner, Observer { isEmpty ->
                controllVisibility(isEmpty)


        })

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)

        }
        view.listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        //Set Menu
        setHasOptionsMenu(true)
        return view
    }

    private fun controllVisibility(empty: Boolean) {
        if(empty){
            view?.recyclerView?.visibility = View.GONE
            view?.imageViewNoData?.visibility = View.VISIBLE

        }else{
            view?.recyclerView?.visibility = View.VISIBLE
            view?.imageViewNoData?.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuDeleteAll) {
            deleteAllDataBase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllDataBase() {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Wanna delete ALL?")
        builder.setMessage("After that the all elements will be gone ")
        builder.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteAll()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.create().show()
    }

}