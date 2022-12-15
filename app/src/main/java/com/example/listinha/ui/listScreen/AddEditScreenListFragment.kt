package com.example.listinha.ui.listScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listinha.R
import com.example.listinha.constants.Constants.SCREEN_LIST_TO_EDIT
import com.example.listinha.databinding.FragmentAddEditScreenListBinding
import com.example.listinha.extensions.toast
import com.example.listinha.models.ScreenList
import com.example.listinha.viewmodel.AddEditScreenListViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditScreenListFragment : Fragment(){

    private lateinit var binding: FragmentAddEditScreenListBinding

    private val viewModel: AddEditScreenListViewModel by viewModels()

    private val screenListToEdit by lazy { arguments?.getParcelable<ScreenList>(SCREEN_LIST_TO_EDIT) }

    private lateinit var mAdView: AdView


    private fun nameFocusListener() = with(binding) {
        editTextNameScreenAddEditList.setOnFocusChangeListener { _, focused ->
            if (!focused){
                textInputNameScreenAddEditList.helperText = validName()
                if (validName()!=null){
                    textInputNameScreenAddEditList.error = getString(R.string.insert_name_helper)
                }else textInputNameScreenAddEditList.error = null
            }

        }

    }

    private fun validName(): String? {
        val editTextName = binding.editTextNameScreenAddEditList.text.toString()
        if (editTextName.isBlank()){
            return getString(R.string.insert_name_helper)
        }
        return null
    }

    private fun setupAccordingToEditMode(screenList: ScreenList?) = with(binding) {
        screenList?.run {
            viewModel.setupEditMode(id)
            editTextNameScreenAddEditList.setText(name)
            editTextDescriptionScreenAddEditList.setText(description)
            toolbarListScreenEdit.title = getString(R.string.toolbar_title_edit_list)
            textViewTitleAddEditList.setText("")
        }
    }

    private fun setupListener() {
        with(binding){
            fabSaveScreenAddEditList.setOnClickListener {
                if(validName()==null){
                    viewModel.onSaveEvent(
                        name = editTextNameScreenAddEditList.text.toString(),
                        description = editTextDescriptionScreenAddEditList.text.toString(),
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
    private fun setupMenu() {
        binding.toolbarListScreenEdit.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditScreenListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameFocusListener()
        setupAccordingToEditMode(screenListToEdit)
        setupListener()
        setupMenu()
        context?.let { MobileAds.initialize(it) }
        mAdView = binding.adViewScreenListAddEdit
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}