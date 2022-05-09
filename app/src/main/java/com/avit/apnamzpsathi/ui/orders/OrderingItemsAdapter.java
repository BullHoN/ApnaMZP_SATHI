package com.avit.apnamzpsathi.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.model.ShopItemData;
import com.avit.apnamzpsathi.model.ShopPricingData;

import java.util.List;

public class OrderingItemsAdapter extends RecyclerView.Adapter<OrderingItemsAdapter.OrderItemsViewHolder>{
    private List<ShopItemData> orderItems;
    private Context context;

    public OrderingItemsAdapter(List<ShopItemData> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_items,parent,false);
        return new OrderItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsViewHolder holder, int position) {
        ShopItemData curr = orderItems.get(position);
        ShopPricingData pricingData = curr.getPricings().get(0);
        String orderItemDetails = curr.getQuantity() + " x " + curr.getName() + " (" + pricingData.getType() + ") ";
        holder.orderItemDetail.setText(orderItemDetails);

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class OrderItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView orderItemDetail;

        public OrderItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemDetail = itemView.findViewById(R.id.order_item_detail);
        }
    }
}
