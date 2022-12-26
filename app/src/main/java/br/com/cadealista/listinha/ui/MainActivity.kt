package br.com.cadealista.listinha.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.databinding.ActivityMainBinding
import br.com.cadealista.listinha.util.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        checkHasDataToImport()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkHasDataToImport() {
        try {
            intent?.run {
                data?.let { uri -> Utils.getExportDataFileContent(uri) }
            }
        } catch (e: Exception) {
            Log.d("MainActivity", "Cannot import data")
        }
    }
}