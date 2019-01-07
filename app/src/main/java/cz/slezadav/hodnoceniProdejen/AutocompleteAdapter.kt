package cz.slezadav.hodnoceniProdejen

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import java.text.Normalizer


class AutocompleteAdapter(context: Context, textViewResourceId: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, textViewResourceId), Filterable {

    private var fullList = objects.toMutableList()
    private val originalValues = mutableListOf<String>().apply { addAll(fullList) }
    private val filter = ArrayFilter(this)

    override fun getFilter(): Filter {
        return filter
    }

    override fun getCount(): Int {
        return fullList.size
    }

    override fun getItem(position: Int): String {
        return fullList[position]
    }


    class ArrayFilter(val adapter: AutocompleteAdapter) : Filter() {
        private var lock: Any = Any()

        override fun performFiltering(prefix: CharSequence): FilterResults {
            val results = FilterResults()
            val linePrefix = prefix.split("\n").last()
            if (linePrefix.isEmpty()) {
                synchronized(lock) {
                    val list = mutableListOf<String>().apply { addAll(adapter.originalValues) }
                    results.values = list
                    results.count = list.size
                }
            } else {
                val prefixString = linePrefix.toLowerCase()
                val values = adapter.originalValues
                val newValues = mutableListOf<String>()
                newValues.addAll(values.filter { stripAccents(it.toLowerCase()).contains(stripAccents(prefixString)) })
                results.values = newValues
                results.count = newValues.size
            }
            return results
        }


        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.values != null) {
                adapter.fullList = results.values as MutableList<String>
            } else {
                adapter.fullList = mutableListOf()
            }
            if (results.count > 0) {
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyDataSetInvalidated()
            }
        }

        fun stripAccents(s: String): String {
            var s = s
            s = Normalizer.normalize(s, Normalizer.Form.NFD)
            s = s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
            return s
        }
    }
}