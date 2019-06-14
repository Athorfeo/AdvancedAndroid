package com.obcompany.advancedandroid.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.obcompany.advancedandroid.app.model.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): Flowable<MutableList<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User): Completable

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM Users")
    fun deleteAllUsers()
}