// Generated code from Butter Knife. Do not modify!
package com.example.uhfsdkdemo.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.uhfsdkdemo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChukuInfoActivity_ViewBinding<T extends ChukuInfoActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ChukuInfoActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", Toolbar.class);
    target.tvCode = Utils.findRequiredViewAsType(source, R.id.tv_code, "field 'tvCode'", TextView.class);
    target.tvDevname = Utils.findRequiredViewAsType(source, R.id.tv_devname, "field 'tvDevname'", TextView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    target.tvOrderNo = Utils.findRequiredViewAsType(source, R.id.tv_orderNo, "field 'tvOrderNo'", TextView.class);
    target.tvTimeKct = Utils.findRequiredViewAsType(source, R.id.tv_timeKct, "field 'tvTimeKct'", TextView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    target.tvColor = Utils.findRequiredViewAsType(source, R.id.tv_color, "field 'tvColor'", TextView.class);
    target.tvLenth = Utils.findRequiredViewAsType(source, R.id.tv_lenth, "field 'tvLenth'", TextView.class);
    target.tvShrinkage = Utils.findRequiredViewAsType(source, R.id.tv_shrinkage, "field 'tvShrinkage'", TextView.class);
    target.tvStoreroom = Utils.findRequiredViewAsType(source, R.id.tv_storeroom, "field 'tvStoreroom'", TextView.class);
    target.tvFrozenMan = Utils.findRequiredViewAsType(source, R.id.tv_frozenMan, "field 'tvFrozenMan'", TextView.class);
    target.tvIsOut = Utils.findRequiredViewAsType(source, R.id.tv_isOut, "field 'tvIsOut'", TextView.class);
    target.tvStartTime = Utils.findRequiredViewAsType(source, R.id.tv_startTime, "field 'tvStartTime'", TextView.class);
    target.tvEndTime = Utils.findRequiredViewAsType(source, R.id.tv_endTime, "field 'tvEndTime'", TextView.class);
    target.tvIsHandFilling = Utils.findRequiredViewAsType(source, R.id.tv_isHandFilling, "field 'tvIsHandFilling'", TextView.class);
    target.tvOutOrderNo = Utils.findRequiredViewAsType(source, R.id.tv_outOrderNo, "field 'tvOutOrderNo'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mToolbar = null;
    target.tvCode = null;
    target.tvDevname = null;
    target.tvTime = null;
    target.tvOrderNo = null;
    target.tvTimeKct = null;
    target.tvName = null;
    target.tvColor = null;
    target.tvLenth = null;
    target.tvShrinkage = null;
    target.tvStoreroom = null;
    target.tvFrozenMan = null;
    target.tvIsOut = null;
    target.tvStartTime = null;
    target.tvEndTime = null;
    target.tvIsHandFilling = null;
    target.tvOutOrderNo = null;

    this.target = null;
  }
}
