package com.example.shoesstore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.shoesstore.Domain.Category;
import com.example.shoesstore.databinding.ActivityCreateCategoryBinding;
import com.example.shoesstore.utilities.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class CreateCategoryActivity extends BaseActivity {

    private ActivityCreateCategoryBinding binding;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Uri imageUri;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.imageProfile.setOnClickListener(v -> selectImage());
        binding.buttonSubmit.setOnClickListener(v -> uploadImageAndSaveCategory());
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Chụp ảnh", "Chọn từ thư viện", "Hủy"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateCategoryActivity.this);
                builder.setTitle("Tùy chọn");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Chụp ảnh")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Chọn từ thư viện")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Hủy")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else {
                Toast.makeText(this, "Lỗi cấp quyền Máy Ảnh", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi cấp quyền Máy Ảnh", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_CAMERA || requestCode == PICK_IMAGE_GALLERY) {
                imageUri = data.getData();
                if (imageUri != null) {
                    binding.textAddImage.setVisibility(View.GONE);
                    binding.imageProfile.setImageURI(imageUri); // Hiển thị ảnh từ Uri gốc
                }
            }
        }
    }


    private void uploadImageAndSaveCategory() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri) // Tải ảnh từ Uri gốc
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveCategoryToDatabase(imageUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(CreateCategoryActivity.this, "Lỗi khi tải ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            saveCategoryToDatabase(null);
        }
    }


    private void saveCategoryToDatabase(String imageUrl) {
        String categoryName = binding.editTextCategoryName.getText().toString().trim();
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = databaseReference.push().getKey();
        if (id != null) {
            Category category = new Category(id, categoryName, imageUrl);
            databaseReference.child(Constants.KEY_COLLECTION_CATEGORY).child(id).setValue(category)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateCategoryActivity.this, "Danh mục được tạo thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CreateCategoryActivity.this, "Lỗi khi tạo danh mục", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) return "";
        String extension = null;
        try {
            extension = getContentResolver().getType(uri).split("/")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extension != null ? extension : "jpg"; // Nếu không tìm thấy extension, trả về mặc định là jpg
    }

}
