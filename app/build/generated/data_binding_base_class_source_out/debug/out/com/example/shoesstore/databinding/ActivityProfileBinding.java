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

public final class ActivityProfileBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout btnChangePassword;

  @NonNull
  public final LinearLayout btnLogout;

  @NonNull
  public final LinearLayout contactOption;

  @NonNull
  public final TextView emailTxt;

  @NonNull
  public final TextView txtTitle;

  private ActivityProfileBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout btnChangePassword, @NonNull LinearLayout btnLogout,
      @NonNull LinearLayout contactOption, @NonNull TextView emailTxt, @NonNull TextView txtTitle) {
    this.rootView = rootView;
    this.btnChangePassword = btnChangePassword;
    this.btnLogout = btnLogout;
    this.contactOption = contactOption;
    this.emailTxt = emailTxt;
    this.txtTitle = txtTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnChangePassword;
      LinearLayout btnChangePassword = ViewBindings.findChildViewById(rootView, id);
      if (btnChangePassword == null) {
        break missingId;
      }

      id = R.id.btnLogout;
      LinearLayout btnLogout = ViewBindings.findChildViewById(rootView, id);
      if (btnLogout == null) {
        break missingId;
      }

      id = R.id.contactOption;
      LinearLayout contactOption = ViewBindings.findChildViewById(rootView, id);
      if (contactOption == null) {
        break missingId;
      }

      id = R.id.emailTxt;
      TextView emailTxt = ViewBindings.findChildViewById(rootView, id);
      if (emailTxt == null) {
        break missingId;
      }

      id = R.id.txtTitle;
      TextView txtTitle = ViewBindings.findChildViewById(rootView, id);
      if (txtTitle == null) {
        break missingId;
      }

      return new ActivityProfileBinding((LinearLayout) rootView, btnChangePassword, btnLogout,
          contactOption, emailTxt, txtTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
