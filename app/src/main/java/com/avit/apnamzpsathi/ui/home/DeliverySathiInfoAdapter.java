package com.avit.apnamzpsathi.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.model.DeliveryInfoData;

import java.util.List;

public class DeliverySathiInfoAdapter extends RecyclerView.Adapter<DeliverySathiInfoAdapter.DeliveyrSathiInfoViewHolder>{

    private Context context;
    private List<DeliveryInfoData> deliveryInfoData;

    public DeliverySathiInfoAdapter(Context context, List<DeliveryInfoData> deliveryInfoData) {
        this.context = context;
        this.deliveryInfoData = deliveryInfoData;
    }

    @NonNull
    @Override
    public DeliveyrSathiInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_deliveryinfo,parent,false);
        return new DeliveyrSathiInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveyrSathiInfoViewHolder holder, int position) {
        DeliveryInfoData curr = deliveryInfoData.get(position);

        holder.messageView.setText(curr.getMessage());
        holder.valueView.setText("₹" + curr.getValue());

    }

    @Override
    public int getItemCount() {
        return deliveryInfoData.size();
    }

    public void replaceItems(List<DeliveryInfoData> list){
        deliveryInfoData = list;
        notifyDataSetChanged();
    }

    public class DeliveyrSathiInfoViewHolder extends RecyclerView.ViewHolder{
        public TextView messageView,valueView;
        public DeliveyrSathiInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.message);
            valueView = itemView.findViewById(R.id.value);
        }
    }
}
