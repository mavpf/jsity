package com.example.jobsity.ui.people

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobsity.databinding.FragmentIndexBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private val viewModel: PeopleViewModel by viewModels()

    private var _binding: FragmentIndexBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val root = inflater.inflate(R.layout.fragment_index, container, false)
        _binding = FragmentIndexBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.progressBar.visibility = View.INVISIBLE

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSearch.setOnClickListener {
            view.hideKeyboard()
            viewModel.searchPerson(binding.searchFieldValue.text.toString())
        }

        binding.searchFieldValue.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.searchPerson(binding.searchFieldValue.text.toString())
                return@OnKeyListener true
            }
            false
        })

        viewModel.personData.observe(viewLifecycleOwner, {
            binding.mainRecyclerView.visibility = View.VISIBLE
            binding.mainRecyclerView.adapter = PeopleAdapter(it)
        })

    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}