package com.example.listinha.ui.items

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listinha.R
import com.example.listinha.databinding.FragmentListBinding
import com.example.listinha.models.Item
import com.example.listinha.ui.deleteallcompleteditems.DeleteAllCompletedDialogFragment
import com.example.listinha.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemFragment : Fragment() {

    private val listItems = mutableListOf<Item>()

    private val viewModel: ItemViewModel by viewModels()

    private lateinit var binding: FragmentListBinding

    private val itemAdapter = ItemAdapter(onComplete = { completed, item ->
        viewModel.onComplete(completed, item)
    })

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
                    R.id.action_delete_all_completed_item -> {
                        setupDeleteAllCompletedDialog()
                        true
                    }else -> false
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

    private fun setupRecyclerView() {
        binding.apply {
            recyclerViewList.apply {
                adapter = itemAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setupDeleteAllCompletedDialog() {
        findNavController().navigate(R.id.action_global_deleteAllCompletedDialogFragment)
    }

    fun sumItems(item: List<Item>) {
        listItems.clear()
        item.forEach {
            val price = it.price.toDoubleOrNull()
            val quantity = it.quantity.toDoubleOrNull()
            if (price != null && quantity != null) {
                listItems.add(it)
            }
        }
        val sumAllItems = listItems.sumOf {
            it.price.toDouble() * it.quantity.toDouble()
        }
        val sumAllItemsFormated = String.format("%.2f", sumAllItems)
        binding.textviewTotalValueCardView.text = "R$ $sumAllItemsFormated"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
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
                val item = itemAdapter.currentList[viewHolder.bindingAdapterPosition]
                viewModel.onItemSwiped(item)
            }

        }).attachToRecyclerView(binding.recyclerViewList)

        viewModel.items.observe(viewLifecycleOwner) {
            sumItems(it)
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
}