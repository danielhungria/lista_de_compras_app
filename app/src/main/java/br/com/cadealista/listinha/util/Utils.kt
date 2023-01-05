package br.com.cadealista.listinha.util

import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

object Utils {

    fun getExportDataFileContent(exportDataUri: Uri?) {
        exportDataUri?.let { uri ->
            uri.path?.let {
                val fileReader =
                    FileReader(Environment.getExternalStorageDirectory().absolutePath +
                            File.separator + it.split(":")[1]
                )
                val bufferedReader = BufferedReader(fileReader)
                val text = StringBuilder()
                bufferedReader.forEachLine { line ->
                    text.append(line)
                    text.append("\n")
                }
                Log.i("Import", "getExportDataFileContent: $text")
            }
        }
    }

}