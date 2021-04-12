package com.auttmme.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowersViewModel : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setFollowers(username: String) {
        val listItemsFollowers = ArrayList<User>()

        val url = "https://api.github.com/users/${username}/followers"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseArray = JSONArray(result)
                    Log.d("followers", result)
                    for (i in 0 until responseArray.length()) {
                        val itemFollowers = responseArray.getJSONObject(i)
                        val followers = User()
                        followers.photo = itemFollowers.getString("avatar_url")
                        followers.username = itemFollowers.getString("login")
                        listItemsFollowers.add(followers)
                    }
                    listFollowers.postValue(listItemsFollowers)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}