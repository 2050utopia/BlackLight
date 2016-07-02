package info.papdt.blacklight.support.helper

import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.HashMap

/**
 * Created by peter on 7/2/16.
 */
var subscriptions = HashMap<Any, CompositeSubscription>()

fun Any.unsubscribeAll() {
    subscriptions[this]?.unsubscribeAll()
    subscriptions.remove(this)
}

fun Subscription.autoUnsubscribe(s: Any) {
    if (subscriptions[s] == null) {
        subscriptions[s] = CompositeSubscription()
    }
    subscriptions[s]!!.add(this)
}