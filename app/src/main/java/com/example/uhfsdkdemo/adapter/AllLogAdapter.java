package com.example.uhfsdkdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.bean.OrderBean;
import com.example.uhfsdkdemo.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：
 */

public class AllLogAdapter extends RecyclerView.Adapter<AllLogAdapter.Holder> {

    private Context mContext;
    private List<OrderBean> datas;
    private OnItemClickListener mItemClickListener;

    public AllLogAdapter(Context context, List<OrderBean> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.name.setText(position + 1 + ".  名称:  " + datas.get(position).getName());
        holder.color.setText("颜色:  " + datas.get(position).getColor());
        holder.factroy.setText("工厂:  " + datas.get(position).getFactroy());
//        holder.pNum.setText(datas.get(position).getOrderNumber()+"匹");
//        holder.mNum.setText(datas.get(position).getRiceNumber()+"米");
        holder.pNum.setText("匹数:  " + String.format("%.2f", Double.valueOf(datas.get(position).getOrderNumber())) + "匹");
        holder.mNum.setText("米数:  " + String.format("%.2f", Double.valueOf(datas.get(position).getRiceNumber())) + "米");
        holder.inTime.setText("进厂日期:  " + TimeUtils.parseTime1(datas.get(position).getOrderTime()));
        holder.outTime.setText("出厂日期:  " + TimeUtils.parseTime1(datas.get(position).getShippingDate()));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.pNum)
        TextView pNum;
        @BindView(R.id.mNum)
        TextView mNum;
        @BindView(R.id.inTime)
        TextView inTime;
        @BindView(R.id.outTime)
        TextView outTime;
        @BindView(R.id.color)
        TextView color;
        @BindView(R.id.factroy)
        TextView factroy;
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