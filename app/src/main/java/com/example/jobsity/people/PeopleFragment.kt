package com.example.jobsity.people

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
import com.example.jobsity.R

class PeopleFragment: Fragment() {

    private val viewModel: PeopleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_index, container, false)

        root.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerPeople = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        val searchButton = view.findViewById<Button>(R.id.button_search)
        val searchText = view.findViewById<EditText>(R.id.search_field_value)

        searchButton.setOnClickListener {
            view.hideKeyboard()
            viewModel.searchPerson(searchText.text.toString())
        }

        searchText.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
            view.hideKeyboard()
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action ==KeyEvent.ACTION_UP)
            {
                viewModel.searchPerson(searchText.text.toString())
                return@OnKeyListener true
            }
            false
        })

        viewModel.personData.observe(viewLifecycleOwner, {
            recyclerPeople.visibility = View.VISIBLE
            recyclerPeople.adapter = PeopleAdapter(it)
        })

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}