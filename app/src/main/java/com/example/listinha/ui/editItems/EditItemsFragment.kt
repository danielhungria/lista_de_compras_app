package com.example.listinha.ui.editItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listinha.constants.Constants.ITEM_TO_EDIT
import com.example.listinha.databinding.FragmentEditItemBinding
import com.example.listinha.models.Item
import com.example.listinha.viewmodel.EditItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditItemsFragment: Fragment() {

    private lateinit var binding: FragmentEditItemBinding

    private val editItemViewModel: EditItemsViewModel by viewModels()

    private val itemToEdit by lazy {  arguments?.getParcelable<Item>(ITEM_TO_EDIT)  }

    private val viewModel: EditItemsViewModel by viewModels()

    private fun setupListener() {
        binding.fabSaveList.setOnClickListener { _ ->
            with(binding){
                itemToEdit?.let {
                    viewModel.onSaveEventEdit(
                        id = it.id,
                        name = editTextNameEdit.text.toString(),
                        price = editTextPriceEdit.text.toString(),
                        quantity = editTextQuantityEdit.text.toString(),
                        closeScreen = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }

        }
    }

    private fun setupAccordingToEditMode(item: Item?) = with(binding) {
        item?.run {
            binding.textViewTitleEdit.text = "Edit Item"
            Toast.makeText(context, "${item.id}", Toast.LENGTH_LONG).show()
            editTextNameEdit.setText(item.name)
            editTextQuantityEdit.setText(item.quantity)
            editTextPriceEdit.setText(item.price)
        }
    }

    private fun setupSaveFab() = with(binding) {
        fabSaveList.setOnClickListener {
            editItemViewModel.onSaveEvent(
                name = editTextNameEdit.text.toString(),
                price = editTextPriceEdit.text.toString(),
                quantity = editTextQuantityEdit.text.toString(),
                closeScreen = {
                    findNavController().popBackStack()
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSaveFab()
        setupAccordingToEditMode(itemToEdit)
        setupListener()
    }


}