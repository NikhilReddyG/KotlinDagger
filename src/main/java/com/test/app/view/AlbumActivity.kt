package com.test.app.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.test.app.R
import com.test.app.api.APIInterface
import com.test.app.app.BaseApplication
import com.test.app.di.components.AlbumComponent
import com.test.app.di.components.ApplicationComponent
import com.test.app.di.components.DaggerAlbumComponent
import com.test.app.di.module.AlbumActivityModule
import com.test.app.di.qualifier.ApplicationContext
import com.test.app.model.Album
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AlbumActivity : Activity(), AlbumAdapter.ClickListener {

    @set:Inject
    var mApiInterface: APIInterface? = null

    @set:Inject
    var mAlbumAdapter: AlbumAdapter? = null

   /* @set:Inject
    @ApplicationContext
    var mContext: Context? = null*/

    private lateinit var mAlbumComponent: AlbumComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        initSetUpDagger()
        initSetUpViews()
    }

    private fun initSetUpDagger() {

        val mApplicationComponent = BaseApplication.get(this).getApplicationComponent()

        mAlbumComponent = DaggerAlbumComponent.builder()
                .albumActivityModule(AlbumActivityModule(this))
                .applicationComponent(mApplicationComponent).build()
        mAlbumComponent.injectAlbumActivity(this)
    }

    private fun initSetUpViews() {

        val mRecyclerView = findViewById<RecyclerView>(R.id.albumRecyclerView) as RecyclerView
        val mLinearLayoutManger = LinearLayoutManager(this)
        mLinearLayoutManger.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = mLinearLayoutManger


        mRecyclerView.adapter = mAlbumAdapter

        mApiInterface!!.fetchAlbums().enqueue(object : Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {

                println("Nikhil 111::>" + response.body()!!)
                mAlbumAdapter!!.setData(response.body()!!)
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                println("Nikhil 3333::>" + t.message)
            }
        })

    }


    override fun launchIntent(ablumsName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}