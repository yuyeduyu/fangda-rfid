package com.example.uhfsdkdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.bean.SettlementBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：
 */

public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.Holder> {

    private Context mContext;
    private List<SettlementBean> datas;
    private OnItemClickListener mItemClickListener;

    public SettlementAdapter(Context context, List<SettlementBean> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_settlement, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.productName.setText(datas.get(position).getProductName());
        holder.deliverySupplierDate.setText(datas.get(position).getDeliverySupplierDate());
        holder.isIncludeTax.setText(datas.get(position).getIsIncludeTax());
        holder.mmi.setText(datas.get(position).getMmi());
        holder.numberOfCheckouts.setText(datas.get(position).getNumberOfCheckouts());
        holder.priceAdjustment.setText(datas.get(position).getPriceAdjustment());
        holder.supplierName.setText(datas.get(position).getSupplierName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.priceAdjustment)
        TextView priceAdjustment;
        @BindView(R.id.isIncludeTax)
        TextView isIncludeTax;
        @BindView(R.id.supplierName)
        TextView supplierName;
        @BindView(R.id.deliverySupplierDate)
        TextView deliverySupplierDate;
        @BindView(R.id.numberOfCheckouts)
        TextView numberOfCheckouts;
        @BindView(R.id.mmi)
        TextView mmi;
        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}