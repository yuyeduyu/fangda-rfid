package com.example.uhfsdkdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.activity.ChukuInfoActivity;
import com.example.uhfsdkdemo.bean.ChukuResultBean;
import com.example.uhfsdkdemo.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lishanhui on 2018-06-11.
 * 描述：
 */

public class ChukuAdapter extends RecyclerView.Adapter<ChukuAdapter.Holder> {


    private Context mContext;
    private List<ChukuResultBean.ChukuBean> datas;
    private OnItemClickListener mItemClickListener;

    public ChukuAdapter(Context context, List<ChukuResultBean.ChukuBean> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //解决 EditText不可复制
        holder.setIsRecyclable(false);

        final ChukuResultBean.ChukuBean data = datas.get(position);
        holder.time.setText(position+1+".扫描时间:"+ TimeUtils.longToString(Long.valueOf(data.getScanningTime()),TimeUtils.timeType));
        holder.name.setText(data.getProductName());
        holder.color.setText(data.getColor());
        holder.length.setText("米数:"+data.getLength());
        holder.code.setText("BARCODE:"+data.getBarcode());
        holder.shrinkage.setText("缩率:"+data.getShrinkage());
        holder.deviceName.setText("设备名称:"+data.getDeviceName());
        holder.storeroom.setText("库房:"+data.getStoreroom());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChukuInfoActivity.class).putExtra("data",data));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.color)
        TextView color;
        @BindView(R.id.length)
        TextView length;
        @BindView(R.id.code)
        TextView code;
        @BindView(R.id.shrinkage)
        TextView shrinkage;
        @BindView(R.id.deviceName)
        TextView deviceName;
        @BindView(R.id.storeroom)
        TextView storeroom;

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