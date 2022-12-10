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
import com.example.listinha.models.ScreenList
import com.example.listinha.viewmodel.AddEditScreenListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditScreenListFragment : Fragment(){

    private lateinit var binding: FragmentAddEditScreenListBinding

    private val viewModel: AddEditScreenListViewModel by viewModels()

    private val screenListToEdit by lazy { arguments?.getParcelable<ScreenList>(SCREEN_LIST_TO_EDIT) }

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
        setupAccordingToEditMode(screenListToEdit)
        setupListener()
        setupMenu()
    }

    private fun setupAccordingToEditMode(screenList: ScreenList?) = with(binding) {
        screenList?.run {
            viewModel.setupEditMode(id)
            editTextNameScreenAddEditList.setText(name)
            editTextDescriptionScreenAddEditList.setText(description)
            toolbarListScreenEdit.title = getString(R.string.toolbar_title_edit)
            textViewTitleAddEditList.setText(getString(R.string.textview_title_edit_list))
            textViewDescriptionAddEditList.setText(getString(R.string.textview_description_edit))
        }
    }

    private fun setupListener() {
        with(binding){
            fabSaveScreenAddEditList.setOnClickListener {
                viewModel.onSaveEvent(
                    name = editTextNameScreenAddEditList.text.toString(),
                    description = editTextDescriptionScreenAddEditList.text.toString(),
                    closeScreen = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }
    private fun setupMenu() {
        binding.toolbarListScreenEdit.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}