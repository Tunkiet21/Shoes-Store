package com.example.shoesstore.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.NumberFormat;
import java.util.Locale;

import android.content.Intent;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.shoesstore.Domain.Product;
import com.example.shoesstore.Domain.Review;
import com.example.shoesstore.Listeners.OnProductClickListener;
import com.example.shoesstore.databinding.ViewholderPopListBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnProductClickListener listener;

    public ProductMainAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
        // Lấy rating trung bình từ Firebase
        holder.fetchAverageRating(product.getId(), holder.binding.ratingBar,holder.binding.ratingTxt);
        // Lắng nghe sự thay đổi đánh giá mới (thêm, sửa, xóa)
        holder.listenForNewReviews(product.getId(), holder.binding.ratingBar, holder.binding.ratingTxt);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ViewholderPopListBinding binding;

        public ProductViewHolder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Xử lý sự kiện click cho itemView
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onProductClick(productList.get(position));
                }
            });


        }

        public void bind(Product product) {
            binding.title.setText(product.getTitle());

            // Định dạng giá tiền theo VND
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Hiển thị giá mới
            binding.priceTxt.setText(format.format(product.getPrice()));

            // Hiển thị giá cũ và gạch ngang
            binding.oldPriceTxt.setText(format.format(product.getOldPrice()));
            binding.oldPriceTxt.setPaintFlags(binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            // Hiển thị hình ảnh sản phẩm
            Glide.with(binding.pic.getContext())
                    .load(product.getPicUrl())
                    .into(binding.pic);
        }

        public void fetchAverageRating(String productId, RatingBar ratingBar, TextView ratingTxt) {
            DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
            reviewsRef.orderByChild("productId").equalTo(productId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            float totalRating = 0;
                            int count = 0;

                            // Tính tổng số sao và số lượng review
                            for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                                Review review = reviewSnapshot.getValue(Review.class);
                                if (review != null) {
                                    totalRating += review.getRating();
                                    count++;
                                }
                            }

                            // Hiển thị số sao trung bình nếu có review
                            if (count > 0) {
                                float averageRating = totalRating / count;

                                // Cập nhật RatingBar với số sao trung bình
                                ratingBar.setRating(averageRating);

                                // Cập nhật ratingTxt với số sao trung bình dưới dạng "(x)"
                                ratingTxt.setText(String.format("(%s)", averageRating));
                            } else {
                                // Nếu không có review, hiển thị RatingBar và TextView mặc định
                                ratingBar.setRating(0);
                                ratingTxt.setText("(0)");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu cần
                        }
                    });
        }
        public void listenForNewReviews(String productId, RatingBar ratingBar, TextView ratingTxt) {
            DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
            reviewsRef.orderByChild("productId").equalTo(productId)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                            // Khi có một đánh giá mới được thêm vào
                            fetchAverageRating(productId, ratingBar, ratingTxt); // Cập nhật lại số sao
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                            // Khi có đánh giá được sửa đổi
                            fetchAverageRating(productId, ratingBar, ratingTxt); // Cập nhật lại số sao
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            // Khi có đánh giá bị xóa
                            fetchAverageRating(productId, ratingBar, ratingTxt); // Cập nhật lại số sao
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                            // Khi có đánh giá bị di chuyển (không sử dụng trong trường hợp này)
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu cần
                        }
                    });
        }

    }
}
