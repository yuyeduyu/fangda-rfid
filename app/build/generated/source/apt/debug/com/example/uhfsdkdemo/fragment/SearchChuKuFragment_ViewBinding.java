// Generated code from Butter Knife. Do not modify!
package com.example.uhfsdkdemo.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.example.uhfsdkdemo.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchChuKuFragment_ViewBinding<T extends SearchChuKuFragment> implements Unbinder {
  protected T target;

  private View view2131689659;

  private View view2131689656;

  @UiThread
  public SearchChuKuFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.storeHousePtrFrame = Utils.findRequiredViewAsType(source, R.id.store_house_ptr_frame, "field 'storeHousePtrFrame'", PtrClassicFrameLayout.class);
    view = Utils.findRequiredView(source, R.id.search, "field 'search' and method 'onViewClicked'");
    target.search = Utils.castView(view, R.id.search, "field 'search'", ImageView.class);
    view2131689659 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.recyclerview = Utils.findRequiredViewAsType(source, R.id.recyclerview, "field 'recyclerview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.time, "field 'tvTime' and method 'onViewClicked'");
    target.tvTime = Utils.castView(view, R.id.time, "field 'tvTime'", TextView.class);
    view2131689656 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.etName = Utils.findRequiredViewAsType(source, R.id.et_name, "field 'etName'", EditText.class);
    target.etColor = Utils.findRequiredViewAsType(source, R.id.et_color, "field 'etColor'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.storeHousePtrFrame = null;
    target.search = null;
    target.recyclerview = null;
    target.tvTime = null;
    target.etName = null;
    target.etColor = null;

    view2131689659.setOnClickListener(null);
    view2131689659 = null;
    view2131689656.setOnClickListener(null);
    view2131689656 = null;

    this.target = null;
  }
}
