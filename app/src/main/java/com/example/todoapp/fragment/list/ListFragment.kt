package com.example.todoapp.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.todoapp.R
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragment.SharedViewModel
import com.example.todoapp.fragment.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private val adapter: ListAdapter by lazy { ListAdapter() }

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _biding: FragmentListBinding? = null
    private val biding get() = _biding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentListBinding.inflate(inflater, container, false)
        biding.lifecycleOwner = this
        biding.sharedViewModel = sharedViewModel
        setUpRecyclerView()

        toDoViewModel.getAllData?.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDatabaEmpty(data)
            adapter.setdata(data)
        })


        //Set Menu
        setHasOptionsMenu(true)
        return biding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = biding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL)


        animationSetUp(recyclerView)
        swipeToDelete(recyclerView)
    }

    private fun animationSetUp(recyclerView: RecyclerView) {
        recyclerView.adapter = AlphaInAnimationAdapter(adapter).apply {
            // Change the durations.
            setDuration(2000)
            // Change the interpolator.
            setInterpolator(OvershootInterpolator())
            // Disable
            // the first scroll mode.
            setFirstOnly(false)
        }
        recyclerView.itemAnimator = SlideInLeftAnimator().apply {
            setInterpolator(OvershootInterpolator())
        }
    }

    fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeteleCallBack = object : SwipeToDetele() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.deleteData(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)


                //Restore Data
                restoreDeleteData(
                    viewHolder.itemView,
                    deletedItem = itemToDelete,
                    viewHolder.adapterPosition
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeteleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun restoreDeleteData(view: View, deletedItem: ToDoData, position: Int) {
        val snackbar = Snackbar.make(view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_SHORT)
        snackbar.setAction("Undo") {
            toDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }

        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menuSearch)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        toDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setdata(it)
            }

        })
    }


}