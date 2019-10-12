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

public class AllLogAdapter$Holder_ViewBinding<T extends AllLogAdapter.Holder> implements Unbinder {
  protected T target;

  @UiThread
  public AllLogAdapter$Holder_ViewBinding(T target, View source) {
    this.target = target;

    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.pNum = Utils.findRequiredViewAsType(source, R.id.pNum, "field 'pNum'", TextView.class);
    target.mNum = Utils.findRequiredViewAsType(source, R.id.mNum, "field 'mNum'", TextView.class);
    target.inTime = Utils.findRequiredViewAsType(source, R.id.inTime, "field 'inTime'", TextView.class);
    target.outTime = Utils.findRequiredViewAsType(source, R.id.outTime, "field 'outTime'", TextView.class);
    target.color = Utils.findRequiredViewAsType(source, R.id.color, "field 'color'", TextView.class);
    target.factroy = Utils.findRequiredViewAsType(source, R.id.factroy, "field 'factroy'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.name = null;
    target.pNum = null;
    target.mNum = null;
    target.inTime = null;
    target.outTime = null;
    target.color = null;
    target.factroy = null;

    this.target = null;
  }
}
