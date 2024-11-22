package co.unitedsoftware.standby.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.unitedsoftware.standby.ui.fragment.clock.BlendClockFragment
import co.unitedsoftware.standby.ui.fragment.clock.MaterialClockFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MaterialClockFragment()
            1 -> BlendClockFragment()
            else -> throw IllegalArgumentException()
        }
}