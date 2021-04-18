package com.auttmme.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.auttmme.githubuser.adapter.UserAdapter
import com.auttmme.githubuser.databinding.ActivityFavoriteBinding
import com.auttmme.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.auttmme.githubuser.db.UserHelper
import com.auttmme.githubuser.detail.UserDetailActivity
import com.auttmme.githubuser.helper.MappingHelper
import com.auttmme.githubuser.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.nio.file.attribute.UserDefinedFileAttributeView

class FavoriteActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        adapter = UserAdapter()
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User, position: Int) {
                val intent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_POSITION, position)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })

        if (savedInstanceState == null) {
            loadUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.mData = list
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                UserDetailActivity.REQUEST_ADD -> if (resultCode == UserDetailActivity.RESULT_ADD) {
                    val user = data.getParcelableExtra<User>(UserDetailActivity.EXTRA_USER) as User

                    adapter.addItem(user)
                    binding.rvUsers.smoothScrollToPosition(adapter.itemCount -1)

                    Log.d("mUser: ", user.toString())
                    Log.d("mData: ", data.toString())
                }
                UserDetailActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(UserDetailActivity.EXTRA_USER, 0)
                    adapter.removeItem(position)
                }
            }
        }
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteUser = deferredUsers.await()
            if (favoriteUser.size > 0) {
                adapter.mData = favoriteUser
            } else {
                adapter.mData = ArrayList()
            }
            userHelper.close()
            Log.d("vvv: ", favoriteUser.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }
}