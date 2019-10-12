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

public class SettlementAdapter$Holder_ViewBinding<T extends SettlementAdapter.Holder> implements Unbinder {
  protected T target;

  @UiThread
  public SettlementAdapter$Holder_ViewBinding(T target, View source) {
    this.target = target;

    target.productName = Utils.findRequiredViewAsType(source, R.id.productName, "field 'productName'", TextView.class);
    target.priceAdjustment = Utils.findRequiredViewAsType(source, R.id.priceAdjustment, "field 'priceAdjustment'", TextView.class);
    target.isIncludeTax = Utils.findRequiredViewAsType(source, R.id.isIncludeTax, "field 'isIncludeTax'", TextView.class);
    target.supplierName = Utils.findRequiredViewAsType(source, R.id.supplierName, "field 'supplierName'", TextView.class);
    target.deliverySupplierDate = Utils.findRequiredViewAsType(source, R.id.deliverySupplierDate, "field 'deliverySupplierDate'", TextView.class);
    target.numberOfCheckouts = Utils.findRequiredViewAsType(source, R.id.numberOfCheckouts, "field 'numberOfCheckouts'", TextView.class);
    target.mmi = Utils.findRequiredViewAsType(source, R.id.mmi, "field 'mmi'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.productName = null;
    target.priceAdjustment = null;
    target.isIncludeTax = null;
    target.supplierName = null;
    target.deliverySupplierDate = null;
    target.numberOfCheckouts = null;
    target.mmi = null;

    this.target = null;
  }
}
