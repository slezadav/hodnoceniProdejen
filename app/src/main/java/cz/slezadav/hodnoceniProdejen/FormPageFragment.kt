package cz.slezadav.hodnoceniProdejen

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import cz.slezadav.hodnoceniProdejen.databinding.FormDetailBinding
import cz.slezadav.hodnoceniProdejen.databinding.FormRowBinding
import cz.slezadav.hodnoceniProdejen.model.Evaluation
import cz.slezadav.hodnoceniProdejen.model.FormPart

class FormPageFragment : Fragment() {

    private lateinit var ui: FormDetailBinding
    private lateinit var item: FormPart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_POSITION)) {
                item = Evaluation.items[it.getInt(ARG_POSITION)]
            }
        }
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DataBindingUtil.inflate<FormDetailBinding>(
        inflater,
        R.layout.form_detail,
        container,
        false
    ).apply {
        ui = this
        item.subparts.forEachIndexed { index, it ->
            DataBindingUtil.inflate<FormRowBinding>(inflater, R.layout.form_row, ui.rowContainer, true).apply {
                if (it.score != -1f) {
                    radio1.radioGroup.findViewWithTag<View>(it.getScoreAsString())?.let{
                        radio1.radioGroup.check(it.id)
                    }

                }
                label1.text = "${it.index} ${it.title}"
                radio1.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    Evaluation.items[Evaluation.items.indexOf(item)].subparts[index].score = group.findViewById<View>(
                        checkedId
                    )?.tag?.toString()?.toFloat() ?: -1f
                }
            }
        }
        var last = item == Evaluation.items.last()
        ui.finishButton.text = if(last) "Uložit a zavřít" else "Další"
        ui.finishButton.setOnClickListener {
            if(last) {
                activity?.finish()
            }else{
                val frag =  ( (activity as? FormListActivity?)?.supportFragmentManager?.findFragmentByTag("pagerFragment") as? FormDetailFragment?)
                frag?.ui?.pager?.let {
                    it.setCurrentItem(it.currentItem+1,true)
                }

            }
        }

        if (!(activity as FormListActivity).twoPane) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        }


    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    /**
     * Handluje klik na nastaveni v menu
     * @param item polozka menu
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.note) {
            activity?.startActivityFromFragment(this@FormPageFragment, Intent(activity, NotesActivity::class.java), 0)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val ARG_POSITION = "item_pos"
    }
}