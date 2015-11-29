package com.n8.intouch.tabbedscreen


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.n8.intouch.R

/**
 * A tabbed fragment to hold multiple other fragments
 */
class TabbedFragment : Fragment() {

    lateinit var tabLayout:TabLayout

    lateinit var toolbar:Toolbar

    lateinit var viewPager:ViewPager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_tabbed, container, false)

        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout
        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        viewPager = view.findViewById(R.id.viewPager) as ViewPager

        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        toolbar.title = getString(R.string.app_name)

        return view
    }

    fun setupViewPager(viewPager: ViewPager) {
        viewPager.adapter = createPagerAdapter()
    }

    fun createPagerAdapter(): PagerAdapter? {
        return TabbedFragmentAdapter(createFragmentList(), activity.supportFragmentManager)
    }

    fun createFragmentList(): List<Fragment>{
        return listOf<Fragment>(EventsListFragment(), ContactsFragment())
    }

}
