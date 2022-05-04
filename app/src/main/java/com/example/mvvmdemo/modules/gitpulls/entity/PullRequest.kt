package com.example.mvvmdemo.modules.gitpulls.entity
import com.google.gson.annotations.SerializedName


data class PullRequest(
    @SerializedName("closed_at")
    val closedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: User

) {
    override fun toString(): String {
        return "PullRequest(closedAt=$closedAt, createdAt='$createdAt', id=$id, title='$title', updatedAt='$updatedAt', user=$user)"
    }
}

data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,

) {
    override fun toString(): String {
        return "User(avatarUrl=$avatarUrl, id=$id, login='$login')"
    }
}

