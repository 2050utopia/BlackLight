package info.papdt.blacklight.support.helper

import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.SerializedSubject
import rx.subjects.Subject

/**
 * Created by peter on 7/2/16.
 */
object RxBus {
    val bus: Subject<Event, Event> = SerializedSubject<Event, Event>(PublishSubject())

    fun post(obj: Event) {
        bus.onNext(obj)
    }

    inline fun <reified T : Event> listen(): Observable<T> {
        return bus.ofType(T::class.java)
    }
}
