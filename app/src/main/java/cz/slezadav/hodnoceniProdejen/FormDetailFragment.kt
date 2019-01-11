package cz.slezadav.hodnoceniProdejen

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.slezadav.hodnoceniProdejen.databinding.FragmentScreenSlidePageBinding
import cz.slezadav.hodnoceniProdejen.model.Evaluation

/**
 * A fragment representing a single Form detail screen.
 * This fragment is either contained in a [FormListActivity]
 * in two-pane mode (on tablets) or a [FormDetailActivity]
 * on handsets.
 */
class FormDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
   // private var item: FormPart? = null

    lateinit var ui: FragmentScreenSlidePageBinding
    private var adapter: FormPagerAdapter? = null
    var initIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                initIndex = Evaluation.items.indexOf(Evaluation.map[it.getString(ARG_ITEM_ID)])
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DataBindingUtil.inflate<FragmentScreenSlidePageBinding>(
        inflater,
        R.layout.fragment_screen_slide_page,
        container,
        false
    ).apply {
        ui = this
    }.root

    override fun onPause() {
        super.onPause()
        activity?.let {
            Evaluation.writeToFile(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            adapter = FormPagerAdapter(it.supportFragmentManager)
            ui.pager.adapter = adapter
            ui.pager.setCurrentItem(initIndex,false)
            activity?.findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)?.title = Evaluation.items[initIndex].index+" "+
                    Evaluation.items[initIndex].title
            ui.pager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(p0: Int) {
                    (activity as FormListActivity?)?.let {
                        it.findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)?.title = Evaluation.items[p0].index+" "+
                                Evaluation.items[p0].title
                        Evaluation.writeToFile(it)
                        it.setActiveItem(p0)
                    }
                }

            })
        }

    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
