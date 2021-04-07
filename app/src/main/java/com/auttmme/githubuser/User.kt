package com.auttmme.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var photo: String? = "",
    var username: String? = "",
    var name: String? = "",
    var location: String? = "",
    var followers: Int? = 0,
    var following: Int? = 0,
    var repo: Int? = 0,
    var company: String? = ""
        ) : Parcelable