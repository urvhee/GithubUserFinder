package com.example.fetchuseraccgithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchuseraccgithub.adapter.UserAdapter
import com.example.fetchuseraccgithub.model.ResponseUser
import com.example.fetchuseraccgithub.model.ResponseUsers
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.refresh.view.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity(), MainView {

    private var presenter: MainPresenter? = null

    private var username = "a"
    private var page = 1

    private var users: ResponseUsers? = null
    private var user: List<ResponseUser>? = null
    private var mAdapter = GroupAdapter<ViewHolder>()

    private var p = 1
    private var isRefresh = false
    private var isBottom = false
    private var maxPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initPresenter()
        initEvent()
    }

    private fun initView() {
        rv_user.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun initPresenter() {
        presenter = MainPresenter(this, this.applicationContext)
        presenter?.getUsers(username, page)
    }

    private fun initEvent() {

        btn_search.setOnClickListener {
            mAdapter.clear()
            username = search_input.text.toString()
            presenter?.getUsers(username, 1)
        }

        rv_user.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val isBottomReached = !recyclerView!!.canScrollVertically(1)
                if (isBottomReached && (maxPage > p) && (!isRefresh)) {
                    p = p.plus(1)
                    isBottom = true
                    presenter?.getUsers(username, p)
                }
            }
        })

        sr.setOnRefreshListener {

            if (user!!.isEmpty()) {
                mAdapter.clear()
                search_input.text.clear()
                p = 1
                username = "a"
                presenter?.getUsers(username, p)
            }

            isRefresh = true
            p = p.plus(1)
            presenter?.getUsers(username, p)
        }
    }

    override fun onSuccess(data: ResponseUsers?) {
        if (p <= 1) mAdapter.clear()
        users = data
        user = users?.items as List<ResponseUser>?

        user?.forEach {
            mAdapter.add(UserAdapter(it))
        }
        if (user?.size == 0){
//            rv_user.visibility = View.GONE
            refresh.visibility = View.VISIBLE
            rv_user.visibility = View.GONE
            maxPage = 0
        } else {
            if (p > 1) {
                val position = mAdapter.itemCount
                rv_user.layoutManager?.scrollToPosition(position)
            }
            if (maxPage == 0) maxPage = getMaxPage(users?.totalCount!!.toInt())
            rv_user.visibility = View.VISIBLE
            refresh.visibility = View.GONE
        }
    }

    override fun onError(msg: String) {
        triggerAlertDialog(msg)
    }

    override fun onShowLoading() {
        when {
            isRefresh -> {
                sr.isRefreshing = false
                mAdapter.clear()
            }
            isBottom -> progress_bar.visibility = View.VISIBLE
        }
    }

    override fun onHideLoading() {
        when {
            isRefresh -> sr.isRefreshing = false
            isBottom -> progress_bar.visibility = View.GONE
        }

        isRefresh = false
        isBottom = false
    }

    private fun triggerAlertDialog(message: String) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null)

        mDialogView.text_alert_dialog.text = message
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btn_ok.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun getMaxPage(total: Int): Int {
        return ceil(total.toDouble().div(10)).toInt()
    }
}