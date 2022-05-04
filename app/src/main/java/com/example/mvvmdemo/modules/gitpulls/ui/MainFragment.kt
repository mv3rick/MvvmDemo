package com.example.mvvmdemo.modules.gitpulls.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.mvvmdemo.core.NetworkResult
import com.example.mvvmdemo.databinding.MainFragmentBinding
import com.example.mvvmdemo.modules.gitpulls.entity.PullRequest
import com.example.mvvmdemo.modules.gitpulls.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val TAG = "MainFragment"
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var pullRequests = ArrayList<PullRequest>()
    private val pullRequestsAdapter: PullRequestsAdapter by lazy {
        PullRequestsAdapter(context, pullRequests)
    }
    private val pullRequestsLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private var pageNumber = 1;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.response.observe(viewLifecycleOwner, Observer {
            handleGitPullRequestResponse(it)
        })
        binding.gitPullsListView.apply {
            layoutManager = pullRequestsLayoutManager
            adapter = pullRequestsAdapter
            addOnScrollListener(getScrollListener())
        }
        fetchGitPullRequests()
    }

    private fun handleGitPullRequestResponse(result: NetworkResult<List<PullRequest>>?) {
        Log.d(TAG, "handleGitPullRequestResponse: $result")
        if(result == null){
            return
        }
        when (result) {
            is NetworkResult.Success -> {
                if(result.paginationIndex == pageNumber) {
                    result.data?.let {
                        if (pageNumber == 1) {
                            pullRequests.clear()
                            pullRequests.addAll(result.data)
                            pullRequestsAdapter.notifyDataSetChanged()
                        } else {
                            val pos = pullRequests.size
                            val resultSize = result.data.size
                            pullRequests.addAll(result.data)
                            pullRequestsAdapter.notifyItemRangeInserted(pos, resultSize)
                        }
                    }
                    binding.loadingBar.visibility = View.GONE
                }
            }

            is NetworkResult.Error -> {
                binding.loadingBar.visibility = View.GONE
                result.message?.let {
                    val snack = Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG)
                    snack.setAction("Retry", View.OnClickListener {
                        fetchGitPullRequests()
                    })
                    snack.show()
                }
            }

            is NetworkResult.Loading -> {
                binding.loadingBar.visibility = View.VISIBLE
            }
        }
    }

    private fun fetchGitPullRequests() {
        Log.d(TAG, "fetchGitPullRequests pageNumber: $pageNumber")
        viewModel.fetchGitPullRequests(pageNumber = pageNumber)
    }

    private fun getScrollListener() : OnScrollListener {
        return object : OnScrollListener() {
            private var previousTotal = 0
            private var loading = true
            private val visibleThreshold = 10
            var firstVisibleItem = 0
            var visibleItemCount = 0
            var totalItemCount = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = binding.gitPullsListView.childCount
                totalItemCount = pullRequestsLayoutManager.itemCount
                firstVisibleItem = pullRequestsLayoutManager.findFirstVisibleItemPosition()
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount
                    <= firstVisibleItem + visibleThreshold) {
                    // End has been reached
                    pageNumber++
                    Log.d(TAG, "fetching more elements pageNumber : $pageNumber")
                    fetchGitPullRequests()
                    loading = true
                }
            }
        }
    }
}