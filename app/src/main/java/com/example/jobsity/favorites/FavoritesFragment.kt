package com.example.jobsity.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        val searchButton = view.findViewById<Button>(R.id.button_search)

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

            val searchField = view.findViewById<EditText>(R.id.search_field_value).text.toString()
            //Without value, get all favorites
            if (searchField == "") {
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
                val searchName = "%"+searchField+"%"
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

}

