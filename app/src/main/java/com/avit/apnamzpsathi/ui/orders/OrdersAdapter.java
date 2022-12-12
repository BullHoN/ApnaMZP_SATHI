package com.avit.apnamzpsathi.ui.orders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
import com.avit.apnamzpsathi.utils.PrettyStrings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersAdapterViewHolder>{

    public interface OrdersActions {
        void updateOrderStatus(String orderId,Integer updatedStatus,int position,MaterialButton button);
        void updateItemsOnTheWayTotalCost(String orderId, String totalCost);
        void cancelItemsOnTheWay(String orderId);
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

        holder.totalAmountToGiveView.setText("Give To Shop: ₹" + curr.getTotalAmountToGive());
        if(!curr.isPaid()){
            holder.totalAmountToTakeView.setText("Take From Customer: ₹" + curr.getTotalAmountToTake());
        }
        else {
            holder.totalAmountToTakeView.setText("Payment Done");
        }


        holder.orderId.setText("Order Id: #" + curr.get_id());
        holder.shopName.setText("Name: " + curr.getShopInfo().getName());
        holder.shopAddress.setText("Address: " + curr.getShopInfo().getRawAddress());

        holder.shopAddressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMaps(curr.getShopInfo().getLatitude(),curr.getShopInfo().getLongitude());
            }
        });

        holder.shopPhoneNo.setText("Phone No: " + curr.getShopInfo().getPhoneNo());
        holder.shopPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(curr.getShopInfo().getPhoneNo());
            }
        });

        holder.expectedIncome.setText("Expected Income "+ PrettyStrings.getPriceInRupees(curr.getDeliverySathiIncome()));

        holder.customerName.setText("Name: " + curr.getUserInfo().getName());
        holder.customerAddress.setText("Address: " + curr.getUserInfo().getRawAddress());

        holder.customerAddressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMaps(curr.getUserInfo().getLatitude(),curr.getUserInfo().getLongitude());
            }
        });

        if(curr.getUserInfo().getLandmark() != null && curr.getUserInfo().getLandmark().length() != 0){
            holder.customerLandmarkContainer.setVisibility(View.VISIBLE);
            holder.customerLandmark.setText("Landmark: " + curr.getUserInfo().getLandmark());
        }
        else {
            holder.customerLandmarkContainer.setVisibility(View.GONE);
        }

        if(curr.getUserInfo().getHouseNo() != null && curr.getUserInfo().getHouseNo().length() != 0){
            holder.customerHouseContainer.setVisibility(View.VISIBLE);
            holder.customerHouseNo.setText("House No : " + curr.getUserInfo().getHouseNo());
        }

        holder.customerPhoneNo.setText("PhoneNo: " + curr.getUserInfo().getPhoneNo());
        holder.customerPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(curr.getUserInfo().getPhoneNo());
            }
        });

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

        if(curr.getItemsOnTheWay() != null && curr.getItemsOnTheWay().size() > 0){
            holder.itemsOnTheWayToggleButton.setBackgroundColor(context.getResources().getColor(R.color.failure));
            holder.itemsOnTheWayRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            OrderItemsOnTheWayAdapter orderingItemsAdapter = new OrderItemsOnTheWayAdapter(context,curr.getItemsOnTheWay());
            holder.itemsOnTheWayRecyclerView.setAdapter(orderingItemsAdapter);

            holder.itemsOnTheWayToggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(curr.isItemsOnTheWayVisible()){
                        curr.setItemsOnTheWayVisible(false);
                        holder.expandableItemsOnTheWayDetailsView.setVisibility(View.GONE);
                    }
                    else {
                        curr.setItemsOnTheWayVisible(true);
                        holder.expandableItemsOnTheWayDetailsView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else {
            holder.itemsOnTheWayContainer.setVisibility(View.GONE);
        }



        holder.moreActionsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view,curr.get_id(),curr);
            }
        });

        holder.nextActionButton.setEnabled(true);
        if(curr.getOrderStatus() <= 4){
            holder.nextActionButton.setText("Order Received From Shop");
        }
        else {
            holder.nextActionButton.setText("Order Delivered");
        }

        int currPosition = position;
        holder.nextActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.nextActionButton.setEnabled(false);
                ordersActions.updateOrderStatus(curr.get_id(), curr.getOrderStatus()+1,currPosition,holder.nextActionButton);
            }
        });

    }

    private void call(String phoneNo){
        Intent callingIntent = new Intent();
        callingIntent.setAction(Intent.ACTION_DIAL);
        callingIntent.setData(Uri.parse("tel: " + phoneNo));
        context.startActivity(callingIntent);
    }

    public void removeOrderItem(int position){
        orderItems.remove(position);
        notifyDataSetChanged();
    }

    private void openGoogleMaps(String latitude,String longitude){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude+","+longitude);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        context.startActivity(mapIntent);
    }

    private void showMenu(View v,String order_id,OrderItem orderItem){
        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.inflate(R.menu.menu_order);

        if(orderItem.getItemsOnTheWay().size() == 0){
            popupMenu.getMenu().getItem(1).setVisible(false);
            popupMenu.getMenu().getItem(2).setVisible(false);
        }
        else if(orderItem.isItemsOnTheWayCancelled()){
            popupMenu.getMenu().getItem(1).setVisible(false);
            popupMenu.getMenu().getItem(2).setVisible(false);
        }
        else if(orderItem.getItemsOnTheWayActualCost() != 0){
            popupMenu.getMenu().getItem(2).setVisible(false);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.cancel_items_on_the_way:
                        // Cancel Items On The Way
                        ordersActions.cancelItemsOnTheWay(order_id);
                        return true;
                    case R.id.add_cost_of_items_on_the_way:
                        // Add Cost Of Items On The Way
                        addCostOfItemsOnTheWayDialog(order_id);
                        return true;
                    default:
                        return  false;
                }

            }
        });

        popupMenu.show();
    }

    private void addCostOfItemsOnTheWayDialog(String order_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_item_actions,null,false);

        TextView titleView = view.findViewById(R.id.title);
        titleView.setText("Enter Total Cost Of All Items On The Way");

        TextInputEditText textInputEditText = view.findViewById(R.id.dialog_input);

        builder.setView(view);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String data = textInputEditText.getText().toString();
                if(data.length() == 0){
                    Toasty.error(context,"Enter Valid Data",Toasty.LENGTH_SHORT).show();
                    return;
                }

                ordersActions.updateItemsOnTheWayTotalCost(order_id,data);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

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
        public TextView orderId,customerName,customerPhoneNo,customerAddress, customerLandmark, customerHouseNo;
        public TextView shopName,shopPhoneNo,shopAddress;
        public LinearLayout customerDetailsToggleButton, expandableCustomerDetailsView;
        public LinearLayout customerHouseContainer, customerLandmarkContainer;
        public LinearLayout shopDetailsToggleButton, expandableShopDetailsView;
        public LinearLayout itemsOnTheWayToggleButton, expandableItemsOnTheWayDetailsView;
        public RecyclerView itemsOnTheWayRecyclerView;
        public MaterialButton nextActionButton;
        public ImageButton moreActionsMenuButton;
        public TextView totalAmountToTakeView, totalAmountToGiveView, expectedIncome;
        public LinearLayout customerAddressContainer, shopAddressContainer, itemsOnTheWayContainer;

        public OrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            orderingItemsList = itemView.findViewById(R.id.order_items);
            orderId = itemView.findViewById(R.id.order_id);
            customerName = itemView.findViewById(R.id.customer_name);
            customerPhoneNo = itemView.findViewById(R.id.customer_phoneNo);
            customerAddress = itemView.findViewById(R.id.customer_address);
            customerLandmark = itemView.findViewById(R.id.customer_landmark);
            customerLandmarkContainer = itemView.findViewById(R.id.customer_landmark_container);
            customerHouseContainer = itemView.findViewById(R.id.customer_houseNo_container);
            customerHouseNo = itemView.findViewById(R.id.customer_houseNo);
            customerDetailsToggleButton = itemView.findViewById(R.id.customer_toggle_button);
            expandableCustomerDetailsView = itemView.findViewById(R.id.expandable_customer_details);

            itemsOnTheWayToggleButton = itemView.findViewById(R.id.itemsOn_the_way_toggle_button);
            expandableItemsOnTheWayDetailsView = itemView.findViewById(R.id.items_on_the_way_expandable_layout);
            itemsOnTheWayRecyclerView = itemView.findViewById(R.id.items_on_the_way_recyclerview);

            totalAmountToTakeView = itemView.findViewById(R.id.totalAmountToTake);
            totalAmountToGiveView = itemView.findViewById(R.id.totalAmountToGive);

            shopName = itemView.findViewById(R.id.shop_name);
            shopPhoneNo = itemView.findViewById(R.id.shop_phoneNo);
            shopAddress = itemView.findViewById(R.id.shop_address);
            shopDetailsToggleButton = itemView.findViewById(R.id.shop_toggle_button);
            expandableShopDetailsView = itemView.findViewById(R.id.shop_expandable_layout);

            nextActionButton = itemView.findViewById(R.id.next_step);
            moreActionsMenuButton = itemView.findViewById(R.id.more_actions);

            customerAddressContainer = itemView.findViewById(R.id.customer_address_container);
            shopAddressContainer = itemView.findViewById(R.id.shop_address_container);

            expectedIncome = itemView.findViewById(R.id.expectedIncome);
            itemsOnTheWayContainer = itemView.findViewById(R.id.items_on_the_way_container);

        }
    }
}
