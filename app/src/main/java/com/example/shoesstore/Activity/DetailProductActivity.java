package com.example.shoesstore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

//Định dạng VND
import java.text.NumberFormat;
import java.util.Locale;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoesstore.Adapter.SizeAdapter;
import com.example.shoesstore.Domain.CartItem;

import com.example.shoesstore.Domain.Product;
import com.example.shoesstore.Domain.Review;
import com.example.shoesstore.databinding.ActivityDetailProductBinding;
import com.example.shoesstore.utilities.CartManager;

//
import com.example.shoesstore.utilities.Constants;
import com.example.shoesstore.utilities.PreferenceManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;
    private SizeAdapter sizeAdapter;
    private CartManager cartManager;
    private String selectedSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        cartManager = new CartManager(this);

        // Lấy email được truyền từ MainActivity
        String userEmail = getIntent().getStringExtra("user_email");

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            // Định dạng giá tiền theo VND
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Hiển thị tên sản phẩm
            Glide.with(this).load(product.getPicUrl()).into(binding.productImage);
            binding.productTitle.setText(product.getTitle());

            // Hiển thị mô tả sản phẩm
            binding.desTxt.setText(product.getDescription());

            // Hiển thị giá theo VND
            binding.productPrice.setText(format.format(product.getPrice()));

            // Set up RecyclerView for sizes
            binding.sizeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            sizeAdapter = new SizeAdapter(product.getSizes(), size -> {
                selectedSize = size; // Cập nhật kích thước được chọn
            });
            binding.sizeRecyclerView.setAdapter(sizeAdapter);






            // Xử lý nút Thêm vào giỏ hàng
            binding.addToCartButton.setOnClickListener(view -> {
                if (selectedSize == null) {
                    Toast.makeText(this, "Vui lòng chọn kích thước", Toast.LENGTH_SHORT).show();
                    return;
                }
                CartItem cartItem = new CartItem(product.getId(), product.getTitle(), product.getPrice(), 1, selectedSize, product.getPicUrl());
                cartManager.addToCart(cartItem);
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            });



            displayAverageRating(product.getId());
            setListeners();
        }
    }
    private void displayAverageRating(String productId) {
        FirebaseDatabase.getInstance().getReference("reviews")
                .orderByChild("productId").equalTo(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        float totalRating = 0;
                        int count = 0;

                        for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                            Review review = reviewSnapshot.getValue(Review.class);
                            if (review != null) {
                                totalRating += review.getRating();
                                count++;
                            }
                        }

                        if (count > 0) {
                            float averageRating = totalRating / count;
                            binding.productRating.setRating(averageRating); // Hiển thị trung bình lên RatingBar
                            binding.productRatingCount.setText(String.format("%.1f", averageRating)); // Hiển thị số sao trung bình
                        } else {
                            binding.productRatingCount.setText("Chưa có đánh giá");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailProductActivity.this, "Không tải được đánh giá", Toast.LENGTH_SHORT).show();
                    }
                });

        // Lắng nghe sự thay đổi mới trong đánh giá (thêm, sửa, xóa)
        FirebaseDatabase.getInstance().getReference("reviews")
                .orderByChild("productId").equalTo(productId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                        // Cập nhật rating sau khi có đánh giá mới
                        updateAverageRating(productId);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                        // Cập nhật rating khi đánh giá bị thay đổi
                        updateAverageRating(productId);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        // Cập nhật rating khi đánh giá bị xóa
                        updateAverageRating(productId);
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                        // Không sử dụng trong trường hợp này
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailProductActivity.this, "Không tải được đánh giá", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateAverageRating(String productId) {
        FirebaseDatabase.getInstance().getReference("reviews")
                .orderByChild("productId").equalTo(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        float totalRating = 0;
                        int count = 0;

                        for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                            Review review = reviewSnapshot.getValue(Review.class);
                            if (review != null) {
                                totalRating += review.getRating();
                                count++;
                            }
                        }

                        if (count > 0) {
                            float averageRating = totalRating / count;
                            binding.productRating.setRating(averageRating); // Hiển thị trung bình lên RatingBar
                            binding.productRatingCount.setText(String.format("%.1f", averageRating)); // Hiển thị số sao trung bình
                        } else {
                            binding.productRatingCount.setText("Chưa có đánh giá");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailProductActivity.this, "Không tải được đánh giá", Toast.LENGTH_SHORT).show();
                    }
                });
    }








    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        // Navigate to the ReviewActivity
        Product product = (Product) getIntent().getSerializableExtra("product");
        binding.imageReview.setOnClickListener(v -> {
            Intent intent = new Intent(DetailProductActivity.this, ReviewActivity.class);
            intent.putExtra("productImage", product.getPicUrl());
            intent.putExtra("productTitle", product.getTitle());
            intent.putExtra("productDescription", product.getDescription());

            intent.putExtra("productId",product.getId());


            startActivity(intent);
        });

    }

}