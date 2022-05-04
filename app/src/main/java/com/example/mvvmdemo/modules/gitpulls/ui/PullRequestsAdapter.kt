package com.example.mvvmdemo.modules.gitpulls.ui

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmdemo.R
import com.example.mvvmdemo.databinding.ItemPullrequestBinding
import com.example.mvvmdemo.modules.gitpulls.entity.PullRequest
import java.text.SimpleDateFormat
import java.util.*

class PullRequestsAdapter(private val context: Context?, var list: List<PullRequest>)
    : RecyclerView.Adapter<PullRequestsAdapter.PullRequestViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullRequestsAdapter.PullRequestViewHolder {
        var binding = ItemPullrequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PullRequestViewHolder(binding )
    }

    override fun onBindViewHolder(holder: PullRequestViewHolder, position: Int) {
        holder.setView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PullRequestViewHolder(val view: ItemPullrequestBinding) : RecyclerView.ViewHolder(view.root) {
        fun setView(pullRequest : PullRequest){
            context?.let {
                pullRequest.user.avatarUrl?.let {
                    Glide.with(context.applicationContext).load(pullRequest.user.avatarUrl)
                        .into(view.userImageView)
                }

                view.title.text = pullRequest.title

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

                val parsedDate = inputFormat.parse(pullRequest.closedAt)
                parsedDate?.let {
                    val relativeTime = DateUtils.getRelativeTimeSpanString(parsedDate.time)
                    view.closedTimeView.text = context.getString(R.string.closed, relativeTime)
                }

                val parsedCreatedDate = inputFormat.parse(pullRequest.createdAt)
                parsedCreatedDate?.let {
                    val relativeTime = DateUtils.getRelativeTimeSpanString(parsedCreatedDate.time)
                    view.byUserView.text = context.getString(
                        R.string.creator_detail,
                        pullRequest.user.login,
                        relativeTime
                    )
                }
            }
        }
    }

}