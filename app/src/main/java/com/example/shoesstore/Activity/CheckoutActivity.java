package com.example.shoesstore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import android.util.Log;

import com.example.shoesstore.Adapter.CartItemAdapter;
import com.example.shoesstore.Api.CreateOrder;
import com.example.shoesstore.Domain.CartItem;
import com.example.shoesstore.Domain.Order;
import com.example.shoesstore.Domain.OrderItem;
import com.example.shoesstore.Domain.Payment;

import com.example.shoesstore.R;
import com.example.shoesstore.databinding.ActivityCheckoutBinding;
import com.example.shoesstore.utilities.CartManager;
import com.example.shoesstore.utilities.Constants;
import com.example.shoesstore.utilities.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private String orderId;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItems;
    private CartManager cartManager;
    private DatabaseReference databaseReference;
    private PreferenceManager preferecnceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferecnceManager = new PreferenceManager(getApplicationContext());

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        init();
        loadData();
        setListeners();



    }



    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        cartManager = new CartManager(getApplicationContext());

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void loadData() {
        cartItems = cartManager.getCartItems();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        String address = intent.getStringExtra("address");  // Nhận địa chỉ từ CartActivity
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String subtotal = intent.getStringExtra("subtotal");
        String delivery = intent.getStringExtra("delivery");
        String tax = intent.getStringExtra("tax");
        String total = intent.getStringExtra("total");

        // Hiển thị thông tin trên UI
        binding.txtAddress.setText(address);  // Hiển thị địa chỉ
        binding.txtPhoneNumber.setText(phoneNumber);  // Hiển thị số điện thoại
        binding.txtSubtotal.setText(subtotal);
        binding.txtDelivery.setText(delivery);
        binding.txtTax.setText(tax);
        binding.txtTotal.setText(total);
    }


    private void setListeners() {
        binding.buttonBack.setOnClickListener(view -> onBackPressed());
        binding.btnContinue.setOnClickListener(view -> handleContinue());
    }

    private void handleContinue() {
        int selectedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedPaymentMethod = selectedRadioButton.getText().toString();

            // Kiểm tra lựa chọn thanh toán
            if (selectedRadioButtonId == R.id.rbZaloPay) {
                // Xử lý thanh toán qua ZaloPay
                CreateOrder orderApi = new CreateOrder();

                try {
                    String totalString = binding.txtTotal.getText().toString();

                    if (totalString == null || totalString.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Tổng tiền trống", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Lấy giá trị tổng tiền và chuyển đổi sang dạng long
                    double totalValue = parseToDouble(totalString); // Chuyển đổi tại đây
                    long roundedValue = Math.round(totalValue);

                    // Gọi API tạo đơn hàng và kiểm tra phản hồi
                    JSONObject data = orderApi.createOrder(String.valueOf(roundedValue));
                    Log.d("CreateOrder", "API Response: " + data.toString());

                    // Kiểm tra mã trả về
                    String code = data.getString("return_code");
                    Log.d("CreateOrder", "Return code: " + code);

                    if (code.equals("1")) {
                        String token = data.getString("zp_trans_token");
                        Log.d("CreateOrder", "Transaction token: " + token);

                        ZaloPaySDK.getInstance().payOrder(CheckoutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                Log.d("ZaloPay", "Payment succeeded: " + s);
                                savePaymentInfo();
                                updateStatusOrder();
                                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Log.d("ZaloPay", "Payment canceled: " + s);
                                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Log.e("ZaloPay", "Payment error: " + zaloPayError.toString());
                                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Lỗi thanh toán", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Log.e("CreateOrder", "Failed to create order. Return code: " + code);
                        Toast.makeText(getApplicationContext(), "Lỗi khi tạo đơn hàng", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.e("CreateOrder", "Error creating order: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (selectedRadioButtonId == R.id.rbTm) {
                // Xử lý thanh toán tiền mặt
                createOrderIn();
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
        }
    }

    private double parseToDouble(String value) {
        try {
            if (value == null || value.isEmpty()) {
                return 0.0;
            }

            // Thay thế các ký tự không cần thiết (định dạng tiền VND)
            String normalizedValue = value.replaceAll("[^\\d]", "");
            Log.d("CheckoutActivity", "Normalized Value: " + normalizedValue);

            return Double.parseDouble(normalizedValue);
        } catch (NumberFormatException e) {
            Log.e("CheckoutActivity", "Lỗi khi chuyển đổi giá trị: " + value, e);
            return 0.0;
        }
    }

    private void createOrderIn() {
        String orderId = databaseReference.child("orders").push().getKey();
        if (orderId == null) {
            Toast.makeText(this, "Không thể tạo đơn hàng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        String gmail = preferecnceManager.getString(Constants.KEY_EMAIL);
        String address = binding.txtAddress.getText().toString();
        String phoneNumber = binding.txtPhoneNumber.getText().toString();

        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        String totalString = binding.txtTotal.getText().toString()
                .replaceAll("[^\\d]", "")
                .replace("đ", "")
                .replace(",", "")
                .replace(".", "")
                .replace(" ", "")
                .trim();

        double totalAmount;

        try {
            totalAmount = Double.parseDouble(totalString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tổng tiền không hợp lệ. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
            Log.e("CheckoutActivity", "Error parsing total: " + e.getMessage());
            return;
        }

        Order order = new Order(orderId, gmail, orderDate, "pending", totalAmount, address, phoneNumber);

        databaseReference.child("orders").child(orderId).setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (cartItems != null) {
                            for (CartItem cartItem : cartItems) {
                                String itemId = databaseReference.child("order_items").child(orderId).push().getKey();
                                if (itemId != null) {
                                    OrderItem orderItem = new OrderItem(
                                            orderId,
                                            cartItem.getProductId(),
                                            cartItem.getProductPrice(),
                                            cartItem.getQuantity(),
                                            cartItem.getSize()
                                    );
                                    databaseReference.child("order_items").child(orderId).child(itemId).setValue(orderItem);
                                }
                            }
                        }

                        cartManager.clear();
                        cartItems.clear();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "Đơn hàng đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Lỗi không thể tạo đơn hàng.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void updateStatusOrder() {
        databaseReference.child("orders").child(orderId).child("status").setValue("pending")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Thành Công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi khi cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void savePaymentInfo() {
        int selectedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String paymentMethod = selectedRadioButton.getText().toString();

        String paymentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String idPayment = databaseReference.child("payments").push().getKey();
        if (idPayment == null) return;

        Payment payment = new Payment(orderId, idPayment, paymentMethod, paymentDate);

        databaseReference.child("payments").child(idPayment).setValue(payment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Thanh toán đã được ghi nhận thành công.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Không thể ghi nhận thanh toán.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("ZaloPay", "onNewIntent called");
        ZaloPaySDK.getInstance().onResult(intent);
    }

}
