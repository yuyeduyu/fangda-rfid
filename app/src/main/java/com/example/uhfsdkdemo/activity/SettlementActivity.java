package com.example.uhfsdkdemo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.uhfsdkdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettlementActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        ButterKnife.bind(this);

        initTool();
    }
    /**
     * 初始化 toolbar
     */
    private void initTool() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("结算查询");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
