package com.example.shoesstore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.shoesstore.R;
import com.example.shoesstore.databinding.ActivityMainBinding;
import com.example.shoesstore.databinding.ActivityProfileBinding;
import com.example.shoesstore.utilities.Constants;
import com.example.shoesstore.utilities.PreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private PreferenceManager preferecnceManager;
    private static final String PHONE_NUMBER = "0932024808";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferecnceManager = new PreferenceManager(getApplicationContext());

        setListeners();
        loadData();
    }

    private void loadData() {
        String email = preferecnceManager.getString(Constants.KEY_EMAIL);
        binding.emailTxt.setText(email);
    }

    private void setListeners() {
        binding.btnLogout.setOnClickListener(view -> {
            preferecnceManager.clear();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        });

        // Sự kiện khi nhấn vào "Đổi mật khẩu"
        binding.btnChangePassword.setOnClickListener(view -> {
            // Lấy email từ PreferenceManager
            String email = preferecnceManager.getString(Constants.KEY_EMAIL);

            // Chuyển email qua Intent
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            intent.putExtra("gmail", email); // Truyền email qua Intent
            startActivity(intent); // Mở Activity mới
        });

        // Sự kiện khi nhấn vào "Liên hệ"
        binding.contactOption.setOnClickListener(view -> showContactDialog());
    }
    private void showContactDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Liên hệ")
                .setMessage("Số điện thoại: " + PHONE_NUMBER)
                .setPositiveButton("Gọi", (dialog, which) -> makePhoneCall())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
        startActivity(intent);
    }
}