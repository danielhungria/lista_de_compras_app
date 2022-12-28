package br.com.cadealista.listinha.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.databinding.ActivityMainBinding
import br.com.cadealista.listinha.models.ExportedList
import br.com.cadealista.listinha.util.Utils
import br.com.cadealista.listinha.viewmodel.ScreenListViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.io.StringReader
import java.nio.charset.StandardCharsets


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModelScreenList: ScreenListViewModel by viewModels()


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        when (intent.action) {
            Intent.ACTION_SEND -> {
                viewModelScreenList.checkHasDataToImport(this, intent)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}