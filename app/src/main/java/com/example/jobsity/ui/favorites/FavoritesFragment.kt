package com.example.jobsity.ui.favorites

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.JobsityApplication
import com.example.jobsity.databinding.FragmentIndexBinding
import com.example.jobsity.ui.index.IndexAdapter

class FavoritesFragment : Fragment() {

    //viewModel for ROOM
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory((context?.applicationContext as JobsityApplication).repository)
    }

    private var _binding: FragmentIndexBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val root = inflater.inflate(R.layout.fragment_index, container, false)
        _binding = FragmentIndexBinding.inflate(inflater, container, false)
        val view = binding.root

        //Get all information from ROOM
        viewModel.getAllFavorites

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.GONE

        mLayoutManager = LinearLayoutManager(requireContext())
        binding.mainRecyclerView.layoutManager = mLayoutManager

        //Load all favorites
        viewModel.getAllFavorites.observe(viewLifecycleOwner, { record ->
            viewModel.getIdFavorites.observe(viewLifecycleOwner, { favoriteId ->
                //Save current recyclerview state
                val recyclerViewState: Parcelable? = mLayoutManager.onSaveInstanceState()
                binding.mainRecyclerView.adapter =
                    IndexAdapter(viewModel.transformToShowIndex(record), favoriteId) {
                        viewModel.updateFavorite(it)
                    }
                //Restore recyclerview state
                mLayoutManager.onRestoreInstanceState(recyclerViewState)
            })
        })

        //Check if search button clicked
        binding.buttonSearch.setOnClickListener {
            view.hideKeyboard()
            getFavorites(binding.searchFieldValue)
        }

        //Using enter key
        binding.searchFieldValue.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                getFavorites(binding.searchFieldValue)
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getFavorites(searchField: EditText) {
        if (binding.searchFieldValue.text.toString() == "") {
            viewModel.getAllFavorites.observe(viewLifecycleOwner, { record ->
                viewModel.getIdFavorites.observe(viewLifecycleOwner, { favoriteId ->
                    val recyclerViewState: Parcelable? = mLayoutManager.onSaveInstanceState()
                    binding.mainRecyclerView.adapter =
                        IndexAdapter(viewModel.transformToShowIndex(record), favoriteId) {
                            viewModel.updateFavorite(it)
                        }
                    mLayoutManager.onRestoreInstanceState(recyclerViewState)
                })
            })
        } else {
            //With value, get select info
            val searchName = "%" + searchField.text.toString() + "%"
            viewModel.getNameFavorites(searchName).observe(viewLifecycleOwner, { record ->
                viewModel.getIdFavorites.observe(viewLifecycleOwner, { favoriteId ->
                    val recyclerViewState: Parcelable? = mLayoutManager.onSaveInstanceState()
                    binding.mainRecyclerView.adapter =
                        IndexAdapter(viewModel.transformToShowIndex(record), favoriteId) {
                            viewModel.updateFavorite(it)
                        }
                    mLayoutManager.onRestoreInstanceState(recyclerViewState)
                })
            })
        }
    }
}

