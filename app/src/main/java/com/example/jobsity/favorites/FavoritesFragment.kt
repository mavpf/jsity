package com.example.jobsity.favorites

import android.content.Context
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.JobsityApplication
import com.example.jobsity.R
import com.example.jobsity.db.FavoritesViewModel
import com.example.jobsity.db.FavoritesViewModelFactory
import com.example.jobsity.index.IndexAdapter

class FavoritesFragment : Fragment() {

    //Viewmodel for ROOM
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory((context?.applicationContext as JobsityApplication).repository)
    }

    private lateinit var recyclerIndex: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_index, container, false)

        recyclerIndex = root.findViewById(R.id.main_recycler_view)

        //Get all information from ROOM
        viewModel.getAllFavorites

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE

        val searchButton = view.findViewById<Button>(R.id.button_search)
        val searchField = view.findViewById<EditText>(R.id.search_field_value)

        //Load all favorites
        viewModel.getAllFavorites.observe(viewLifecycleOwner, { record ->
            viewModel.getIdFavorites.observe(viewLifecycleOwner, { favoriteId ->
                recyclerIndex.adapter =
                    IndexAdapter(viewModel.transformToShowIndex(record), favoriteId) {
                        viewModel.updateFavorite(it)
                    }
            })
        })

        //Check if search button clicked
        searchButton.setOnClickListener {
            view.hideKeyboard()
            getFavorites(searchField)
        }

        //Using enter key
        searchField.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            {
                getFavorites(searchField)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getFavorites(searchField: EditText){
        if (searchField.text.toString() == "") {
            viewModel.getAllFavorites.observe(viewLifecycleOwner, { record ->
                viewModel.getIdFavorites.observe(viewLifecycleOwner, { favoriteId ->
                    recyclerIndex.adapter =
                        IndexAdapter(viewModel.transformToShowIndex(record), favoriteId) {
                            viewModel.updateFavorite(it)
                        }
                })
            })
        } else {
            //With value, get select info
            val searchName = "%"+searchField.text.toString()+"%"
            viewModel.getNameFavorites(searchName).observe(viewLifecycleOwner, { record ->
                viewModel.getIdFavorites.observe(viewLifecycleOwner, { favoriteId ->
                    recyclerIndex.adapter =
                        IndexAdapter(viewModel.transformToShowIndex(record), favoriteId) {
                            viewModel.updateFavorite(it)
                        }
                })
            })
        }
    }
}

