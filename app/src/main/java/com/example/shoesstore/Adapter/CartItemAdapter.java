package com.example.shoesstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstore.Domain.CartItem;
import com.example.shoesstore.databinding.ItemContainerCartBinding;
import com.example.shoesstore.utilities.CartManager;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnCartItemQuantityChangeListener onCartItemQuantityChangeListener;

    public CartItemAdapter(Context context, List<CartItem> cartItems, OnCartItemQuantityChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.onCartItemQuantityChangeListener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerCartBinding binding = ItemContainerCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerCartBinding binding;

        public CartItemViewHolder(ItemContainerCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem cartItem) {
            // Hiển thị hình ảnh sản phẩm
            Glide.with(context).load(cartItem.getProductImage()).into(binding.itemImage);
            // Hiển thị tên sản phẩm, kích cỡ và số lượng
            binding.itemName.setText(cartItem.getProductName());
            binding.itemSize.setText(cartItem.getSize());
            binding.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

            // Định dạng giá tiền theo VND
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Hiển thị giá sản phẩm theo định dạng VND
            binding.itemPrice.setText(format.format(cartItem.getProductPrice()));
            // Xử lý sự kiện tăng số lượng sản phẩm
            binding.btnIncrease.setOnClickListener(view -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                binding.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
                CartManager cartManager = new CartManager(context);
                cartManager.updateCartItem(cartItem);
                if (onCartItemQuantityChangeListener != null) {
                    onCartItemQuantityChangeListener.onQuantityChanged();
                }
            });

            // Xử lý sự kiện giảm số lượng sản phẩm
            binding.btnDecrease.setOnClickListener(view -> {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    binding.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
                    CartManager cartManager = new CartManager(context);
                    cartManager.updateCartItem(cartItem);
                    if (onCartItemQuantityChangeListener != null) {
                        onCartItemQuantityChangeListener.onQuantityChanged();
                    }
                } else {
                    CartManager cartManager = new CartManager(context);
                    cartManager.removeCartItem(cartItem);
                    cartItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    if (onCartItemQuantityChangeListener != null) {
                        onCartItemQuantityChangeListener.onQuantityChanged();
                    }
                }
            });
        }
    }

    public interface OnCartItemQuantityChangeListener {
        void onQuantityChanged();
    }
}
