package com.example.listinha.ui.listScreen

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.listinha.R
import com.example.listinha.constants.Constants.SCREEN_LIST_TO_EDIT
import com.example.listinha.databinding.FragmentListScreenBinding
import com.example.listinha.extensions.navigateTo
import com.example.listinha.viewmodel.ScreenListViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenListFragment: Fragment() {

    private val viewModel:  ScreenListViewModel by viewModels()

    private lateinit var binding: FragmentListScreenBinding

    lateinit var mAdView: AdView

    private val screenListAdapter = ScreenListAdapter(onClick={
        navigateTo(
            R.id.action_screenListFragment_to_itemFragment,
            bundleOf(SCREEN_LIST_TO_EDIT to it)
        )}, longPress = {
            navigateTo(R.id.action_screenListFragment_to_screenListAddEditFragment2,
            bundleOf(SCREEN_LIST_TO_EDIT to it))
    }, longPressDelete = {screenList ->
        viewModel.delete(screenList)
    })

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
        setupItemTouchHelper()
        viewModel.screenLists.observe(viewLifecycleOwner){
            screenListAdapter.updateList(it)
        }
        viewModel.fetchScreenList()
        context?.let { MobileAds.initialize(it) }
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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

    private fun setupItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = screenListAdapter.currentList[viewHolder.bindingAdapterPosition]
                viewModel.onItemSwiped(item)
            }

        }).attachToRecyclerView(binding.recyclerViewScreenList)
    }

}