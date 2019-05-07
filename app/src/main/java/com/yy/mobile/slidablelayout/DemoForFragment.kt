package com.yy.mobile.slidablelayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yy.mobile.widget.SlidableUI
import com.yy.mobile.widget.SlideAdapter
import com.yy.mobile.widget.SlideDirection
import com.yy.mobile.widget.SlideFragmentAdapter
import com.yy.mobile.widget.SlideViewHolder
import kotlinx.android.synthetic.main.page_main_content.*

/**
 * Created by 张宇 on 2019/4/15.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
class DemoForFragment : BaseDemoActivity() {

    override fun createAdapter(data: SimpleQueue<PageInfo>): SlideAdapter<out SlideViewHolder> =
        FragmentAdapter(data, supportFragmentManager)

    private class FragmentAdapter(
        private val data: SimpleQueue<PageInfo>, fm: FragmentManager
    ) : SlideFragmentAdapter(fm) {

        override fun onCreateFragment(context: Context): Fragment = DemoFragment()

        override fun onBindFragment(fragment: Fragment, direction: SlideDirection) {
            super.onBindFragment(fragment, direction)
            (fragment as DemoFragment).setCurrentData(getData(direction)!!)
        }

        override fun canSlideTo(direction: SlideDirection): Boolean {
            return getData(direction) != null
        }

        override fun finishSlide(direction: SlideDirection) {
            if (direction == SlideDirection.Next) {
                data.moveToNext()
            } else if (direction == SlideDirection.Prev) {
                data.moveToPrev()
            }
        }

        fun getData(direction: SlideDirection): PageInfo? {
            return if (direction == SlideDirection.Next) {
                data.next()
            } else if (direction == SlideDirection.Prev) {
                data.prev()
            } else {
                data.current()
            }
        }
    }

    class DemoFragment : Fragment(), SlidableUI {

        private var currentInfo: PageInfo? = null

        @SuppressLint("InflateParams")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.page_main_content, null, false)
        }

        fun setCurrentData(data: PageInfo) {
            currentInfo = data
        }

        override fun startVisible(direction: SlideDirection) {
            currentInfo?.let {
                content_title.text = it.title
                content_player.setImageDrawable(resources.getDrawable(it.drawableRes))
            }
        }

        override fun completeVisible(direction: SlideDirection) {
            val drawable = content_player.drawable
            if (drawable is Animatable) {
                drawable.start()
            }
        }

        override fun invisible(direction: SlideDirection) {
            //clean up resource
            content_player.setImageDrawable(null)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.i("SlidableLayout", "onCreate")
        }

        override fun onHiddenChanged(hidden: Boolean) {
            super.onHiddenChanged(hidden)
            Log.i("SlidableLayout", "onHiddenChanged " +
                "${if (hidden) "->hidden" else "->show"} " +
                "$currentInfo")
        }

        override fun onResume() {
            super.onResume()
            Log.i("SlidableLayout", "onResume")
        }

        override fun onStop() {
            super.onStop()
            Log.i("SlidableLayout", "onStop")
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.i("SlidableLayout", "onDestroy")
        }

        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            Log.i("SlidableLayout", "setUserVisibleHint isVisible = $isVisibleToUser $currentInfo")
        }
    }
}