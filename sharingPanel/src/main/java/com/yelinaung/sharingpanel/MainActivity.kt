package com.yelinaung.sharingpanel

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), ShareAdapter.ItemListener {

  var items: List<ShareAdapter.ShareItem> = ArrayList()
  var isImage = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val shareAdapter: ShareAdapter = ShareAdapter()
    shareAdapter.itemListener = this
    rvSharePanel.adapter = shareAdapter
    rvSharePanel.setHasFixedSize(true)
    rvSharePanel.addItemDecoration(ShareAdapter.MarginDecoration(this))
    // forget builder pattern. long live named arguments!
    items = getShareableItems(context = this, isImage = isImage)
    shareAdapter.setData(items)

    val bottomSheetBehavior = BottomSheetBehavior.from<View>(rvSharePanel)
    bottomSheetBehavior.isHideable = true
    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onStateChanged(bottomSheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
          finish()
        }
      }

      override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    })
  }

  override fun clickOnItem(position: Int) {
    val shareItem: ShareAdapter.ShareItem = items[position]

    // string interpolation
    Log.d("click", "click on ${shareItem.appName}")

    val shareIntent = Intent()

    // grave accent in Kotlin https://stackoverflow.com/q/45066128
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.`package` = shareItem.packageId
    if (isImage) {
      shareIntent.type = "image/*"
    } else {
      shareIntent.type = "text/plain"
    }

    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Kotlin!")
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Kotlin is not bad at all!")
    startActivity(shareIntent)
  }

  fun getShareableItems(context: Context, isImage: Boolean): List<ShareAdapter.ShareItem> {
    val shareIntent: Intent = Intent(Intent.ACTION_SEND, null)
    if (isImage) {
      shareIntent.type = "image/*"
    } else {
      shareIntent.type = "text/plain"
    }

    // kv pair of app name and package name itself
    val shareableAppPackages = TreeMap<String, String>()
    val resInfo =
        applicationContext.packageManager.queryIntentActivities(shareIntent,
            PackageManager.GET_RESOLVED_FILTER)

    for (resolveInfo in resInfo) {
      shareableAppPackages.put(
          this.getAppNameByPackage(context, resolveInfo.activityInfo.packageName),
          resolveInfo.activityInfo.packageName)
    }

    val items: MutableList<ShareAdapter.ShareItem> = ArrayList()

    // kv loop ? shut up and take my money!
    for ((key, value) in shareableAppPackages) {
      try {
        val shareItem: ShareAdapter.ShareItem = ShareAdapter.ShareItem(appName = key,
            packageId = value,
            icon = context.packageManager.getApplicationIcon(value))
        items.add(shareItem)
      } catch (e: Throwable) {
        Log.e(this.localClassName, "Cannot load name for $key", e)
      }
    }
    return items
  }

  fun getAppNameByPackage(context: Context, packageName: String): String {
    val pm = context.packageManager
    val ai: ApplicationInfo?
    ai = pm.getApplicationInfo(packageName, 0)

    return (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String
  }

}
