package com.example.shoesstore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoesstore.R;
import com.example.shoesstore.databinding.ActivityChangePasswordBinding;
import com.example.shoesstore.utilities.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("gmail");
        }
        setOnListeners();
    }

    private void setOnListeners() {
        binding.imageBack.setOnClickListener(v-> {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });
        binding.buttonToggleNewPassword.setOnClickListener(v -> togglePasswordVisibility(binding.inputNewPassword, binding.buttonToggleNewPassword));
        binding.buttonToggleConfirmNewPassword.setOnClickListener(v -> togglePasswordVisibility(binding.inputConfirmNewPassword, binding.buttonToggleConfirmNewPassword));

        binding.buttonChangePassword.setOnClickListener(v -> {
            loading(true);
            if (isValidChangePasswordDetails()){
                updatePassword(binding.inputNewPassword.getText().toString().trim(),email);
            }

        });

    }

    private void updatePassword(String newPassword, String yourEmail) {
        DatabaseReference databaseReference = database.getReference(Constants.KEY_COLLECTION_USERS);
        databaseReference.orderByChild("gmail").equalTo(yourEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userId = snapshot.getKey();

                                // Tạo map để cập nhật mật khẩu
                                HashMap<String, Object> updates = new HashMap<>();
                                updates.put(Constants.KEY_PASSWORD, newPassword);

                                // Cập nhật mật khẩu cho người dùng
                                databaseReference.child(userId).updateChildren(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading(false);
                                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                finish();
                                                Toast.makeText(getApplicationContext(), "Thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading(false);
                                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                finish();
                                                Toast.makeText(getApplicationContext(), "Mật khẩu không được giống như mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.e("Lỗi rồi", "Không tìm thấy người dùng với email này.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Lỗiiii", "Lỗi khi lấy tài liệu người dùng: " + databaseError.getMessage());
                    }
                });
    }

    private void togglePasswordVisibility(EditText inputPassword, Button buttonTogglePassword) {
        // Lưu vị trí con trỏ hiện tại
        int cursorPosition = inputPassword.getSelectionStart();

        if (inputPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Nếu đang  hiển thị password, chuyển sang chế độ ẩn password
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // Thiết lập biểu tượng cho Button để hiển thị biểu tượng ẩn password
            buttonTogglePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
        } else {
            // Nếu đang ẩn password, chuyển sang chế độ hiển thị password
            inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            // Thiết lập biểu tượng cho Button để hiển thị biểu tượng hiển thị password
            buttonTogglePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eyeoff, 0);
        }

        // Khôi phục vị trí con trỏ
        inputPassword.setSelection(cursorPosition);
    }

    private Boolean isValidChangePasswordDetails(){

        String newPassword = binding.inputNewPassword.getText().toString().trim();
        String confirmPassword = binding.inputConfirmNewPassword.getText().toString().trim();

        if (newPassword.isEmpty()) {
            showToast("Vui lòng nhập mật khẩu mới.");
            return false;
        } else if (newPassword.length() < 10) {
            showToast("Mật khẩu mới phải có ít nhất 10 ký tự.");
            return false;
        } else if (newPassword.length() > 20) {
            showToast("Mật khẩu mới không được dài hơn 20 ký tự.");
            return false;
        } else if (!newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            showToast("Mật khẩu mới phải chứa ít nhất 1 chữ cái và 1 số.");
            return false;
        } else if (newPassword.matches(".*[^a-zA-Z\\d].*")) {
            showToast("Mật khẩu mới không được chứa ký tự đặc biệt.");
            return false;
        } else if (confirmPassword.isEmpty()) {
            showToast("Vui lòng xác nhận mật khẩu mới.");
            return false;
        } else if (!newPassword.equals(confirmPassword)) {
            showToast("Mật khẩu mới và xác nhận mật khẩu phải giống nhau.");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonChangePassword.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonChangePassword.setVisibility(View.VISIBLE);
        }
    }
}