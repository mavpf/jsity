package com.example.jobsity.index

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.JobsityApplication
import com.example.jobsity.R
import com.example.jobsity.db.FavoritesViewModel
import com.example.jobsity.db.FavoritesViewModelFactory


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class IndexFragment : Fragment() {

    private val viewModel: IndexViewModel by viewModels()

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory((context?.applicationContext as JobsityApplication).repository)
    }

    private lateinit var recyclerIndex: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_index, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recycler view variables
        recyclerIndex = view.findViewById(R.id.main_recycler_view)
        var loading = true
        var pastVisibleItems: Int
        var visibleItemCount: Int
        var totalItemCount = 0
        val mLayoutManager = LinearLayoutManager(requireContext())
        recyclerIndex.layoutManager = mLayoutManager

        //Search field
        val searchField = view.findViewById<EditText>(R.id.search_field_value)

        //Search button
        val searchButton: Button = view.findViewById(R.id.button_search)

        //Search flag
        var searchFlag: Boolean = false

        //Search data from search API. If none, return full index
        //Using button
        searchButton.setOnClickListener {
            view.hideKeyboard()
            if (searchField.text.toString() == "") {
                viewModel.showIndexData.clear()
                viewModel._indexPage = 0
                searchFlag = false
                viewModel.getShowIndex(viewModel.indexPage())
            } else {
                searchFlag = true
                viewModel.getShowNames(searchField.text.toString())
            }
        }


        //Using enter key
        searchField.setOnKeyListener( View.OnKeyListener { _, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            {
                if (searchField.text.toString() == "") {
                    viewModel.showIndexData.clear()
                    viewModel._indexPage = 0
                    searchFlag = false
                    viewModel.getShowIndex(viewModel.indexPage())
                    return@OnKeyListener true
                } else {
                    searchFlag = true
                    viewModel.getShowNames(searchField.text.toString())
                    return@OnKeyListener true
                }
            }
            false
        })

        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        recyclerIndex.visibility = View.INVISIBLE

        //Observe data
        viewModel.showIndexStatus.observe(viewLifecycleOwner, {
            favoritesViewModel.getIdFavorites.observe(viewLifecycleOwner, {
                //Create adapter with info from API and ROOM (Favorites)
                //Hide progressbar
                view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                recyclerIndex.visibility = View.VISIBLE
                //Save current recyclerview state
                val recyclerViewState: Parcelable? = mLayoutManager.onSaveInstanceState()
                recyclerIndex.adapter = IndexAdapter(viewModel.showIndexData, it) { dataset ->
                    //Listening to clickListener from the adapter, and update favorites
                    favoritesViewModel.updateFavorite(dataset)
                }
                //Restore recyclerview state
                mLayoutManager.onRestoreInstanceState(recyclerViewState)
            })
        })

        //Listen to recyclerview scroll, and add more data at the end
        recyclerIndex.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!searchFlag) {
                    if (dy > 0) { //check for scroll down
                        visibleItemCount = mLayoutManager.childCount
                        totalItemCount = mLayoutManager.itemCount
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                        if (loading) {
                            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                loading = false
                                viewModel.indexPageIncrease()
                                viewModel.getShowIndex(viewModel.indexPage())
                                loading = true
                            }
                        }
                    }
                }
            }
        })


    }

    //Function to Hide Keyboard when search is done
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}