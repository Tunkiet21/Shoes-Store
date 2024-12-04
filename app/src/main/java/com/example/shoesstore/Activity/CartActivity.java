package com.example.shoesstore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

//Định dạng VND
import java.text.NumberFormat;
import java.util.Locale;


import com.example.shoesstore.Adapter.CartItemAdapter;
import com.example.shoesstore.Domain.CartItem;
import com.example.shoesstore.Domain.Order;
import com.example.shoesstore.Domain.OrderItem;
import com.example.shoesstore.databinding.ActivityCartBinding;
import com.example.shoesstore.utilities.CartManager;
import com.example.shoesstore.utilities.Constants;
import com.example.shoesstore.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnCartItemQuantityChangeListener {

    private ActivityCartBinding binding;
    private CartManager cartManager;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItems;

    private PreferenceManager preferecnceManager;
    private DatabaseReference databaseReference;

    private double subtotal, delivery, tax, total;
    private double discount = 0.0;
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo định dạng tiền tệ VND
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        preferecnceManager = new PreferenceManager(getApplicationContext());

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        cartManager = new CartManager(this);
        cartItems = cartManager.getCartItems();

        binding.cartItems.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter(this, cartItems, this);
        binding.cartItems.setAdapter(cartItemAdapter);

        binding.buttonBack.setOnClickListener(v -> onBackPressed());

        binding.buttonCheckout.setOnClickListener(v -> {
            // Lấy giá trị từ EditText
            String address = binding.etAddress.getText().toString().trim();
            String phoneNumber = binding.etPhone.getText().toString().trim();

            // Kiểm tra nếu địa chỉ hoặc số điện thoại rỗng
            if (address.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
            } else if (phoneNumber.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu cả địa chỉ và số điện thoại đều hợp lệ, truyền dữ liệu qua CheckoutActivity
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("address", address); // Truyền địa chỉ
                intent.putExtra("phoneNumber", phoneNumber); // Truyền số điện thoại
                intent.putExtra("subtotal", binding.txtSubtotal.getText().toString());
                intent.putExtra("delivery", binding.txtDelivery.getText().toString());
                intent.putExtra("tax", binding.txtTax.getText().toString());
                intent.putExtra("total", binding.txtTotal.getText().toString());
                startActivity(intent);
            }
        });


        binding.btnCuppoon.setOnClickListener(v -> applyCoupon());

        if (!cartItems.isEmpty()) {
            updateSummary();
        }

        // Thêm TextWatcher cho etPhone để tự động định dạng số điện thoại
        binding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Kiểm tra nếu người dùng chưa nhập mã vùng +84
                if (charSequence.length() > 0 && !charSequence.toString().startsWith("+84")) {
                    // Thêm mã vùng vào nếu chưa có
                    binding.etPhone.setText("+84 " + charSequence.toString().replaceAll("[^\\d]", ""));
                    binding.etPhone.setSelection(binding.etPhone.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    @Override
    public void onQuantityChanged() {
        updateSummary();
    }

    private void updateSummary() {
        subtotal = calculateSubtotal();
        delivery = calculateDelivery();
        tax = calculateTax();
        total = subtotal + delivery + tax - discount;

        // Định dạng các giá trị và hiển thị dưới dạng VND
        binding.txtSubtotal.setText(currencyFormat.format(subtotal));
        binding.txtDelivery.setText(currencyFormat.format(delivery));
        binding.txtTax.setText(currencyFormat.format(tax));
        binding.txtTotal.setText(currencyFormat.format(total));
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (CartItem cartItem : cartItems) {
            subtotal += cartItem.getProductPrice() * cartItem.getQuantity();
        }
        return subtotal;
    }

    private double calculateDelivery() {
        return 10000; // Ví dụ giá vận chuyển cố định
    }

    private double calculateTax() {
        return subtotal * 0.01; // Thuế 1%
    }

    private void applyCoupon() {
        String couponCode = binding.edtCuppon.getText().toString().trim();
        if (TextUtils.isEmpty(couponCode)) {
            Toast.makeText(this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("coupons").orderByChild("code").equalTo(couponCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Coupon coupon = snapshot.getValue(Coupon.class);
                        if (coupon != null && "Active".equals(coupon.getStatus())) {
                            snapshot.getRef().child("status").setValue("Inactive").addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    applyDiscount(coupon.getDiscount());
                                } else {
                                    Toast.makeText(CartActivity.this, "Lỗi khi nhập mã", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Mã giảm giá không tồn tại hoặc quá hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "Lỗi khi áp dụng mã", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyDiscount(String discountStr) {
        if (discountStr.endsWith("%")) {
            discount = subtotal * Double.parseDouble(discountStr.replace("%", "")) / 100.0;
        } else {
            discount = Double.parseDouble(discountStr);
        }
        updateSummary();
        Toast.makeText(this, "Mã giảm giá áp dụng thành công", Toast.LENGTH_SHORT).show();
    }


}
