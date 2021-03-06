package com.fxc.pics.pic.picDetail

import android.util.Log
import com.fxc.pics.common.base.BasePresenter
import com.fxc.pics.pic.network.DataSource
import com.fxc.pics.pic.network.entities.PicDetailEntity
import com.fxc.pics.pic.network.getPhotoDetail

/**
 *
 * @author fxc
 * @date 2018/2/22
 */
class PicDetailPresenterImp(view: PicDetailActivity) : BasePresenter<PicDetailActivity>(view) {
	override fun onCreate() {
		super.onCreate()
		val intent = view.intent
		val id = intent.getStringExtra(PicDetailActivity.KEY_PIC_ID)

	}

	override fun onPause() {
		super.onPause()
	}
}