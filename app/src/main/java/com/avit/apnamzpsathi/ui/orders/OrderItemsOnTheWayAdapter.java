package com.avit.apnamzpsathi.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.apnamzpsathi.R;

import java.util.List;

public class OrderItemsOnTheWayAdapter extends RecyclerView.Adapter<OrderItemsOnTheWayAdapter.OrderItemsOnTheWayViewHolder>{

    private Context context;
    private List<String> itemsOnTheWay;

    public OrderItemsOnTheWayAdapter(Context context, List<String> itemsOnTheWay) {
        this.context = context;
        this.itemsOnTheWay = itemsOnTheWay;
    }

    @NonNull
    @Override
    public OrderItemsOnTheWayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_on_the_way,parent,false);
        return new OrderItemsOnTheWayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsOnTheWayViewHolder holder, int position) {
        holder.itemOnTheWayView.setText(itemsOnTheWay.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsOnTheWay.size();
    }

    public class OrderItemsOnTheWayViewHolder extends RecyclerView.ViewHolder {

        public TextView itemOnTheWayView;

        public OrderItemsOnTheWayViewHolder(@NonNull View itemView) {
            super(itemView);
            itemOnTheWayView = itemView.findViewById(R.id.item_on_the_way);
        }
    }
}
