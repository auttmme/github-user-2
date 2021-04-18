package com.auttmme.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auttmme.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()
    val userDetail = MutableLiveData<User>()

    fun setUser(username: String) {
        val listItems = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=${username}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val itemUser = User()
                        itemUser.id = item.getInt("id")
                        itemUser.username= item.getString("login")
                        itemUser.photo = item.getString("avatar_url")
                        listItems.add(itemUser)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun setDetail(username: String) {
        val listDetailItems = User()

        val url = "https://api.github.com/users/${username}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)
                    Log.d("hasil", responseObject.toString())

                    val itemDetailUser = User()
                    itemDetailUser.id = responseObject.getInt("id")
                    itemDetailUser.photo = responseObject.getString("avatar_url")
                    itemDetailUser.username = responseObject.getString("login")
                    itemDetailUser.name = responseObject.getString("name")
                    itemDetailUser.company = responseObject.getString("company")
                    itemDetailUser.location = responseObject.getString("location")
                    itemDetailUser.repo = responseObject.getInt("public_repos")
                    itemDetailUser.followers = responseObject.getInt("followers")
                    itemDetailUser.following = responseObject.getInt("following")

                    userDetail.postValue(itemDetailUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getDetail(): LiveData<User> {
        return userDetail
    }
}