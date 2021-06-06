package com.example.jobsity.index

import android.annotation.SuppressLint
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
        val root = inflater.inflate(R.layout.fragment_index, container, false)

        recyclerIndex = root.findViewById(R.id.main_recycler_view)

        //Set SWIPE actions
        recyclerIndex.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            @SuppressLint("ClickableViewAccessibility")
            //Swipe left will increase pages
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                viewModel.indexPageIncrease()
                viewModel.getShowIndex(viewModel.indexPage())
            }

            //Swipe right, will decrease, until 0
            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeRight() {
                super.onSwipeRight()
                if (viewModel.indexPage() > 0) {
                    viewModel.indexPageDecrease()
                    viewModel.getShowIndex(viewModel.indexPage())
                }
            }
        })
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Search button
        val searchButton: Button = view.findViewById(R.id.button_search)

        //Search data from search API. If none, return full index
        searchButton.setOnClickListener {
            val searchField = view.findViewById<EditText>(R.id.search_field_value).text.toString()
            if (searchField == "") {
                viewModel.getShowIndex(viewModel.indexPage())
            } else {
                viewModel.getShowNames(searchField)
            }
        }

        //Observe data
        viewModel.showIndexLiveData.observe(viewLifecycleOwner, { record ->
            favoritesViewModel.getIdFavorites.observe(viewLifecycleOwner, {
                //Create adapter with info from API and ROOM (Favorites)
                recyclerIndex.adapter = IndexAdapter(record, it) { dataset ->
                    //Listening to clickListener from the adapter, and update favorites
                    favoritesViewModel.updateFavorite(dataset)
                }
            })
        })


    }

}



