// Generated by view binder compiler. Do not edit!
package com.example.shoesstore.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.shoesstore.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityReviewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView backIcon;

  @NonNull
  public final Button buttonSubmit;

  @NonNull
  public final LinearLayout headerLayout;

  @NonNull
  public final TextView headerTitle;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView productDescriptionTextView;

  @NonNull
  public final ImageView productImageView;

  @NonNull
  public final TextView productTitleTextView;

  @NonNull
  public final RatingBar ratingBarReview;

  @NonNull
  public final EditText textViewComment;

  @NonNull
  public final TextView usernameTextView;

  private ActivityReviewBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView backIcon,
      @NonNull Button buttonSubmit, @NonNull LinearLayout headerLayout,
      @NonNull TextView headerTitle, @NonNull ConstraintLayout main,
      @NonNull TextView productDescriptionTextView, @NonNull ImageView productImageView,
      @NonNull TextView productTitleTextView, @NonNull RatingBar ratingBarReview,
      @NonNull EditText textViewComment, @NonNull TextView usernameTextView) {
    this.rootView = rootView;
    this.backIcon = backIcon;
    this.buttonSubmit = buttonSubmit;
    this.headerLayout = headerLayout;
    this.headerTitle = headerTitle;
    this.main = main;
    this.productDescriptionTextView = productDescriptionTextView;
    this.productImageView = productImageView;
    this.productTitleTextView = productTitleTextView;
    this.ratingBarReview = ratingBarReview;
    this.textViewComment = textViewComment;
    this.usernameTextView = usernameTextView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityReviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityReviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_review, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityReviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backIcon;
      ImageView backIcon = ViewBindings.findChildViewById(rootView, id);
      if (backIcon == null) {
        break missingId;
      }

      id = R.id.buttonSubmit;
      Button buttonSubmit = ViewBindings.findChildViewById(rootView, id);
      if (buttonSubmit == null) {
        break missingId;
      }

      id = R.id.headerLayout;
      LinearLayout headerLayout = ViewBindings.findChildViewById(rootView, id);
      if (headerLayout == null) {
        break missingId;
      }

      id = R.id.headerTitle;
      TextView headerTitle = ViewBindings.findChildViewById(rootView, id);
      if (headerTitle == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.productDescriptionTextView;
      TextView productDescriptionTextView = ViewBindings.findChildViewById(rootView, id);
      if (productDescriptionTextView == null) {
        break missingId;
      }

      id = R.id.productImageView;
      ImageView productImageView = ViewBindings.findChildViewById(rootView, id);
      if (productImageView == null) {
        break missingId;
      }

      id = R.id.productTitleTextView;
      TextView productTitleTextView = ViewBindings.findChildViewById(rootView, id);
      if (productTitleTextView == null) {
        break missingId;
      }

      id = R.id.ratingBarReview;
      RatingBar ratingBarReview = ViewBindings.findChildViewById(rootView, id);
      if (ratingBarReview == null) {
        break missingId;
      }

      id = R.id.textViewComment;
      EditText textViewComment = ViewBindings.findChildViewById(rootView, id);
      if (textViewComment == null) {
        break missingId;
      }

      id = R.id.usernameTextView;
      TextView usernameTextView = ViewBindings.findChildViewById(rootView, id);
      if (usernameTextView == null) {
        break missingId;
      }

      return new ActivityReviewBinding((ConstraintLayout) rootView, backIcon, buttonSubmit,
          headerLayout, headerTitle, main, productDescriptionTextView, productImageView,
          productTitleTextView, ratingBarReview, textViewComment, usernameTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
