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

    // This property is only valid between onCreateView and
    // onDestroyView.

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_index, container, false)

        recyclerIndex = root.findViewById(R.id.main_recycler_view)

        recyclerIndex.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                viewModel.indexPageIncrease()
                viewModel.getShowIndex(viewModel.indexPage())
            }

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

        val searchButton: Button = view.findViewById(R.id.button_search)

        searchButton.setOnClickListener {
            val searchField = view.findViewById<EditText>(R.id.search_field_value).text.toString()
            if (searchField == "") {
                viewModel.getShowIndex(viewModel.indexPage())
            } else {
                viewModel.getShowNames(searchField)
            }
        }

        viewModel.showIndexLiveData.observe(viewLifecycleOwner, { record ->
            favoritesViewModel.getIdFavorites.observe(viewLifecycleOwner, {
                recyclerIndex.adapter = IndexAdapter(record, it) { dataset ->
                    favoritesViewModel.updateFavorite(dataset)
                }
            })
        })


    }

}



