package com.example.fetchuseraccgithub

import android.content.Context
import com.example.fetchuseraccgithub.model.ResponseUser
import com.example.fetchuseraccgithub.model.ResponseUsers
import com.example.fetchuseraccgithub.network.ConfigRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Response

class MainPresenter(private var modelView: MainView, private var context: Context) {

    fun getUsers(name: String, page: Int){
        modelView.onShowLoading()
        val compositeDisposable = CompositeDisposable()
        val disposable = ConfigRetrofit.retrofit.getUsers(name, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ r ->
                modelView.onSuccess(r)
                modelView.onHideLoading()
            },{ e ->
                modelView.onError(e.localizedMessage)
                modelView.onHideLoading()
            }, {})
        compositeDisposable.add(disposable)
    }
}