package info.papdt.blacklight.api

import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

import okhttp3.OkHttpClient

import info.papdt.blacklight.support.helper.AccountManager

/**
 * Created by peter on 7/6/16.
 */
object Weibo {
    val Api by lazy {
        val client = OkHttpClient().newBuilder()

        client.networkInterceptors().add(Interceptor {
            val request = it.request()
            val url = request.url().newBuilder()
                    .addQueryParameter("access_token", AccountManager[AccountManager.current].token)
                    .build()
            it.proceed(request.newBuilder().url(url).build())
        })

        with(Retrofit.Builder()) {
            baseUrl("https://api.weibo.com/2/")
            client(client.build())
            addConverterFactory(JacksonConverterFactory.create())
            addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            build()
        }.create(WeiboApi::class.java)
    }
}