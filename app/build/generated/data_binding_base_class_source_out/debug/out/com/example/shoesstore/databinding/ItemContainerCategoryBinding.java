// Generated by view binder compiler. Do not edit!
package com.example.shoesstore.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.shoesstore.R;
import com.makeramen.roundedimageview.RoundedImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemContainerCategoryBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView imageIcon;

  @NonNull
  public final RoundedImageView imageProfile;

  @NonNull
  public final TextView textName;

  @NonNull
  public final View viewSupporter;

  private ItemContainerCategoryBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView imageIcon, @NonNull RoundedImageView imageProfile,
      @NonNull TextView textName, @NonNull View viewSupporter) {
    this.rootView = rootView;
    this.imageIcon = imageIcon;
    this.imageProfile = imageProfile;
    this.textName = textName;
    this.viewSupporter = viewSupporter;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemContainerCategoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemContainerCategoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_container_category, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemContainerCategoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageIcon;
      ImageView imageIcon = ViewBindings.findChildViewById(rootView, id);
      if (imageIcon == null) {
        break missingId;
      }

      id = R.id.imageProfile;
      RoundedImageView imageProfile = ViewBindings.findChildViewById(rootView, id);
      if (imageProfile == null) {
        break missingId;
      }

      id = R.id.textName;
      TextView textName = ViewBindings.findChildViewById(rootView, id);
      if (textName == null) {
        break missingId;
      }

      id = R.id.viewSupporter;
      View viewSupporter = ViewBindings.findChildViewById(rootView, id);
      if (viewSupporter == null) {
        break missingId;
      }

      return new ItemContainerCategoryBinding((ConstraintLayout) rootView, imageIcon, imageProfile,
          textName, viewSupporter);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
