package info.papdt.blacklight.ui.main

import android.app.AlertDialog
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.activity_welcome.*
import android.support.v4.view.PagerAdapter
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
                .subscribe {
                    unsubscribeAll()
                    finish()
                }
                .autoUnsubscribe(this)

        setupPager()
        setupButtons()
        setupMessages()
        setupWebView()
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
                .subscribe { welcome_button_next.setEnabled(false) }
                .autoUnsubscribe(this)
        RxBus.listen<EnableNextButtonEvent>()
                .subscribe { welcome_button_next.setEnabled(true) }
                .autoUnsubscribe(this)
    }

    fun setupPager() {
        welcome_pager.adapter = MainPagerAdapter()
        RxBus.listen<NextPageEvent>()
                .subscribe {
                    if (welcome_pager.currentItem < 2) {
                        welcome_pager.currentItem = welcome_pager.currentItem + 1
                    } else {
                        RxBus.post(PageOverflowEvent())
                    }
                }
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

    fun setupMessages() {
        RxBus.listen<ShowLoginFailedMessageEvent>()
                .subscribe {
                    with(AlertDialog.Builder(this)) {
                        setMessage(R.string.login_failed)
                        setPositiveButton(android.R.string.ok, null)
                    }.show()
                }
                .autoUnsubscribe(this)
        RxBus.listen<WelcomeUserEvent>()
                .subscribe {
                    welcome_user.text = String.format(getText(R.string.welcome_user).toString(), it.username)
                }
                .autoUnsubscribe(this)
    }

    fun setupWebView() {
        RxBus.listen<LoadUrlEvent>()
                .subscribe { welcome_web_view.loadUrl(it.url) }
                .autoUnsubscribe(this)
        with(welcome_web_view.settings) {
            javaScriptEnabled = true
            saveFormData = false
            savePassword = false
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        welcome_web_view.setWebViewClient(object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (AuthorizationHelper.isRedirected(url!!)) {
                    view?.stopLoading()
                    view?.loadUrl("about:blank")
                    RxBus.post(RedirectEvent(url))
                } else {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (AuthorizationHelper.isRedirected(url!!)) {
                    view?.stopLoading()
                    view?.loadUrl("about:blank")
                }
                super.onPageStarted(view, url, favicon)
            }
        })
    }
}
