package br.com.cadealista.listinha.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.databinding.ActivityMainBinding
import br.com.cadealista.listinha.models.ExportedList
import br.com.cadealista.listinha.util.Utils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.io.StringReader
import java.nio.charset.StandardCharsets


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
                val bufferedReader =
                    contentResolver.openInputStream(
                        intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri
                    )?.readBytes()
                val readText = StringReader(bufferedReader?.let {
                    String(
                        it,
                        StandardCharsets.UTF_8
                    )
                }).readText()
                Log.i("Main", "checkHasDataToImport: $readText")
                Toast.makeText(this@MainActivity, readText, Toast.LENGTH_LONG).show()
                //teste
                val fromJson = Gson().fromJson(readText.trim(), ExportedList::class.java)

            }

        } catch (e: Exception) {
            Log.d("MainActivity", "Cannot import data")
        }
    }
}