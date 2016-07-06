package info.papdt.blacklight.ui.main

import android.os.Bundle
import nucleus.presenter.RxPresenter

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import info.papdt.blacklight.support.helper.*

/**
 * Created by peter on 7/1/16.
 */
class WelcomePresenter : RxPresenter<WelcomeActivity>() {
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        // Check for Network Permission first
        // Though it is not needed to request for INTERNET now
        // So this will always be true
        PermissionHelper.checkInternet()
                .subscribe { if (!it) RxBus.post(FinishEvent()) }
                .autoUnsubscribe(this)

        setupEvents()
    }

    override fun onDestroy() {
        unsubscribeAll()
        super.onDestroy()
    }

    fun setupEvents() {
        RxBus.listen<NextButtonClickEvent>()
                .subscribe { RxBus.post(NextPageEvent()) }
                .autoUnsubscribe(this)
        RxBus.listen<PageChangeEvent>()
                .subscribe {
                    if (it.page == 1) {
                        RxBus.post(DisableNextButtonEvent())
                        RxBus.post(LoadUrlEvent(AuthorizationHelper.authorizationUrl))
                    } else {
                        RxBus.post(EnableNextButtonEvent())
                    }

                    if (it.page == 2) {
                        RxBus.post(WelcomeUserEvent(AccountManager[AccountManager.current].username))
                    }
                }
                .autoUnsubscribe(this)
        RxBus.listen<PageOverflowEvent>()
                .subscribe { RxBus.post(FinishEvent()) }
                .autoUnsubscribe(this)
        RxBus.listen<RedirectEvent>()
                .flatMap {
                    AuthorizationHelper.handleRedirectUrl(it.url)
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { succeed ->
                    if (succeed) {
                        RxBus.post(NextPageEvent())
                    } else {
                        RxBus.post(ShowLoginFailedMessageEvent())
                        RxBus.post(LoadUrlEvent(AuthorizationHelper.authorizationUrl))
                    }
                }
                .autoUnsubscribe(this)
    }
}
