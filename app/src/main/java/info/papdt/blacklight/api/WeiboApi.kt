package info.papdt.blacklight.api

import retrofit2.http.*
import rx.Observable

import info.papdt.blacklight.model.*

/**
 * Created by peter on 7/6/16.
 */
interface WeiboApi {
    @GET("account/get_uid.json")
    fun getUid(): Observable<Uid>
    @GET("users/show.json")
    fun showUser(@Query("uid") uid: Long): Observable<User>
}