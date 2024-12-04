package com.example.shoesstore.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

//định dạng VND
import java.text.NumberFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstore.Domain.CartItem;
import com.example.shoesstore.databinding.ItemContainerCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private OnCartItemQuantityChangeListener onCartItemQuantityChangeListener;

    public CartAdapter(List<CartItem> cartItems, OnCartItemQuantityChangeListener listener) {
        this.cartItems = cartItems;
        this.onCartItemQuantityChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerCartBinding binding = ItemContainerCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        // Định dạng giá tiền theo VND
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        holder.binding.itemName.setText(cartItem.getProductName());
        // Hiển thị giá theo VND
        holder.binding.itemPrice.setText(format.format(cartItem.getProductPrice()));
        holder.binding.itemSize.setText(cartItem.getSize());
        holder.binding.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(holder.itemView.getContext()).load(cartItem.getProductImage()).into(holder.binding.itemImage);

        holder.binding.btnIncrease.setOnClickListener(v -> {
            if (onCartItemQuantityChangeListener != null) {
                onCartItemQuantityChangeListener.onQuantityChanged(cartItem, cartItem.getQuantity() + 1);
            }
        });

        holder.binding.btnDecrease.setOnClickListener(v -> {
            if (onCartItemQuantityChangeListener != null && cartItem.getQuantity() > 1) {
                onCartItemQuantityChangeListener.onQuantityChanged(cartItem, cartItem.getQuantity() - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ItemContainerCartBinding binding;

        public CartViewHolder(@NonNull ItemContainerCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnCartItemQuantityChangeListener {
        void onQuantityChanged(CartItem cartItem, int newQuantity);
    }
}
