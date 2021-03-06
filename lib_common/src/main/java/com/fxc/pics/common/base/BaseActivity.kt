package com.fxc.pics.common.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.fxc.pics.common.events.EventUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException

/**
 * BaseActivity
 * @author fxc
 * @date 2018/1/10
 */
abstract class BaseActivity : AppCompatActivity() {
	companion object {
		private const val TAG = "BaseActivity"
	}

	fun startActivityByShareElement(params: HashMap<String, String>, clazz: Class<*>, vararg shareElement: Pair<View, String>) {
		val intent = Intent()
		intent.setClass(this, clazz)
		for ((k, v) in params) {
			intent.putExtra(k, v)
		}
		val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *shareElement)
		ActivityCompat.startActivity(this, intent, options.toBundle())
	}

	fun bindShareElement(vararg shareElements: Pair<View, String>) {
		for (element in shareElements) {
			ViewCompat.setTransitionName(element.first, element.second)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		try {
			EventUtil.register(this)
		} catch (exception: EventBusException) {
			Log.w(TAG, "${this.javaClass} has no public methods with the @Subscribe annotation")
		}
		for (app in BaseApplication.getActivityDelegates()) {
			app.onCreate()
		}
		val resId = getContentViewId()
		if (resId != 0)
			setContentView(resId)
		 else
			throw RuntimeException("Please set a correct resId!")
		beforeInitWidget()
		initWidget()
		afterInitWidget()
		fixFrescoBug()
	}

	private fun fixFrescoBug() {
		//Fix for when Fresco use shared element, the original image view disappearing when return to the last Activity on Android N
		//修复 Fresco 在 Android N 平台使用 SharedElement 时，返回上一级 Activity 后 ImageView 消失的问题
		if (Build.VERSION.SDK_INT != Build.VERSION_CODES.N && Build.VERSION.SDK_INT != Build.VERSION_CODES.N_MR1) {
			return
		}
		setExitSharedElementCallback(object : SharedElementCallback() {
			override fun onSharedElementEnd(sharedElementNames: MutableList<String>, sharedElements: MutableList<View>, sharedElementSnapshots: MutableList<View>) {
				super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
				sharedElements
						.filterIsInstance<SimpleDraweeView>()
						.forEach { it.visibility = View.VISIBLE }
			}
		})
	}

	protected open fun beforeInitWidget() {

	}

	protected open fun initWidget() {

	}

	protected open fun afterInitWidget() {

	}

	abstract fun getContentViewId(): Int

	override fun onStart() {
		super.onStart()
		for (app in BaseApplication.getActivityDelegates()) {
			app.onStart()
		}
	}

	override fun onNewIntent(intent: Intent?) {
		super.onNewIntent(intent)
		for (app in BaseApplication.getActivityDelegates()) {
			app.onNewIntent(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		for (app in BaseApplication.getActivityDelegates()) {
			app.onResume()
		}
	}

	override fun onPause() {
		super.onPause()
		for (app in BaseApplication.getActivityDelegates()) {
			app.onPause()
		}
	}

	override fun onStop() {
		super.onStop()
		for (app in BaseApplication.getActivityDelegates()) {
			app.onStop()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		for (app in BaseApplication.getActivityDelegates()) {
			app.onDestroy()
		}
		EventUtil.unregister(this)
	}

}