package cz.slezadav.hodnoceniProdejen

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cz.slezadav.hodnoceniProdejen.Util.NewLineTokenizer
import cz.slezadav.hodnoceniProdejen.databinding.ActivityNotesBinding
import cz.slezadav.hodnoceniProdejen.model.Evaluation


class NotesActivity : AppCompatActivity() {
    lateinit var ui:ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = DataBindingUtil.setContentView(this,R.layout.activity_notes)
        val lines = assets.open("Autocomplete.txt").bufferedReader().use { it.readText() }.split("\n")
        supportActionBar?.title = "Poznámky"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val ft = mutableListOf<String>()
        lines.forEach { ft.add(it) }
        val adapter = AutocompleteAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            ft
        )
        ui.autoCompleteTextView.setText(Evaluation.notes)
        ui.autoCompleteTextView.setTokenizer(NewLineTokenizer())
        ui.autoCompleteTextView.setAdapter(adapter)
        ui.autoCompleteTextView.threshold = 1
    }

    override fun onPause() {
        super.onPause()
        Evaluation.notes = ui.autoCompleteTextView.text
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}