package co.unitedsoftware.standby.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import co.unitedsoftware.standby.R
import co.unitedsoftware.standby.ui.adapter.ViewPagerAdapter

class ViewPagerFragment : BaseFragment() {

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_viewpager, container, false)

        viewPager2 = rootView.findViewById(R.id.main)

        adapter = ViewPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )
        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 9999

        return rootView
    }
}