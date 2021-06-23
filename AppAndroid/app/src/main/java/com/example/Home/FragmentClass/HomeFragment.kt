import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.Home.FragmentClass.HomeChildFragments.FragmentsAdapter.FragmentAdapter
import android.R
import com.google.android.material.tabs.TabLayout


class HomeFragment:Fragment(com.example.Home.R.layout.home_fragment_electricity) {

    var mView : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(com.example.Home.R.layout.home_fragment_electricity, container, false)
        mView = rootView
        val viewPager : ViewPager? = mView?.findViewById<ViewPager>(com.example.Home.R.id.view_pager)
        viewPager?.setAdapter(FragmentAdapter(childFragmentManager))
        val tabLayout : TabLayout? = mView?.findViewById<TabLayout>(com.example.Home.R.id.tabLayout)
        tabLayout?.setupWithViewPager(viewPager)

        return rootView
    }
//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}