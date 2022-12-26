package br.com.cadealista.listinha.ui.listScreen

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.constants.Constants.SCREEN_LIST_TO_EDIT
import br.com.cadealista.listinha.databinding.FragmentListScreenBinding
import br.com.cadealista.listinha.extensions.navigateTo
import br.com.cadealista.listinha.viewmodel.ScreenListViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest

@AndroidEntryPoint
class ScreenListFragment : Fragment() {

    private val viewModel: ScreenListViewModel by viewModels()

    private lateinit var binding: FragmentListScreenBinding

    private lateinit var mAdView: AdView

//    private var booleanCreatedList: Boolean = true

    private val screenListAdapter = ScreenListAdapter(onClick = {
        navigateTo(
            R.id.action_screenListFragment_to_itemFragment,
            bundleOf(SCREEN_LIST_TO_EDIT to it)
        )
    }, longPress = {
        navigateTo(
            R.id.action_screenListFragment_to_screenListAddEditFragment2,
            bundleOf(SCREEN_LIST_TO_EDIT to it)
        )
    }, longPressDelete = { screenList ->
        viewModel.delete(screenList)
    },
        sharePress = {
        viewModel.exportData(it)
            Log.i("Fragment", "pressionado compartilhar")
    }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (context?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                 activity?.let { ActivityCompat.requestPermissions(it,permissions,100) }
             }
            } else {
                TODO("VERSION.SDK_INT < M")
            }

        setupRecyclerViewScreenList()
        setupFab()
//        setupFabRecovery()
        setupItemTouchHelper()
//        createDefaultList(booleanCreatedList)
        viewModel.screenLists.observe(viewLifecycleOwner) {
            screenListAdapter.updateList(it)
        }
        viewModel.exportedData.observe(viewLifecycleOwner){
            Log.i("Fragment", "onViewCreated: $it")
        }
        viewModel.fetchScreenList()
        context?.let { MobileAds.initialize(it) }
        mAdView = binding.adViewScreenList
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

//    private fun setupFabRecovery() {
//        binding.resgateLista.setOnClickListener {
//            booleanCreatedList = true
//        }
//    }

//    private fun createDefaultList(boolean: Boolean) {
//        if (boolean){
//            viewModel.insertExampleList()
//            booleanCreatedList = false
//        }
//    }

    private fun setupFab() {
        binding.fabAddListScreen.setOnClickListener {
            navigateTo(R.id.action_screenListFragment_to_screenListAddEditFragment2)
        }
    }

    private fun setupRecyclerViewScreenList() {
        binding.apply {
            recyclerViewScreenList.apply {
                adapter = screenListAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
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