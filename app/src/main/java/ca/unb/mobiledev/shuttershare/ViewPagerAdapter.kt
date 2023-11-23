package ca.unb.mobiledev.shuttershare

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0)
            fragment = JoinEventFragment()
        else if (position == 1)
            fragment = CreateEventFragment()
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0)
            title = "Join Event"
        else if (position == 1)
            title = "Create Event"
        return title
    }
}