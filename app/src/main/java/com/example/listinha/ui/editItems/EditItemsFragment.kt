package com.example.listinha.ui.editItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listinha.databinding.FragmentEditItemBinding
import com.example.listinha.viewmodel.EditItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditItemsFragment: Fragment() {

    private lateinit var binding: FragmentEditItemBinding

    private val editItemViewModel: EditItemsViewModel by viewModels()

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

}