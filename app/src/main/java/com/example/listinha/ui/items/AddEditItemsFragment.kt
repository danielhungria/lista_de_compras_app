package com.example.listinha.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listinha.R
import com.example.listinha.constants.Constants.ITEM_TO_EDIT
import com.example.listinha.constants.Constants.SCREEN_LIST_ID
import com.example.listinha.databinding.FragmentAddEditItemBinding
import com.example.listinha.extensions.toast
import com.example.listinha.models.Item
import com.example.listinha.viewmodel.AddEditItemsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditItemsFragment : Fragment() {

    private lateinit var mAdView: AdView

    private lateinit var binding: FragmentAddEditItemBinding

    private val itemToEdit by lazy { arguments?.getParcelable<Item>(ITEM_TO_EDIT) }

    private val screenListId by lazy { arguments?.getInt(SCREEN_LIST_ID) }

    private val viewModel: AddEditItemsViewModel by viewModels()
    private fun setupListener() {
        with(binding) {
            fabSaveList.setOnClickListener {
                if(validName()==null){
                    viewModel.onSaveEvent(
                        name = editTextNameEdit.text.toString(),
                        price = editTextPriceEdit.text.toString(),
                        quantity = editTextQuantityEdit.text.toString(),
                        idList = screenListId,
                        closeScreen = {
                            findNavController().popBackStack()
                        }
                    )
                }else {
                    context?.toast(getString(R.string.toast_required))
                }
            }
        }
    }


    private fun setupAccordingToEditMode(item: Item?) = with(binding) {
        item?.run {
            viewModel.setupEditMode(id)
            editTextNameEdit.setText(name)
            editTextQuantityEdit.setText(quantity)
            editTextPriceEdit.setText(price)
            toolbarItemEdit.title = getString(R.string.toolbar_title_edit)
//            textViewDescriptionEdit.setText(getString(R.string.textview_description_edit))
//            textViewTitleEdit.setText(getString(R.string.textview_title_edit_item))
        }

    }

    private fun setupMenu() {
        binding.toolbarItemEdit.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun nameFocusListener() {
        binding.editTextNameEdit.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.inputEditTextNameEdit.helperText = validName()
            }
        }
    }

    private fun validName(): String? {
        val editTextName = binding.editTextNameEdit.text.toString()
        if (editTextName.isBlank()){
            return getString(R.string.insert_name_helper)
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameFocusListener()
        setupAccordingToEditMode(itemToEdit)
        setupListener()
        setupMenu()
        context?.let { MobileAds.initialize(it) }
        mAdView = binding.adViewItemListAddEdit
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }



}