package com.example.jobsity.index

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.size
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_index, container, false)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerIndex = view.findViewById(R.id.main_recycler_view)

        var loading = true
        var pastVisiblesItems = 0
        var visibleItemCount = 0
        var totalItemCount = 0

        val mLayoutManager = LinearLayoutManager(requireContext())

        recyclerIndex.layoutManager = mLayoutManager

        val searchField = view.findViewById<EditText>(R.id.search_field_value)

        //Set SWIPE actions
        recyclerIndex.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            @SuppressLint("ClickableViewAccessibility")
            //Swipe left will increase pages
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                view.findViewById<RecyclerView>(R.id.main_recycler_view).visibility = View.INVISIBLE
                viewModel.indexPageIncrease()
                viewModel.getShowIndex(viewModel.indexPage())
            }

            //Swipe right, will decrease, until 0
            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeRight() {
                super.onSwipeRight()
                if (viewModel.indexPage() > 0) {
                    view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                    view.findViewById<RecyclerView>(R.id.main_recycler_view).visibility = View.INVISIBLE
                    viewModel.indexPageDecrease()
                    viewModel.getShowIndex(viewModel.indexPage())
                }
            }
        })

        //Search button
        val searchButton: Button = view.findViewById(R.id.button_search)



        //Search data from search API. If none, return full index
        //Using button
        searchButton.setOnClickListener {
            view.hideKeyboard()
            if (searchField.text.toString() == "") {
                viewModel.getShowIndex(viewModel.indexPage())
            } else {
                viewModel.getShowNames(searchField.text.toString())
            }
        }


        //Using enter key
        searchField.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            {
                Log.d("ret", "enter")
                viewModel.getShowNames(searchField.text.toString())
                return@OnKeyListener true
            }
            false
        })

        //Observe data
        viewModel.showIndexStatus.observe(viewLifecycleOwner, { record ->
            favoritesViewModel.getIdFavorites.observe(viewLifecycleOwner, {
                //Create adapter with info from API and ROOM (Favorites)
                view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                view.findViewById<RecyclerView>(R.id.main_recycler_view).visibility = View.VISIBLE
                recyclerIndex.adapter = IndexAdapter(viewModel.showIndexData, it) { dataset ->
                    //Listening to clickListener from the adapter, and update favorites
                    favoritesViewModel.updateFavorite(dataset)
                }
                recyclerIndex.scrollToPosition(totalItemCount - 2)
            })
        })

        recyclerIndex.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            Log.d("ret", "Last Item Wow !")
                            viewModel.indexPageIncrease()
                            viewModel.getShowIndex(viewModel.indexPage())
                            loading = true
                        }
                    }
                }
            }
        })


    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}



