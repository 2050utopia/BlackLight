package info.papdt.blacklight.support.helper

import android.Manifest
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import rx.Observable
import rx.Subscriber

/**
 * Created by peter on 7/2/16.
 */
object PermissionHelper {
    fun check(permission: String): Observable<Boolean> {
        return Observable.create<Boolean> {
            Dexter.checkPermission(object: PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    it.onNext(true)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    it.onNext(false)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    // Do Nothing for now.
                }
            }, permission)
        }
    }

    fun checkInternet() = check(Manifest.permission.INTERNET)
}