package com.swachtaapp.ui.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.swachtaapp.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    private List<HomeViewModel> list;
    private Activity activity;
    public HomeAdapter(Activity activity, List<HomeViewModel> list) {
        this.activity = activity;
        this.list = list;
    }

    // RecyclerView recyclerView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       final HomeViewModel h = list.get(position);
        holder.textAddress.setText(h.getAddress());
        holder.textRemark.setText(h.getRemark());
        holder.textStatus.setText(h.getStatus());
        holder.textBin.setText(h.getBinNumber());
        holder.textDate.setText(h.getDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+h.getRemark(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textAddress, textRemark, textStatus, textDate, textBin;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textAddress = (TextView) itemView.findViewById(R.id.textAddress);
            this.textRemark = (TextView) itemView.findViewById(R.id.textRemark);
            this.textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            this.textBin = (TextView) itemView.findViewById(R.id.textBin);
            this.textDate = (TextView) itemView.findViewById(R.id.textDate);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
        }
    }
}
