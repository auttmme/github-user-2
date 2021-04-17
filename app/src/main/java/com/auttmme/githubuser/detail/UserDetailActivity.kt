package com.auttmme.githubuser.detail

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.auttmme.githubuser.R
import com.auttmme.githubuser.adapter.SectionsPagerAdapter
import com.auttmme.githubuser.databinding.ActivityUserDetailBinding
import com.auttmme.githubuser.db.DatabaseContract.UserColumns.Companion.PHOTO
import com.auttmme.githubuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.auttmme.githubuser.db.UserHelper
import com.auttmme.githubuser.model.User
import com.auttmme.githubuser.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var detailViewModel: MainViewModel
    private lateinit var userHelper: UserHelper

    private var isFavorite = false

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
        const val EXTRA_USER = "extra_user"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val user = intent.getParcelableExtra<User>(EXTRA_USER)

        user.username?.let {
            if (user != null) {
                detailViewModel.setDetail(it)
                showLoading(true)
            }
        }

        detailViewModel.getDetail().observe(this, {
            itemDetailUser -> if (itemDetailUser != null) {

            with(binding){
                Glide.with(this@UserDetailActivity)
                        .load(itemDetailUser.photo)
                        .apply(RequestOptions().override(85,85))
                        .into(imgDetailPhoto)

                tvDetailUsername.text = itemDetailUser.username
                tvDetailFullName.text = itemDetailUser.name
                tvLocation.text = itemDetailUser.location
                tvDetailFollowers.text = itemDetailUser.followers.toString()
                tvDetailFollowing.text = itemDetailUser.following.toString()
                tvDetailRepo.text = itemDetailUser.repo.toString()
                tvCompany.text = itemDetailUser.company
            }
                showLoading(false)
            }
        })
        Log.d("user:" , user.toString())
        user.username?.let { pageAdapter(it) }

        checkFavorite(user)
        floatingButton(user)
    }

    private fun pageAdapter(username: String){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) {
            tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkFavorite(user: User) {
        val cursor: Cursor = userHelper.queryById(user.id.toString())
        if (cursor.moveToNext()) {
            isFavorite = true
            setStatusFavorite(true)
        }
    }

    private fun floatingButton(user: User) {
        setStatusFavorite(isFavorite)
        binding.fab.setOnClickListener {
            if (isFavorite) {
                val result = userHelper.deleteById(user.id.toString())
                user.id = result
                setResult(RESULT_DELETE, intent)
            } else {
                val values = ContentValues()
                values.put(USERNAME, user.username)
                values.put(PHOTO, user.photo)

                val result = userHelper.insert(values)
                user.id = result.toInt()
                setResult(RESULT_ADD, intent)
            }
            isFavorite !=isFavorite
            setStatusFavorite(isFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if(statusFavorite) {
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}