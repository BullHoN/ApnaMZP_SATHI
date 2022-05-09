package com.avit.apnamzpsathi.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.model.OrderItem;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersAdapterViewHolder>{

    private List<OrderItem> orderItems;
    private Context context;

    public OrdersAdapter(List<OrderItem> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        return new OrdersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapterViewHolder holder, int position) {
        OrderItem curr = orderItems.get(position);

        holder.orderingItemsList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        OrderingItemsAdapter adapter = new OrderingItemsAdapter(curr.getOrderItems(),context);
        holder.orderingItemsList.setAdapter(adapter);

        holder.orderId.setText("Order Id: #" + curr.get_id());
        holder.shopName.setText("Name: " + curr.getShopInfo().getName());
        holder.shopAddress.setText("Address: " + curr.getShopInfo().getRawAddress());
        holder.shopPhoneNo.setText("Phone No: " + curr.getShopInfo().getPhoneNo());


        holder.customerName.setText("Name: " + curr.getUserInfo().getName());
        holder.customerAddress.setText("Address: " + curr.getUserInfo().getRawAddress());
        holder.customerPhoneNo.setText("PhoneNo: " + curr.getUserInfo().getPhoneNo());

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public void updateItems(List<OrderItem> newItems){
        orderItems = newItems;
        notifyDataSetChanged();
    }

    public class OrdersAdapterViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView orderingItemsList;
        public TextView orderId,customerName,customerPhoneNo,customerAddress;
        public TextView shopName,shopPhoneNo,shopAddress;

        public OrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            orderingItemsList = itemView.findViewById(R.id.order_items);
            orderId = itemView.findViewById(R.id.order_id);
            customerName = itemView.findViewById(R.id.customer_name);
            customerPhoneNo = itemView.findViewById(R.id.customer_phoneNo);
            customerAddress = itemView.findViewById(R.id.customer_address);
            shopName = itemView.findViewById(R.id.shop_name);
            shopPhoneNo = itemView.findViewById(R.id.shop_phoneNo);
            shopAddress = itemView.findViewById(R.id.shop_address);
        }
    }
}
