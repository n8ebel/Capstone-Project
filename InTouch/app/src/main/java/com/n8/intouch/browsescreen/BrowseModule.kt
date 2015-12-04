package com.n8.intouch.browsescreen

import android.content.Context
import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides

/**
 * Created by n8 on 12/2/15.
 */
@Module
class BrowseModule(val view: TabbedFragmentView, val fragment: Fragment) {

    @Provides
    fun provideFragment() : Fragment {
        return fragment
    }

    @Provides
    fun provideTabbedFragmentPresenter(fragment: Fragment) : TabbedFragmentPresenter{
        return TabbedFragmentPresenterImpl(fragment, view)
    }
}