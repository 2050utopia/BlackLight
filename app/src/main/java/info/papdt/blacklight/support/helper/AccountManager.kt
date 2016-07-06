package info.papdt.blacklight.support.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by peter on 7/6/16.
 */
data class Account(val username: String, val token: String, val expires: Long)
object AccountManager {
    private var mPrefs: SharedPreferences? = null
    private var mAccounts = mutableListOf<Account>()
    private var mCurrent: Int = 0

    fun init(context: Context) {
        mPrefs = context.getSharedPreferences("accounts", Context.MODE_PRIVATE)
        reload()
    }

    fun reload() {
        mCurrent = mPrefs!!.getInt("current", 0)

        val names = mPrefs!!.getString("names", "").split("\\|")
        val tokens = mPrefs!!.getString("tokens", "").split("\\|")
        val expireDates = mPrefs!!.getString("expire_dates", "").split("\\|")

        names.withIndex().forEach {
            if (it.value.trim() == "" || tokens.size <= it.index || expireDates.size <= it.index) return
            mAccounts.add(Account(it.value, tokens[it.index], expireDates[it.index].toLong()))
        }
    }

    fun save() {
        var names = mutableListOf<String>()
        var tokens = mutableListOf<String>()
        var expireDates = mutableListOf<Long>()

        mAccounts.forEach { account ->
            names.add(account.username)
            tokens.add(account.token)
            expireDates.add(account.expires)
        }

        mPrefs!!.edit()
                .putString("names", names.joinToString("\\|"))
                .putString("tokens", tokens.joinToString("\\|"))
                .putString("expire_dates", expireDates.joinToString("\\|"))
                .putInt("current", mCurrent)
                .commit()
    }

    var count: Int = 0
        get() = mAccounts.size

    var current: Int
        get() = mCurrent
        set(value) {
            mCurrent = value
            save()
        }

    operator fun get(index: Int): Account = mAccounts[index]
    operator fun set(index: Int, value: Account) {
        mAccounts[index] = value
        save()
    }

    fun add(account: Account) {
        mAccounts.add(account)
        save()
    }

    fun remove(index: Int) {
        mAccounts.removeAt(index)
        save()
    }
}