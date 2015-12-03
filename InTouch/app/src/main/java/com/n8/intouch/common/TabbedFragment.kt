package com.n8.intouch.common

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n8.intouch.R
import com.n8.intouch.browsescreen.TabbedFragmentAdapter

/**
 * A tabbed fragment to hold multiple other fragments
 *
 * <p>
 *  If the fragments specified in {@link #createFragmentList(List<Fragment>)} implement {@link TitleProvider}, then those
 *  titles will be displayed as tab titles.
 * </p>
 */
abstract class TabbedFragment : Fragment() {

    lateinit var tabLayout: TabLayout

    lateinit var toolbar: Toolbar

    lateinit var viewPager: ViewPager

    lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_tabbed, container, false)

        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        viewPager = view.findViewById(R.id.viewPager) as ViewPager

        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    fun setupViewPager(viewPager: ViewPager) {
        viewPager.adapter = createPagerAdapter()
    }

    fun createPagerAdapter(): PagerAdapter? {
        return TabbedFragmentAdapter(createFragmentList(), activity.supportFragmentManager)
    }

    abstract fun createFragmentList(): List<Fragment>
}