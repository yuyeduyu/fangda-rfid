// Generated code from Butter Knife. Do not modify!
package com.example.uhfsdkdemo.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.uhfsdkdemo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChukuAdapter$Holder_ViewBinding<T extends ChukuAdapter.Holder> implements Unbinder {
  protected T target;

  @UiThread
  public ChukuAdapter$Holder_ViewBinding(T target, View source) {
    this.target = target;

    target.time = Utils.findRequiredViewAsType(source, R.id.time, "field 'time'", TextView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.color = Utils.findRequiredViewAsType(source, R.id.color, "field 'color'", TextView.class);
    target.length = Utils.findRequiredViewAsType(source, R.id.length, "field 'length'", TextView.class);
    target.code = Utils.findRequiredViewAsType(source, R.id.code, "field 'code'", TextView.class);
    target.shrinkage = Utils.findRequiredViewAsType(source, R.id.shrinkage, "field 'shrinkage'", TextView.class);
    target.deviceName = Utils.findRequiredViewAsType(source, R.id.deviceName, "field 'deviceName'", TextView.class);
    target.storeroom = Utils.findRequiredViewAsType(source, R.id.storeroom, "field 'storeroom'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.time = null;
    target.name = null;
    target.color = null;
    target.length = null;
    target.code = null;
    target.shrinkage = null;
    target.deviceName = null;
    target.storeroom = null;

    this.target = null;
  }
}
