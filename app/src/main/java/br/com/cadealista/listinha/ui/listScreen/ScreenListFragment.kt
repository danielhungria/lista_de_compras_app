package br.com.cadealista.listinha.ui.listScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.cadealista.listinha.BuildConfig
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.constants.Constants.SCREEN_LIST_TO_EDIT
import br.com.cadealista.listinha.databinding.FragmentListScreenBinding
import br.com.cadealista.listinha.extensions.navigateTo
import br.com.cadealista.listinha.viewmodel.ScreenListViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ScreenListFragment : Fragment() {

    private val viewModel: ScreenListViewModel by viewModels()

    private lateinit var binding: FragmentListScreenBinding

    private lateinit var mAdView: AdView

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
    }, sharePress = { screenListId ->
            viewModel.exportData(
                screenListId,
                onSuccess = { file ->
                    handleText(file)
                }, onError = {
                    activity?.runOnUiThread{
                        Toast.makeText(context, "ERRO AO EXPORTAR DADO", Toast.LENGTH_LONG).show()
                    }

                }, context = requireContext()
            )
            Log.i("Fragment", "pressionado compartilhar")
        }
    )

    private fun handleText(file: File) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, context?.let {
                FileProvider.getUriForFile(
                    it,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file
                )
            }
            )
        }
        startActivity(Intent.createChooser(sendIntent, "Sharing"))
    }

    private fun configureAd() {
        mAdView = binding.adViewScreenList
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun setPermissions() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let { ActivityCompat.requestPermissions(it, permissions, 100) }
            }
        }
    }

    private fun deleteFilesAfterExport() {
        val directory = File(
            context?.filesDir, "lists"
        )
        directory.deleteRecursively()
    }

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
        setPermissions()
        setupRecyclerViewScreenList()
        setupFab()
        setupItemTouchHelper()
        configureAd()
        deleteFilesAfterExport()
        context?.let { MobileAds.initialize(it) }
        viewModel.screenLists.observe(viewLifecycleOwner) {
            screenListAdapter.updateList(it)
            if (!viewModel.checkList()) {
                binding.iconBackgroundScreenlist.alpha = 0F
                binding.textBackgroundScreenlist.alpha = 0F
            }else{
                binding.iconBackgroundScreenlist.alpha = 0.3F
                binding.textBackgroundScreenlist.alpha = 0.3F
            }
        }
        viewModel.fetchScreenList()

    }

}