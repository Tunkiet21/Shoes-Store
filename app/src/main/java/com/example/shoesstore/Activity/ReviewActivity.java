package com.example.shoesstore.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shoesstore.Activity.DetailProductActivity;
import com.bumptech.glide.Glide;
import com.example.shoesstore.Domain.Product;
import com.example.shoesstore.Domain.Review;
import com.example.shoesstore.R;
import com.example.shoesstore.utilities.Constants;
import com.example.shoesstore.utilities.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private DatabaseReference reviewsRef;
    private TextView textViewComment, productTitleTextView, productDescriptionTextView, usernameTextView;
    private ImageView productImageView;
    private RatingBar ratingBarReview;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Tìm và thiết lập nút back
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> onBackPressed());

        // Các phần khởi tạo khác như trước
        preferenceManager = new PreferenceManager(getApplicationContext());
        reviewsRef = FirebaseDatabase.getInstance().getReference(Constants.KEY_COLLECTION_REVIEWS);

        // Khởi tạo các thành phần UI
        productImageView = findViewById(R.id.productImageView);
        productTitleTextView = findViewById(R.id.productTitleTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        textViewComment = findViewById(R.id.textViewComment);
        usernameTextView = findViewById(R.id.usernameTextView);
        ratingBarReview = findViewById(R.id.ratingBarReview);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Hiển thị thông tin sản phẩm và người dùng
        displayProductInfo();
        displayUsername();

        // Thiết lập sự kiện cho nút submit
        setListeners();
    }


    private void displayProductInfo() {
        // Nhận thông tin sản phẩm từ Intent
        String productImage = getIntent().getStringExtra("productImage");
        String productTitle = getIntent().getStringExtra("productTitle");
        String productDescription = getIntent().getStringExtra("productDescription");

        // Kiểm tra dữ liệu trước khi hiển thị
        if (productImage != null) {
            Glide.with(this).load(productImage).into(productImageView);
        }
        productTitleTextView.setText(productTitle != null ? productTitle : "No title available");
        productDescriptionTextView.setText(productDescription != null ? productDescription : "No description available");
    }


    private void displayUsername() {
        // Hiển thị tên người dùng đang đăng nhập
        String username = preferenceManager.getString(Constants.KEY_NAME);
        usernameTextView.setText(username);
    }

    private void setListeners() {
        buttonSubmit.setOnClickListener(v -> {
            String comment = textViewComment.getText().toString().trim();
            float rating = ratingBarReview.getRating();

            if (TextUtils.isEmpty(comment)) {
                Toast.makeText(ReviewActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            } else if (rating == 0) {
                Toast.makeText(ReviewActivity.this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            } else {
                submitReview(comment, rating);
            }
        });
    }

    private void submitReview(String comment, float rating) {
        String username = preferenceManager.getString(Constants.KEY_NAME);
        String productTitle = getIntent().getStringExtra("productTitle"); // Nhận tên sản phẩm từ Intent
        String productId = getIntent().getStringExtra("productId"); // Nhận ID sản phẩm từ Intent

        // Tạo đối tượng Review với tên sản phẩm
        Review review = new Review(username,productId, productTitle, comment, rating);

        // Đẩy thông tin lên Firebase
        reviewsRef.push().setValue(review)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ReviewActivity.this, "Review submitted", Toast.LENGTH_SHORT).show();
                    textViewComment.setText(""); // Xóa nội dung bình luận sau khi gửi thành công
                    ratingBarReview.setRating(0); // Đặt lại rating sau khi gửi thành công


                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ReviewActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                });
    }





}
