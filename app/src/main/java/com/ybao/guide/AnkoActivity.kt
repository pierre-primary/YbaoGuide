package com.ybao.guide

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import com.ybao.guide.helper.StandardAdapter
import com.ybao.guide.utils.createTagView
import com.ybao.guide.utils.showToastMsg
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by Y-bao on 2017/11/22 0022.
 */

class AnkoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var rv = recyclerView {
            lparams {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            layoutManager = GridLayoutManager(this@AnkoActivity, 2)
        }
        rv.adapter = adapter

        val guide = Guide.Builder(this)
                .setBackgroundColor(Color.parseColor("#aa000000"))
                .addIndicator(object : Indicator() {
                    var listener = ViewTreeObserver.OnGlobalLayoutListener {
                        targetView = rv.getChildAt(10)
                    }

                    override fun onAttached() {
                        super.onAttached()
                        rv.viewTreeObserver.addOnGlobalLayoutListener(listener);
                    }

                    override fun onDetached() {
                        super.onDetached()
                        rv.viewTreeObserver.removeOnGlobalLayoutListener(listener);
                    }
                })
                .addAttachedView(
                        AttachedView(createTagView("可监听延时加载的控件", 2))
                                .setGravityX(AttachedView.GRAVITY_TO_TARGET, Gravity.CENTER_HORIZONTAL)
                                .setGravityY(AttachedView.GRAVITY_TO_TARGET, Gravity.TOP)
                )
                .addAttachedView(
                        AttachedView(createTagView("无需考虑宽高计算", 4))
                                .setGravityX(AttachedView.GRAVITY_TO_TARGET, Gravity.CENTER_HORIZONTAL)
                                .setGravityY(AttachedView.GRAVITY_TO_TARGET, Gravity.BOTTOM)
                )
                .complete()
                .create()

        guide.setOnClickIndicatorListener { idt ->
            showToastMsg("Click " + idt?.tag)
            true
        }
        guide.show()

        Handler(Looper.getMainLooper()).postDelayed({
            var list = ArrayList<String>()
            for (i in 0..100) {
                list.add("$i")
            }
            adapter.data = list;
        }, 2000)
    }


    var adapter = object : StandardAdapter<StandardAdapter.ItemViewHolder, String>() {
        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            super.onBindViewHolder(holder, position)
            holder?.itemView?.find<TextView>(0x00000001)?.text = "item : " + getItemData(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StandardAdapter.ItemViewHolder {
            return StandardAdapter.ItemViewHolder(UI {
                linearLayout {
                    padding = dip(8)
                    gravity = Gravity.CENTER_VERTICAL
                    orientation = LinearLayout.HORIZONTAL
                    imageView {
                        lparams {
                            width = dip(50)
                            height = dip(50)
                        }
                        backgroundResource = R.mipmap.ic_launcher
                    }
                    textView {
                        id = 0x00000001
                        lparams {
                            leftMargin = dip(8)
                        }
                        singleLine = true
                        ellipsize = TextUtils.TruncateAt.END
                    }
                }
            }.view)
        }
    }
}