package info.papdt.blacklight.support.helper

import rx.Observable

import info.papdt.blacklight.api.Weibo

/**
 * Created by peter on 7/6/16.
 */
object AuthorizationHelper {
    const val OAUTH2_ACCESS_AUTHORIZE = "https://open.weibo.cn/oauth2/authorize"

    // Authentication information of Weico
    const val OAUTH2_APP_ID = "211160679"
    const val OAUTH2_APP_SECRET = "1e6e33db08f9192306c4afa0a61ad56c"
    const val OAUTH2_SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,invitation_write"
    const val OAUTH2_REDIRECT = "http://oauth.weico.cc"

    var authorizationUrl: String = ""
        get() {
            return "${OAUTH2_ACCESS_AUTHORIZE}?client_id=${OAUTH2_APP_ID}&key_hash=${OAUTH2_APP_SECRET}&response_type=token&redirect_uri=${OAUTH2_REDIRECT}&display=mobile&scope=${OAUTH2_SCOPE}"
        }

    fun isRedirected(url: String): Boolean {
        return url.startsWith(OAUTH2_REDIRECT)
    }

    fun handleRedirectUrl(url: String): Observable<Boolean> {
        if (url.contains("error")) throw IllegalArgumentException("The URL contains errors")

        val tokenIndex = url.indexOf("access_token=")
        val expiresIndex = url.indexOf("expires_in=")
        val token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex))
        val expires = System.currentTimeMillis() + url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex)).toLong() * 1000

        var account = Account("", token, expires)
        AccountManager.add(account)
        AccountManager.current = AccountManager.count - 1

        return Weibo.Api.getUid()
                .flatMap { Weibo.Api.showUser(it.uid) }
                .map {
                    AccountManager.set(AccountManager.current, Account(it.name, token, expires))
                    true
                }
                .onErrorReturn { false }
    }

}