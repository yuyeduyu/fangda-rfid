// Generated code from Butter Knife. Do not modify!
package com.example.uhfsdkdemo.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.example.uhfsdkdemo.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettlementActivity_ViewBinding<T extends SettlementActivity> implements Unbinder {
  protected T target;

  private View view2131755166;

  private View view2131755165;

  private View view2131755163;

  @UiThread
  public SettlementActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.pName = Utils.findRequiredViewAsType(source, R.id.pName, "field 'pName'", EditText.class);
    target.tvStartTime = Utils.findRequiredViewAsType(source, R.id.tv_startTime, "field 'tvStartTime'", TextView.class);
    target.tvEndTime = Utils.findRequiredViewAsType(source, R.id.tv_endTime, "field 'tvEndTime'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_Search, "field 'tvSearch' and method 'onViewClicked'");
    target.tvSearch = Utils.castView(view, R.id.tv_Search, "field 'tvSearch'", TextView.class);
    view2131755166 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.recyclerview = Utils.findRequiredViewAsType(source, R.id.recyclerview, "field 'recyclerview'", RecyclerView.class);
    target.storeHousePtrFrame = Utils.findRequiredViewAsType(source, R.id.store_house_ptr_frame, "field 'storeHousePtrFrame'", PtrClassicFrameLayout.class);
    view = Utils.findRequiredView(source, R.id.ll_time, "method 'onViewClicked'");
    view2131755165 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.im_back, "method 'onViewClicked'");
    view2131755163 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.pName = null;
    target.tvStartTime = null;
    target.tvEndTime = null;
    target.tvSearch = null;
    target.recyclerview = null;
    target.storeHousePtrFrame = null;

    view2131755166.setOnClickListener(null);
    view2131755166 = null;
    view2131755165.setOnClickListener(null);
    view2131755165 = null;
    view2131755163.setOnClickListener(null);
    view2131755163 = null;

    this.target = null;
  }
}
