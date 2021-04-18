package com.auttmme.githubuser.helper

import android.database.Cursor
import com.auttmme.githubuser.db.DatabaseContract
import com.auttmme.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PHOTO))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                userList.add(User(id, photo, username))
            }
        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()
        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PHOTO))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
            user = User(id, photo, username)
        }
        return user
    }
}