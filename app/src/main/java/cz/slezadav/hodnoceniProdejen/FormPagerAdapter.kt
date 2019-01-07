package cz.slezadav.hodnoceniProdejen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import cz.slezadav.hodnoceniProdejen.model.Evaluation

class FormPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = Evaluation.items.size

    override fun getItem(position: Int): Fragment =  FormPageFragment().apply {
        arguments = Bundle().apply {
            putInt(FormPageFragment.ARG_POSITION, position)
        }
    }
}