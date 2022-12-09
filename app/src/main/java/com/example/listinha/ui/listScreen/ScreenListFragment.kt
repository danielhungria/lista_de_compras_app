package com.example.listinha.ui.listScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.listinha.R
import com.example.listinha.constants.Constants.SCREEN_LIST_TO_EDIT
import com.example.listinha.databinding.FragmentListScreenBinding
import com.example.listinha.extensions.navigateTo
import com.example.listinha.viewmodel.ScreenListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenListFragment: Fragment() {

    private val viewModel:  ScreenListViewModel by viewModels()

    private lateinit var binding: FragmentListScreenBinding

    private val screenListAdapter = ScreenListAdapter(onClick={
        navigateTo(
            R.id.action_screenListFragment_to_itemFragment,
            bundleOf(SCREEN_LIST_TO_EDIT to it)
        )})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewScreenList()
        setupFab()
        viewModel.screenLists.observe(viewLifecycleOwner){
            screenListAdapter.updateList(it)
        }
        viewModel.fetchScreenList()
    }

    private fun setupFab() {
        binding.fabAddListScreen.setOnClickListener {
            navigateTo(R.id.action_screenListFragment_to_screenListAddEditFragment2)
        }
    }

    private fun setupRecyclerViewScreenList() {
        binding.apply {
            recyclerViewScreenList.apply {
                adapter = screenListAdapter
                layoutManager = GridLayoutManager(requireContext(),2)
            }
        }
    }

}