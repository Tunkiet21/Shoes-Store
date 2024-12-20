// Generated by view binder compiler. Do not edit!
package com.example.shoesstore.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.shoesstore.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemPedingCustomerContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton cancelButton;

  @NonNull
  public final ImageView itemImage;

  @NonNull
  public final TextView itemPrice;

  @NonNull
  public final TextView orderId;

  @NonNull
  public final TextView txtAddress;

  private ItemPedingCustomerContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton cancelButton, @NonNull ImageView itemImage,
      @NonNull TextView itemPrice, @NonNull TextView orderId, @NonNull TextView txtAddress) {
    this.rootView = rootView;
    this.cancelButton = cancelButton;
    this.itemImage = itemImage;
    this.itemPrice = itemPrice;
    this.orderId = orderId;
    this.txtAddress = txtAddress;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemPedingCustomerContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemPedingCustomerContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_peding_customer_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemPedingCustomerContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cancel_button;
      AppCompatButton cancelButton = ViewBindings.findChildViewById(rootView, id);
      if (cancelButton == null) {
        break missingId;
      }

      id = R.id.item_image;
      ImageView itemImage = ViewBindings.findChildViewById(rootView, id);
      if (itemImage == null) {
        break missingId;
      }

      id = R.id.item_price;
      TextView itemPrice = ViewBindings.findChildViewById(rootView, id);
      if (itemPrice == null) {
        break missingId;
      }

      id = R.id.order_id;
      TextView orderId = ViewBindings.findChildViewById(rootView, id);
      if (orderId == null) {
        break missingId;
      }

      id = R.id.txtAddress;
      TextView txtAddress = ViewBindings.findChildViewById(rootView, id);
      if (txtAddress == null) {
        break missingId;
      }

      return new ItemPedingCustomerContainerBinding((ConstraintLayout) rootView, cancelButton,
          itemImage, itemPrice, orderId, txtAddress);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
