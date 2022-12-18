package br.com.cadealista.listinha.ui.items

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.components.GenericDialog
import br.com.cadealista.listinha.constants.Constants.ITEM_TO_EDIT
import br.com.cadealista.listinha.constants.Constants.SCREEN_LIST_ID
import br.com.cadealista.listinha.constants.Constants.SCREEN_LIST_TO_EDIT
import br.com.cadealista.listinha.databinding.FragmentListBinding
import br.com.cadealista.listinha.extensions.navigateTo
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.viewmodel.ItemViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemFragment : Fragment() {

    private lateinit var mAdView: AdView

    private val viewModel: ItemViewModel by viewModels()

    private lateinit var binding: FragmentListBinding

    private val screenList by lazy { arguments?.getParcelable<ScreenList>(SCREEN_LIST_TO_EDIT) }

    private val itemAdapter = ItemAdapter(onComplete = { completed, item ->
        viewModel.onComplete(completed, item)
    }, onClick = {
        navigateTo(
            R.id.action_itemFragment_to_editItemsFragment, bundleOf(
            ITEM_TO_EDIT to it,
            SCREEN_LIST_ID to screenList?.id
        ))
    }, onClickDelete = { item ->
        viewModel.delete(item)
    }
    )

    private fun setupMenu() {
        binding.toolbar.title = screenList?.name
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_search -> {
                    val searchView = it?.actionView as SearchView
                    setupSearchView(searchView)
                    true
                }
                R.id.action_delete_all_completed_item -> {
                    showDeleteAllItemsCompletedDialog()
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
//        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.menu_fragment_item, menu)
//                val searchItem = menu.findItem(R.id.action_search)
//                val searchView = searchItem?.actionView as SearchView
//                setupSearchView(searchView)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                return when (menuItem.itemId) {
//                    R.id.action_delete_all_completed_item -> {
//                        showDeleteAllItemsCompletedDialog()
//                        true
//                    }
//                    else -> false
//                }
//            }
//
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
            navigateTo(R.id.action_itemFragment_to_editItemsFragment, bundleOf(SCREEN_LIST_ID to screenList?.id))
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

    private fun setupTotalMarketPrice() {
        binding.textviewTotalValueCardView.text = viewModel.getTotalMarketPrice()
    }

    private fun showDeleteAllItemsCompletedDialog() {
        context?.let {
            GenericDialog.Builder(it)
                .setTitle(getString(R.string.delete_all_items_completed_dialog_title))
                .setBodyMessage(getString(R.string.delete_all_items_completed_dialog_body_message))
                .setNegativeButtonText(getString(R.string.cancel))
                .setPositiveButtonText(getString(R.string.yes))
                .setOnPositiveButtonClickListener {
                    viewModel.deleteAllItemsCompleted()
                }
                .build()
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
                val item = itemAdapter.currentList[viewHolder.bindingAdapterPosition]
                viewModel.onItemSwiped(item)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                setDeleteIcon(c, viewHolder, dX, dY, actionState, isCurrentlyActive)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }).attachToRecyclerView(binding.recyclerViewList)
    }

    private fun setDeleteIcon(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        currentlyActive: Boolean
    ) {
        val mClearPaint = Paint()
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        val mBackground = ColorDrawable()
        val backgroundColor = Color.parseColor("#b80f0a")
        val deleteDrawable = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_baseline_delete_24) }
        val intrinsicWidth = deleteDrawable!!.intrinsicWidth
        val intrinsicHeight = deleteDrawable!!.intrinsicHeight
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height
        val isCancelled = dX == 0f && !currentlyActive
        if (isCancelled){
            c.drawRect(
                itemView.right + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat(), mClearPaint
            )
            return
        }
        mBackground.color = backgroundColor
        mBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        mBackground.draw(c)
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight)/2
        val deleteIconMargin = (itemHeight - intrinsicHeight)/2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight
        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable.draw(c)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        setupMenu()
        setupItemTouchHelper()

        viewModel.items.observe(viewLifecycleOwner) {
            setupTotalMarketPrice()
            itemAdapter.updateList(it)
        }
        screenList?.id?.let { viewModel.setup(it) }
        viewModel.fetchItemList()

        context?.let { MobileAds.initialize(it) }
        mAdView = binding.adViewItemList
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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