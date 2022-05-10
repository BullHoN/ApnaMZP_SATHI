package com.avit.apnamzpsathi.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.model.OrderItem;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersAdapterViewHolder>{

    public interface OrdersActions {
        void updateOrderStatus(String orderId,Integer updatedStatus);
    }

    private List<OrderItem> orderItems;
    private Context context;
    private OrdersActions ordersActions;


    public OrdersAdapter(List<OrderItem> orderItems, Context context,OrdersActions ordersActions) {
        this.orderItems = orderItems;
        this.context = context;
        this.ordersActions = ordersActions;
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

        holder.customerDetailsToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr.getUserInfo().isVissible()){
                    curr.getUserInfo().setVissible(false);
                    holder.expandableCustomerDetailsView.setVisibility(View.GONE);
                }
                else {
                    curr.getUserInfo().setVissible(true);
                    holder.expandableCustomerDetailsView.setVisibility(View.VISIBLE);
                }
            }
        });


        holder.shopDetailsToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr.getShopInfo().isVissible()){
                    curr.getShopInfo().setVissible(false);
                    holder.expandableShopDetailsView.setVisibility(View.GONE);
                }
                else {
                    curr.getShopInfo().setVissible(true);
                    holder.expandableShopDetailsView.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.moreActionsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        if(curr.getOrderStatus() == 4){
            holder.nextActionButton.setText("Order Received From Shop");
        }
        else {
            holder.nextActionButton.setText("Order Delivered");
        }

        int currPosition = position;
        holder.nextActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItems.remove(currPosition);
                notifyDataSetChanged();
                ordersActions.updateOrderStatus(curr.get_id(), curr.getOrderStatus()+1);
            }
        });

    }

    private void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(context,v);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.transfer_order:
                        // TODO: call prakhar
                        return true;
                    default:
                        return  false;
                }

            }
        });

        popupMenu.inflate(R.menu.menu_order);
        popupMenu.show();

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
        public LinearLayout customerDetailsToggleButton, expandableCustomerDetailsView;
        public LinearLayout shopDetailsToggleButton, expandableShopDetailsView;
        public MaterialButton nextActionButton;
        public ImageButton moreActionsMenuButton;

        public OrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            orderingItemsList = itemView.findViewById(R.id.order_items);
            orderId = itemView.findViewById(R.id.order_id);
            customerName = itemView.findViewById(R.id.customer_name);
            customerPhoneNo = itemView.findViewById(R.id.customer_phoneNo);
            customerAddress = itemView.findViewById(R.id.customer_address);
            customerDetailsToggleButton = itemView.findViewById(R.id.customer_toggle_button);
            expandableCustomerDetailsView = itemView.findViewById(R.id.expandable_customer_details);

            shopName = itemView.findViewById(R.id.shop_name);
            shopPhoneNo = itemView.findViewById(R.id.shop_phoneNo);
            shopAddress = itemView.findViewById(R.id.shop_address);
            shopDetailsToggleButton = itemView.findViewById(R.id.shop_toggle_button);
            expandableShopDetailsView = itemView.findViewById(R.id.shop_expandable_layout);

            nextActionButton = itemView.findViewById(R.id.next_step);
            moreActionsMenuButton = itemView.findViewById(R.id.more_actions);

        }
    }
}
