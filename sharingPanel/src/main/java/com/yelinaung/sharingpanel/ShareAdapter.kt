package com.yelinaung.sharingpanel

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_share_item.view.*

/**
 * Created by yelinaung on 3/8/17.
 */
class ShareAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var shareItems: List<ShareItem> = ArrayList()
  var itemListener: ShareAdapter.ItemListener? = null

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    if (holder is ShareViewHolder) {
      holder.bindShareItem(shareItems[position], itemListener)
    }
  }

  override fun getItemCount() = shareItems.size

  fun setData(shareItems: List<ShareItem>) {
    this.shareItems = shareItems
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_share_item, parent,
        false)
    return ShareViewHolder(view)
  }

  class ShareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindShareItem(shareItem: ShareItem, itemListener: ItemListener?) {
      with(shareItem) {
        itemView.tvTitle.text = shareItem.appName
        itemView.ivLogo.setBackgroundDrawable(shareItem.icon)
        if (itemListener != null) {
          itemView.setOnClickListener({
            itemListener.clickOnItem(adapterPosition)
          })
        }
      }
    }
  }

  interface ItemListener {
    fun clickOnItem(position: Int)
  }

  data class ShareItem(val appName: String, val packageId: String, val icon: Drawable)

  class MarginDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val margin = context.resources.getDimensionPixelSize(R.dimen.item_margin)
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
      outRect.set(margin, margin, margin, margin)
    }
  }

}
