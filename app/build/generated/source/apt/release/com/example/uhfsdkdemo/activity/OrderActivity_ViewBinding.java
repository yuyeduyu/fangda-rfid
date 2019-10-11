// Generated code from Butter Knife. Do not modify!
package com.example.uhfsdkdemo.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.example.uhfsdkdemo.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderActivity_ViewBinding<T extends OrderActivity> implements Unbinder {
  protected T target;

  private View view2131689659;

  @UiThread
  public OrderActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.storeHousePtrFrame = Utils.findRequiredViewAsType(source, R.id.store_house_ptr_frame, "field 'storeHousePtrFrame'", PtrClassicFrameLayout.class);
    target.filterEdit = Utils.findRequiredViewAsType(source, R.id.filter_edit, "field 'filterEdit'", EditText.class);
    view = Utils.findRequiredView(source, R.id.search, "field 'search' and method 'onViewClicked'");
    target.search = Utils.castView(view, R.id.search, "field 'search'", ImageView.class);
    view2131689659 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.recyclerview = Utils.findRequiredViewAsType(source, R.id.recyclerview, "field 'recyclerview'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.storeHousePtrFrame = null;
    target.filterEdit = null;
    target.search = null;
    target.recyclerview = null;

    view2131689659.setOnClickListener(null);
    view2131689659 = null;

    this.target = null;
  }
}
