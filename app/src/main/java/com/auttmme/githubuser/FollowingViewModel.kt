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

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setFollowing(username: String) {
        val listItemsFollowing = ArrayList<User>()

        val url = "https://api.github.com/users/${username}/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()) {
                        val itemFollowing = responseArray.getJSONObject(i)
                        val following = User()
                        following.photo = itemFollowing.getString("avatar_url")
                        following.username = itemFollowing.getString("login")
                        listItemsFollowing.add(following)
                    }
                    listFollowing.postValue(listItemsFollowing)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun getFollowing() : LiveData<ArrayList<User>> {
        return listFollowing
    }
}