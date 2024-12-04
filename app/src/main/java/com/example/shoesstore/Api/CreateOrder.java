package com.example.shoesstore.Api;

import com.example.shoesstore.Constant.AppInfo;
import com.example.shoesstore.Helper.Helpers;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.util.Log;

public class CreateOrder {

    private class CreateOrderData {
        String AppId;
        String AppUser;
        String AppTime;
        String Amount;
        String AppTransId;
        String EmbedData;
        String Items;
        String BankCode;
        String Description;
        String Mac;

        private CreateOrderData(String amount) throws Exception {
            long appTime = new Date().getTime();
            AppId = String.valueOf(AppInfo.APP_ID);
            AppUser = "Android_Demo";
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = Helpers.getAppTransId();  // Đảm bảo đây trả về ID duy nhất cho mỗi giao dịch
            EmbedData = "{}";  // Nếu cần gửi dữ liệu khác, chỉnh sửa ở đây
            Items = "[]";  // Tương tự, nếu cần thêm sản phẩm vào, điều chỉnh ở đây
            BankCode = "zalopayapp";  // Đảm bảo đúng mã ngân hàng của ZaloPay
            Description = "Merchant pay for order #" + Helpers.getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);

            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);  // Chắc chắn Helper.getMac tính đúng chữ ký
        }
    }

    public JSONObject createOrder(String amount) throws Exception {
        CreateOrderData input = new CreateOrderData(amount);

        // Ghi log chi tiết để debug
        Log.d("CreateOrder", "Creating order with AppTransId: " + input.AppTransId);
        Log.d("CreateOrder", "Amount: " + input.Amount);
        Log.d("CreateOrder", "EmbedData: " + input.EmbedData);
        Log.d("CreateOrder", "Items: " + input.Items);
        Log.d("CreateOrder", "Mac: " + input.Mac);

        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.AppId)
                .add("app_user", input.AppUser)
                .add("app_time", input.AppTime)
                .add("amount", input.Amount)
                .add("app_trans_id", input.AppTransId)
                .add("embed_data", input.EmbedData)
                .add("item", input.Items)
                .add("bank_code", input.BankCode)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build();

        try {
            // Gửi yêu cầu đến server và lấy dữ liệu trả về
            JSONObject data = HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody);

            if (data == null || !data.has("return_code")) {
                throw new Exception("Server error or invalid response");
            }

            Log.d("CreateOrder", "Response from server: " + data.toString());
            return data;
        } catch (Exception e) {
            Log.e("CreateOrder", "Error creating order: " + e.getMessage(), e);
            throw new Exception("Error while sending order request", e);
        }
    }
}
