package cz.slezadav.hodnoceniProdejen.model

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object Evaluation {

    /**
     * An array of sample (dummy) items.
     */
    val items: MutableList<FormPart> = mutableListOf()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val map: MutableMap<String, FormPart> = HashMap()


    var storeName = ""

    var inspectorName = ""

    var notes: CharSequence = ""

    fun reset(context: Context) {
        items.clear()
        map.clear()
        notes = ""
        readInput(context)
    }

    fun readInput(context: Context) {
        val lines = context.assets.open("Form.txt").bufferedReader().use { it.readText() }.split("\n")
        lines.forEach { line ->
            val split = line.split(" ")
            val idx = split[0]
            if ( idx.count { char -> char == '.' } == 1) {
                val formPart = FormPart(line.substringAfter(" "), idx)
                items.add(formPart)
                map[idx] = formPart
            } else if(idx.isNotEmpty()) {
                items.last().subparts.add(FormPart.Subpart(line.substringAfter(" "), idx))
            }

        }
    }

    fun writeToFile(context: Context) {

        var dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            //  if ("jpg" == ext) Environment.DIRECTORY_PICTURES else Environment.DIRECTORY_MOVIES)
            , "Hodnocení prodejen"
        )
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("TAG", "failed to create directory")
            }
        }
        val fmtOut = SimpleDateFormat("dd.MM.yyyy")
        val fileName = "Skore " + storeName + " - " + fmtOut.format(Date()) + ".txt"
        val notes = "Poznamky " + storeName + " - " + fmtOut.format(Date()) + ".txt"
        val file = File(dir, fileName)
        val noteFile = File(dir, notes)
        try {
            file.delete()
            noteFile.delete()
            file.createNewFile()
            noteFile.createNewFile()
            FileWriter(file).use { out -> out.write(scoreToString()) }
            FileWriter(noteFile).use { out -> out.write(this.notes.toString()) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun scoreToString(): String {
        var res = StringBuilder().apply {
            append(storeName + "\n" + inspectorName + "\n")
        }
        items.forEach {
            it.subparts.forEach { subpart ->
                res.append(subpart.score.toString())
                res.append(",")
            }
            res.dropLast(1)
            res.append(";")
        }
        return res.toString()
    }

    private fun addItem(item: FormPart) {
        items.add(item)
        map.put(item.index, item)
    }

}
