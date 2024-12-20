// Generated by view binder compiler. Do not edit!
package com.example.shoesstore.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.shoesstore.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemCouponBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView couponCode;

  @NonNull
  public final TextView couponDiscount;

  @NonNull
  public final TextView couponEndDate;

  @NonNull
  public final TextView couponStartDate;

  private ItemCouponBinding(@NonNull LinearLayout rootView, @NonNull TextView couponCode,
      @NonNull TextView couponDiscount, @NonNull TextView couponEndDate,
      @NonNull TextView couponStartDate) {
    this.rootView = rootView;
    this.couponCode = couponCode;
    this.couponDiscount = couponDiscount;
    this.couponEndDate = couponEndDate;
    this.couponStartDate = couponStartDate;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemCouponBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemCouponBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_coupon, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemCouponBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.couponCode;
      TextView couponCode = ViewBindings.findChildViewById(rootView, id);
      if (couponCode == null) {
        break missingId;
      }

      id = R.id.couponDiscount;
      TextView couponDiscount = ViewBindings.findChildViewById(rootView, id);
      if (couponDiscount == null) {
        break missingId;
      }

      id = R.id.couponEndDate;
      TextView couponEndDate = ViewBindings.findChildViewById(rootView, id);
      if (couponEndDate == null) {
        break missingId;
      }

      id = R.id.couponStartDate;
      TextView couponStartDate = ViewBindings.findChildViewById(rootView, id);
      if (couponStartDate == null) {
        break missingId;
      }

      return new ItemCouponBinding((LinearLayout) rootView, couponCode, couponDiscount,
          couponEndDate, couponStartDate);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
