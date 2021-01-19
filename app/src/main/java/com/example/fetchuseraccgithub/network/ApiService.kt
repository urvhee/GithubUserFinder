package com.example.fetchuseraccgithub.network

import com.example.fetchuseraccgithub.model.ResponseUser
import com.example.fetchuseraccgithub.model.ResponseUsers
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUsers(
        @Query("q") q: String= "",
        @Query("page") page: Int = 0,
        @Query("per_page") per_page: Int = 10): Observable<ResponseUsers>
}