package cz.slezadav.hodnoceniProdejen

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.slezadav.hodnoceniProdejen.model.Evaluation
import cz.slezadav.hodnoceniProdejen.model.FormPart
import kotlinx.android.synthetic.main.activity_form_list.*
import kotlinx.android.synthetic.main.form_list.*
import kotlinx.android.synthetic.main.form_list_content.view.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [FormDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class FormListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    var twoPane: Boolean = false

    var adapter:SimpleItemRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (twoPaneOnly != null) {
            twoPane = true
        }

        setupRecyclerView(form_list)
    }

    override fun onBackPressed() {
        if (!twoPane && findViewById<View>(R.id.form_detail_container).visibility == View.VISIBLE) {
            findViewById<View>(R.id.form_detail_container).visibility = View.GONE
            findViewById<View>(R.id.form_list).visibility = View.VISIBLE
            toolbar.title = title
        } else {
            super.onBackPressed()
        }
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

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = SimpleItemRecyclerViewAdapter(this, Evaluation.items, twoPane)
        recyclerView.adapter = adapter
        if(twoPane){
            adapter?.let {
                it.onSelect(it.values[0])
            }

        }
    }

    fun setActiveItem(position:Int){
        adapter?.setActiveItem(position)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: FormListActivity,
        val values: List<FormPart> = listOf(),
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as FormPart
                onSelect(item)
            }
        }

        fun onSelect(item:FormPart?){
            if(item == null){
                return
            }
            val fragment = FormDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FormDetailFragment.ARG_ITEM_ID, item.index)
                }
            }
            values.forEach { it.active = it.index == item.index }

            parentActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.form_detail_container, fragment)
                .commit()
            parentActivity.findViewById<View>(R.id.form_detail_container).visibility = View.VISIBLE
            if (!twoPane) {
                parentActivity.findViewById<View>(R.id.form_list).visibility = View.GONE
            }else{
                parentActivity.title = "${item.index} ${item.title}"
                notifyDataSetChanged()
            }
        }
        fun setActiveItem(position:Int){
            if(parentActivity.twoPane) {
                values.forEach { it.active = false }
                if (position in 0 until values.size) {
                    values[position].active = true
                }
                if(twoPane){
                    parentActivity.title = "${values[position].index} ${values[position].title}"
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemRecyclerViewAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.form_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.index
            holder.contentView.text = item.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }

            holder.root.setBackgroundColor(ContextCompat.getColor(parentActivity,if(item.active) R.color.colorAccent else android.R.color.white))
            holder.contentView.setTextColor(ContextCompat.getColor(parentActivity,if(item.active) android.R.color.white else android.R.color.black))
            holder.idView.setTextColor(ContextCompat.getColor(parentActivity,if(item.active) android.R.color.white else android.R.color.black))
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val root = view
        }
    }
}
