package com.example.listinha.ui.items

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listinha.R
import com.example.listinha.databinding.FragmentListBinding
import com.example.listinha.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemFragment: Fragment() {

    private val viewModel: ItemViewModel by viewModels()

    private lateinit var binding: FragmentListBinding

    private val itemAdapter = ItemAdapter(onComplete = {completed,item ->
        viewModel.onComplete(completed, item)
    }, onSearchBy = {
        viewModel.onSearchBy(it)
    })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        viewModel.items.observe(viewLifecycleOwner){
            itemAdapter.updateList(it)
        }
        viewModel.fetchItemList()
        setupMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fragment_item, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as SearchView
                setupSearchView(searchView)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort -> {
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                itemAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupFab() {
        binding.fabAddList.setOnClickListener {
            findNavController().navigate(R.id.action_itemFragment_to_editItemsFragment)
        }
    }

    private fun setupRecyclerView(){
        binding.apply {
            recyclerViewList.apply {
                adapter = itemAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }



}