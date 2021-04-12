package com.auttmme.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.auttmme.githubuser.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowersViewModel

    companion object {
        private const val USERNAME = "username"

        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(USERNAME)
        setRecyclerView(username)

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)

        if (username != null) {
            showLoading(true)
            followersViewModel.setFollowers(username)
        }

        followersViewModel.getFollowers().observe(viewLifecycleOwner, {
            followers -> if (followers != null) {
            adapter.setData(followers)
            showLoading(false)
        }
        })
    }

    private fun setRecyclerView(username: String?) {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}