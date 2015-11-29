package com.n8.intouch.tabbedscreen

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.n8.intouch.common.TitleProvider

/**
 * A {@link FragmentPagerAdapter} that can adapt a specified list of fragments for
 * a {@link ViewPager}.
 *
 * <p>
 *  If the passed fragment is a {@link TitleProvider}, the fragment's returned title will be used for
 *  the page title.
 * </p>
 */
class TabbedFragmentAdapter : FragmentPagerAdapter {

    lateinit var fragmentList:List<Fragment>

    constructor(fragments:List<Fragment>, fm: FragmentManager?) : super(fm){
        fragmentList = fragments
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment? {
        return fragmentList.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var fragment = fragmentList[position]

        if (fragment is TitleProvider) {
            return fragment.getTitle()
        } else {
            return "No Title"
        }
    }
}