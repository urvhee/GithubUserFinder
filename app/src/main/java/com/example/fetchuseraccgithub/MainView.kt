package com.example.fetchuseraccgithub

import com.example.fetchuseraccgithub.model.ResponseUser
import com.example.fetchuseraccgithub.model.ResponseUsers

interface MainView {
    fun onSuccess(data: ResponseUsers?)
    fun onError(msg: String)
    fun onShowLoading()
    fun onHideLoading()
}