package info.papdt.blacklight.ui.main

import kotlinx.android.synthetic.main.activity_welcome.*
import android.support.v4.view.PagerAdapter
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import nucleus.view.NucleusAppCompatActivity
import nucleus.factory.RequiresPresenter
import info.papdt.blacklight.R
import info.papdt.blacklight.support.helper.*

@RequiresPresenter(WelcomePresenter::class)
class WelcomeActivity : NucleusAppCompatActivity<WelcomePresenter>() {
    val VIEWS: Array<View> by lazy { arrayOf<View>(welcome_page_1, welcome_page_2, welcome_page_3) }

    inner class MainPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return 3;
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object` as View
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
            return VIEWS[position]
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        RxBus.listen<FinishEvent>()
                .subscribe { onDestroy() }
                .autoUnsubscribe(this)

        setupPager()
        setupButtons()
    }

    override fun onDestroy() {
        unsubscribeAll()
        super.onDestroy()
    }

    fun setupButtons() {
        welcome_button_next.setOnClickListener {
            RxBus.post(NextButtonClickEvent())
        }
        RxBus.listen<DisableNextButtonEvent>()
                .subscribe { welcome_button_next.visibility = View.INVISIBLE }
                .autoUnsubscribe(this)
    }

    fun setupPager() {
        welcome_pager.adapter = MainPagerAdapter()
        RxBus.listen<NextPageEvent>()
                .subscribe { welcome_pager.currentItem = welcome_pager.currentItem + 1 }
                .autoUnsubscribe(this)
        welcome_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                RxBus.post(PageChangeEvent(position))
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
        })
    }
}
