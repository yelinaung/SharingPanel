package com.yelinaung.sharingpanel

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Autofit Recycler View from Chiuki
 *
 *  @see <a href="https://github.com/chiuki/android-recyclerview">
 *    Android RecyclerView </a>
 *
 * Created by yelinaung on 3/8/17.
 */
class AutoFitRV : RecyclerView {

  private var manager: GridLayoutManager? = null
  private var columnWidth = -1

  constructor(context: Context) : super(context) {
    init(context, null)
  }

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    init(context, attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context,
      attributeSet, defStyle) {
    init(context, attributeSet)
  }

  fun init(context: Context, attributeSet: AttributeSet?) {
    if (attributeSet != null) {
      val attrsArray = intArrayOf(android.R.attr.columnWidth)
      val array = context.obtainStyledAttributes(attributeSet, attrsArray)
      columnWidth = array.getDimensionPixelSize(0, -1)
      array.recycle()
    }
    manager = GridLayoutManager(context, 1)
    layoutManager = manager
  }

  override fun onMeasure(widthSpec: Int, heightSpec: Int) {
    super.onMeasure(widthSpec, heightSpec)
    if (columnWidth > 0) {
      val spanCount: Int = Math.max(1, measuredWidth / columnWidth)
      manager?.spanCount = spanCount
    }
  }

}