package com.example.uhfsdkdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.bean.ChukuResultBean;
import com.example.uhfsdkdemo.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChukuInfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    ChukuResultBean.ChukuBean data;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_devname)
    TextView tvDevname;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_orderNo)
    TextView tvOrderNo;
    @BindView(R.id.tv_timeKct)
    TextView tvTimeKct;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_color)
    TextView tvColor;
    @BindView(R.id.tv_lenth)
    TextView tvLenth;
    @BindView(R.id.tv_shrinkage)
    TextView tvShrinkage;
    @BindView(R.id.tv_storeroom)
    TextView tvStoreroom;
    @BindView(R.id.tv_frozenMan)
    TextView tvFrozenMan;
    @BindView(R.id.tv_isOut)
    TextView tvIsOut;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.tv_isHandFilling)
    TextView tvIsHandFilling;
    @BindView(R.id.tv_outOrderNo)
    TextView tvOutOrderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuku_info);
        ButterKnife.bind(this);
        initTool();
        Intent intent = getIntent();
        data = (ChukuResultBean.ChukuBean) intent.getSerializableExtra("data");
        if (data == null) {
            Toast.makeText(ChukuInfoActivity.this, "获取数据出错，请退出界面重新进入", Toast.LENGTH_SHORT).show();
        } else {
            shownInfo();
        }
    }

    private void shownInfo() {
        tvCode.setText(data.getBarcode());
        tvDevname.setText(data.getDeviceName());
        tvTime.setText(TimeUtils.longToString(Long.valueOf(data.getScanningTime()),TimeUtils.timeType));
        tvOrderNo.setText(data.getOrderNo());
        tvTimeKct.setText(data.getTimeKct());
        tvName.setText(data.getProductName());
        tvColor.setText(data.getColor());
        tvLenth.setText(data.getLength()+"");
        tvShrinkage.setText(data.getShrinkage());
        tvStoreroom.setText(data.getStoreroom());
        tvFrozenMan.setText(data.getFrozenMan());
        tvIsOut.setText(data.getIsOut());
        tvStartTime.setText(TimeUtils.longToString(Long.valueOf(data.getStartTime()),TimeUtils.timeType));
        tvEndTime.setText(TimeUtils.longToString(Long.valueOf(data.getEndTime()),TimeUtils.timeType));
        tvIsHandFilling.setText(data.getIsHandFilling());
        tvOutOrderNo.setText(data.getOutOrderNo());
    }

    /**
     * 初始化 toolbar
     */
    private void initTool() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("出库记录详情");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
