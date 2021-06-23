package com.example.jobsity.ui.index

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsity.JobsityApplication
import com.example.jobsity.databinding.FragmentIndexBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexFragment : Fragment() {

    private val viewModel: IndexViewModel by viewModels()

    //View Binding
    private var _binding: FragmentIndexBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndexBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recycler view variables
        var loading = true
        var pastVisibleItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int
        val mLayoutManager = LinearLayoutManager(requireContext())
        binding.mainRecyclerView.layoutManager = mLayoutManager

        //Search flag
        var searchFlag = false

        binding.progressBar.visibility = View.VISIBLE
        binding.mainRecyclerView.visibility = View.INVISIBLE

        //Search data from search API. If none, return full index
        //Using button
        binding.buttonSearch.setOnClickListener {
            view.hideKeyboard()
            if (binding.searchFieldValue.text.toString() == "") {
                binding.progressBar.visibility = View.VISIBLE
                binding.mainRecyclerView.visibility = View.INVISIBLE
                viewModel.showIndexData.clear()
                viewModel.indexPage = 0
                searchFlag = false
                viewModel.getShowIndex(viewModel.indexPage())
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.mainRecyclerView.visibility = View.INVISIBLE
                searchFlag = true
                viewModel.getShowNames(binding.searchFieldValue.text.toString())
            }
        }


        //Using enter key
        binding.searchFieldValue.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (binding.searchFieldValue.text.toString() == "") {
                    viewModel.showIndexData.clear()
                    viewModel.indexPage = 0
                    searchFlag = false
                    viewModel.getShowIndex(viewModel.indexPage())
                    return@OnKeyListener true
                } else {
                    searchFlag = true
                    viewModel.getShowNames(binding.searchFieldValue.text.toString())
                    return@OnKeyListener true
                }
            }
            false
        })

        //Observe data
        viewModel.showIndexStatus.observe(viewLifecycleOwner, {
            viewModel.getIdFavorites.observe(viewLifecycleOwner, {
                //Create adapter with info from API and ROOM (Favorites)
                //Hide progressbar
                binding.progressBar.visibility = View.GONE
                binding.mainRecyclerView.visibility = View.VISIBLE
                //Save current recyclerview state
                val recyclerViewState: Parcelable? = mLayoutManager.onSaveInstanceState()
                binding.mainRecyclerView.adapter =
                    IndexAdapter(viewModel.showIndexData, it) { dataset ->
                        //Listening to clickListener from the adapter, and update favorites
                        viewModel.updateFavorite(dataset)
                    }
                //Restore recyclerview state
                mLayoutManager.onRestoreInstanceState(recyclerViewState)
            })
        })

        //Listen to recyclerview scroll, and add more data at the end
        binding.mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Function to Hide Keyboard when search is done
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}