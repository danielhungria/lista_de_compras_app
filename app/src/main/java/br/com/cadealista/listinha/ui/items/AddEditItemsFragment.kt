package br.com.cadealista.listinha.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.constants.Constants.ITEM_TO_EDIT
import br.com.cadealista.listinha.constants.Constants.SCREEN_LIST_ID
import br.com.cadealista.listinha.databinding.FragmentAddEditItemBinding
import br.com.cadealista.listinha.extensions.toast
import br.com.cadealista.listinha.models.Item
import br.com.cadealista.listinha.viewmodel.AddEditItemsViewModel
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

    private fun nameFocusListener() = with(binding) {
        editTextNameEdit.setOnFocusChangeListener { _, focused ->
            if (!focused){
                inputEditTextNameEdit.helperText = validName()
                if (validName()!=null){
                    inputEditTextNameEdit.error = getString(R.string.insert_name_helper)
                }else inputEditTextNameEdit.error = null
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