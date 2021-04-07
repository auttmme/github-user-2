package com.auttmme.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.auttmme.githubuser.databinding.ActivityUserDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var detailViewModel: MainViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val user = intent.getParcelableExtra<User>(EXTRA_USER)

        detailViewModel.getDetail().observe(this, {
            itemDetailUser -> if (itemDetailUser != null) {

                showLoading(false)
            }
        })

        with(binding){
            Glide.with(this@UserDetailActivity)
                    .load(user.photo)
                    .apply(RequestOptions().override(55,55))
                    .into(imgDetailPhoto)

            tvDetailUsername.text = user.username
            tvDetailFollowers.text = user.name
            tvLocation.text = user.location
            tvDetailFollowers.text = user.followers.toString()
            tvDetailFollowing.text = user.following.toString()
            tvDetailRepo.text = user.repo.toString()
            tvCompany.text = user.company
        }

        pageAdapter()
    }



    private fun pageAdapter(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
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
}