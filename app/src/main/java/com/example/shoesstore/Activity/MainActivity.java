package com.example.shoesstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.shoesstore.Adapter.CategoryMainAdapter;

import com.example.shoesstore.Adapter.ProductAdapter;
import com.example.shoesstore.Adapter.ProductMainAdapter;
import com.example.shoesstore.Adapter.SliderAdapter;
import com.example.shoesstore.Domain.Category;

import com.example.shoesstore.Domain.Product;
import com.example.shoesstore.Domain.SliderItems;
import com.example.shoesstore.Listeners.OnProductClickListener;
import com.example.shoesstore.databinding.ActivityMainBinding;
import com.example.shoesstore.utilities.Constants;
import com.example.shoesstore.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnProductClickListener {

    private ActivityMainBinding binding;
    private PreferenceManager preferecnceManager;
    private List<Product> allProductsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Nhận email từ Intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("user_email");

        // Giờ bạn có thể sử dụng email trong activity mà không cần hiển thị nó lên UI.
        if (email != null) {
            // Sử dụng email ở đây cho các mục đích khác, ví dụ:
            Log.d("Email", "Nhận được email: " + email);

        }

        preferecnceManager = new PreferenceManager(getApplicationContext());

        View decorView = getWindow().getDecorView();
        // Ẩn cả thanh điều hướng và thanh trạng thái.
        // ẩn thanh điều hướng.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        initBanner();
        initCategory();
        initAllProducts();
        setListeners();
        setUpSearchFunctionality();

    }



    private void setListeners() {
        binding.heartIcon.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class)));
        binding.profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                preferecnceManager.clear();
//                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
//                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        binding.cartIcon.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            // Lấy email đã nhận từ Intent ban đầu
            Intent receivedIntent = getIntent();
            String email = receivedIntent.getStringExtra("user_email");
            if (email != null) {
                intent.putExtra("user_email", email); // Truyền email qua Intent
            }
            startActivity(intent);
        });
    }





    @Override
    public void onProductClick(Product product) {

        Intent intent = new Intent(this, DetailProductActivity.class);
        intent.putExtra("product",product);



        startActivity(intent);
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference(Constants.KEY_COLLECTION_CATEGORY);
        binding.progressBarOfficial.setVisibility(View.VISIBLE);

        ArrayList<Category> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Category item = issue.getValue(Category.class);
                        items.add(item);
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryMainAdapter(MainActivity.this, items));
                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);

        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        SliderItems item = issue.getValue(SliderItems.class);
                        items.add(item);
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }




    private void setUpSearchFunctionality() {
        binding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần làm gì trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Lọc dữ liệu mỗi khi người dùng nhập
                filterProducts(charSequence.toString());
                // Kiểm tra độ dài của từ khóa tìm kiếm
                if (charSequence.length() > 0) {
                    // Ẩn phần banner và danh mục khi có từ khóa tìm kiếm
                    binding.bannerContainer.setVisibility(View.GONE);
                    binding.textView2.setVisibility(View.GONE); // Text "Thương hiệu"
                    binding.categoryContainer.setVisibility(View.GONE);
                } else {
                    // Hiện lại khi ô tìm kiếm trống
                    binding.bannerContainer.setVisibility(View.VISIBLE);
                    binding.textView2.setVisibility(View.VISIBLE);
                    binding.categoryContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần làm gì sau khi văn bản thay đổi
            }
        });
    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            // Nếu không có từ khóa tìm kiếm, hiển thị tất cả sản phẩm
            displayAllProducts();
            return;
        }

        ArrayList<Product> filteredProducts = new ArrayList<>();
        // Lọc từ danh sách allProductsList
        for (Product product : allProductsList) {
            if (product.getTitle() != null && product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        // Chia filteredProducts thành các nhóm phổ biến và thường
        ArrayList<Product> popularProducts = new ArrayList<>();
        ArrayList<Product> regularProducts = new ArrayList<>();

        for (Product product : filteredProducts) {
            if (product.isHot()) {
                popularProducts.add(product);  // Sản phẩm phổ biến
            } else {
                regularProducts.add(product);  // Sản phẩm thường
            }
        }

        // Cập nhật lại recyclerViewPopular
        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.recyclerViewPopular.setAdapter(new ProductMainAdapter(MainActivity.this, popularProducts, MainActivity.this));

        // Cập nhật lại recyclerViewRegular
        binding.recyclerViewRegular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.recyclerViewRegular.setAdapter(new ProductMainAdapter(MainActivity.this, regularProducts, MainActivity.this));
    }

    private void displayAllProducts() {
        // Hiển thị toàn bộ sản phẩm khi không có từ khóa tìm kiếm
        ArrayList<Product> popularProducts = new ArrayList<>();
        ArrayList<Product> regularProducts = new ArrayList<>();

        for (Product product : allProductsList) {
            if (product.isHot()) {
                popularProducts.add(product);  // Sản phẩm phổ biến
            } else {
                regularProducts.add(product);  // Sản phẩm thường
            }
        }

        // Cập nhật lại recyclerViewPopular
        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.recyclerViewPopular.setAdapter(new ProductMainAdapter(MainActivity.this, popularProducts, MainActivity.this));

        // Cập nhật lại recyclerViewRegular
        binding.recyclerViewRegular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.recyclerViewRegular.setAdapter(new ProductMainAdapter(MainActivity.this, regularProducts, MainActivity.this));
    }
    private void initAllProducts() {
        DatabaseReference myRef = database.getReference(Constants.KEY_COLLECTION_PRODUCTS);
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        binding.progressBarRegular.setVisibility(View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Product product = issue.getValue(Product.class);
                        if (product != null) {
                            allProductsList.add(product); // Chỉ thêm vào allProductsList một lần
                        }
                    }
                    // Sau khi lấy xong toàn bộ sản phẩm, hiển thị chúng
                    displayAllProducts();
                }
                binding.progressBarPopular.setVisibility(View.GONE);
                binding.progressBarRegular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
                binding.progressBarRegular.setVisibility(View.GONE);
            }
        });
    }

}