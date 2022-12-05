package com.example.listinha.ui.deleteallcompleteditems

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.listinha.viewmodel.DeleteAllCompletedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment(){

    private val viewModel: DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Do you really want to delete all completed items?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes"){_, _ ->
            //call viewmodel
            viewModel.onConfirmClick()
            }
            .create()
}