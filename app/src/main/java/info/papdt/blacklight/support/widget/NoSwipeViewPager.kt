package info.papdt.blacklight.support.widget

import android.content.Context
import android.util.AttributeSet
import android.support.v4.view.ViewPager
import android.view.MotionEvent

/**
 * Created by peter on 7/2/16.
 * A ViewPager which does not accept swipe gestures
 */
class NoSwipeViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}