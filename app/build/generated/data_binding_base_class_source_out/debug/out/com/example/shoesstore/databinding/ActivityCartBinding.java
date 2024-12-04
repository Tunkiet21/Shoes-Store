// Generated by view binder compiler. Do not edit!
package com.example.shoesstore.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.shoesstore.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCartBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnCuppoon;

  @NonNull
  public final ImageButton buttonBack;

  @NonNull
  public final Button buttonCheckout;

  @NonNull
  public final RecyclerView cartItems;

  @NonNull
  public final EditText edtCuppon;

  @NonNull
  public final EditText etAddress;

  @NonNull
  public final EditText etPhone;

  @NonNull
  public final LinearLayout layoutAddress;

  @NonNull
  public final LinearLayout layoutCuppon;

  @NonNull
  public final LinearLayout layoutPhone;

  @NonNull
  public final LinearLayout layoutSumary;

  @NonNull
  public final ScrollView srollViewC;

  @NonNull
  public final TextView tvAddress;

  @NonNull
  public final TextView tvPhone;

  @NonNull
  public final TextView txtDelivery;

  @NonNull
  public final TextView txtSubtotal;

  @NonNull
  public final TextView txtTax;

  @NonNull
  public final TextView txtTitle;

  @NonNull
  public final TextView txtTotal;

  private ActivityCartBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnCuppoon,
      @NonNull ImageButton buttonBack, @NonNull Button buttonCheckout,
      @NonNull RecyclerView cartItems, @NonNull EditText edtCuppon, @NonNull EditText etAddress,
      @NonNull EditText etPhone, @NonNull LinearLayout layoutAddress,
      @NonNull LinearLayout layoutCuppon, @NonNull LinearLayout layoutPhone,
      @NonNull LinearLayout layoutSumary, @NonNull ScrollView srollViewC,
      @NonNull TextView tvAddress, @NonNull TextView tvPhone, @NonNull TextView txtDelivery,
      @NonNull TextView txtSubtotal, @NonNull TextView txtTax, @NonNull TextView txtTitle,
      @NonNull TextView txtTotal) {
    this.rootView = rootView;
    this.btnCuppoon = btnCuppoon;
    this.buttonBack = buttonBack;
    this.buttonCheckout = buttonCheckout;
    this.cartItems = cartItems;
    this.edtCuppon = edtCuppon;
    this.etAddress = etAddress;
    this.etPhone = etPhone;
    this.layoutAddress = layoutAddress;
    this.layoutCuppon = layoutCuppon;
    this.layoutPhone = layoutPhone;
    this.layoutSumary = layoutSumary;
    this.srollViewC = srollViewC;
    this.tvAddress = tvAddress;
    this.tvPhone = tvPhone;
    this.txtDelivery = txtDelivery;
    this.txtSubtotal = txtSubtotal;
    this.txtTax = txtTax;
    this.txtTitle = txtTitle;
    this.txtTotal = txtTotal;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCartBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCartBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_cart, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCartBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnCuppoon;
      Button btnCuppoon = ViewBindings.findChildViewById(rootView, id);
      if (btnCuppoon == null) {
        break missingId;
      }

      id = R.id.buttonBack;
      ImageButton buttonBack = ViewBindings.findChildViewById(rootView, id);
      if (buttonBack == null) {
        break missingId;
      }

      id = R.id.buttonCheckout;
      Button buttonCheckout = ViewBindings.findChildViewById(rootView, id);
      if (buttonCheckout == null) {
        break missingId;
      }

      id = R.id.cartItems;
      RecyclerView cartItems = ViewBindings.findChildViewById(rootView, id);
      if (cartItems == null) {
        break missingId;
      }

      id = R.id.edtCuppon;
      EditText edtCuppon = ViewBindings.findChildViewById(rootView, id);
      if (edtCuppon == null) {
        break missingId;
      }

      id = R.id.etAddress;
      EditText etAddress = ViewBindings.findChildViewById(rootView, id);
      if (etAddress == null) {
        break missingId;
      }

      id = R.id.etPhone;
      EditText etPhone = ViewBindings.findChildViewById(rootView, id);
      if (etPhone == null) {
        break missingId;
      }

      id = R.id.layoutAddress;
      LinearLayout layoutAddress = ViewBindings.findChildViewById(rootView, id);
      if (layoutAddress == null) {
        break missingId;
      }

      id = R.id.layoutCuppon;
      LinearLayout layoutCuppon = ViewBindings.findChildViewById(rootView, id);
      if (layoutCuppon == null) {
        break missingId;
      }

      id = R.id.layoutPhone;
      LinearLayout layoutPhone = ViewBindings.findChildViewById(rootView, id);
      if (layoutPhone == null) {
        break missingId;
      }

      id = R.id.layoutSumary;
      LinearLayout layoutSumary = ViewBindings.findChildViewById(rootView, id);
      if (layoutSumary == null) {
        break missingId;
      }

      id = R.id.srollViewC;
      ScrollView srollViewC = ViewBindings.findChildViewById(rootView, id);
      if (srollViewC == null) {
        break missingId;
      }

      id = R.id.tvAddress;
      TextView tvAddress = ViewBindings.findChildViewById(rootView, id);
      if (tvAddress == null) {
        break missingId;
      }

      id = R.id.tvPhone;
      TextView tvPhone = ViewBindings.findChildViewById(rootView, id);
      if (tvPhone == null) {
        break missingId;
      }

      id = R.id.txtDelivery;
      TextView txtDelivery = ViewBindings.findChildViewById(rootView, id);
      if (txtDelivery == null) {
        break missingId;
      }

      id = R.id.txtSubtotal;
      TextView txtSubtotal = ViewBindings.findChildViewById(rootView, id);
      if (txtSubtotal == null) {
        break missingId;
      }

      id = R.id.txtTax;
      TextView txtTax = ViewBindings.findChildViewById(rootView, id);
      if (txtTax == null) {
        break missingId;
      }

      id = R.id.txtTitle;
      TextView txtTitle = ViewBindings.findChildViewById(rootView, id);
      if (txtTitle == null) {
        break missingId;
      }

      id = R.id.txtTotal;
      TextView txtTotal = ViewBindings.findChildViewById(rootView, id);
      if (txtTotal == null) {
        break missingId;
      }

      return new ActivityCartBinding((ConstraintLayout) rootView, btnCuppoon, buttonBack,
          buttonCheckout, cartItems, edtCuppon, etAddress, etPhone, layoutAddress, layoutCuppon,
          layoutPhone, layoutSumary, srollViewC, tvAddress, tvPhone, txtDelivery, txtSubtotal,
          txtTax, txtTitle, txtTotal);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}