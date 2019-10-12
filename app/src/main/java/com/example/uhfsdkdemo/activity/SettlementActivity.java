package com.example.uhfsdkdemo.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.Server;
import com.example.uhfsdkdemo.adapter.AllLogAdapter;
import com.example.uhfsdkdemo.adapter.SettlementAdapter;
import com.example.uhfsdkdemo.bean.SettlementBean;
import com.example.uhfsdkdemo.utils.CustomDatePickerUtils.CustomDatePicker;
import com.example.uhfsdkdemo.utils.CustomDatePickerUtils.GetDataUtils;
import com.example.uhfsdkdemo.utils.TimeUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;

public class SettlementActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pName)
    EditText pName;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.tv_Search)
    TextView tvSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout storeHousePtrFrame;
    ArrayList<SettlementBean> mLogs = new ArrayList<>();
    private SettlementAdapter adapter;

    private int pager = 1;//数据库查询页数
    private int TotalPager = 0;//数据库总页数
    private int pagerSize = 10;//数据库每次查询数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        ButterKnife.bind(this);
        //设置横屏模式
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
//        initTool();

        initRecyview();
        initRefresh();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        startTime = GetDataUtils.getDateStrByMint(now, -180);
        lastData = GetDataUtils.getDateStrByMint(now, 0);
        tvStartTime.setText(startTime.substring(0, 10));
        tvEndTime.setText(lastData.substring(0, 10));
    }

    private void initRecyview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettlementAdapter(this, mLogs);
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void initRefresh() {
        storeHousePtrFrame.setLastUpdateTimeRelateObject(this);
        storeHousePtrFrame.setPtrHandler(new PtrHandler2() {
            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //上拉加载
                if (pager > TotalPager)
                    Toast.makeText(SettlementActivity.this, "数据已全部加载  ", Toast.LENGTH_LONG).show();
                else
                    getAllLog();
                storeHousePtrFrame.refreshComplete();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                pager = 1;
                mLogs.clear();
                getAllLog();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

        });
        // the following are default settings
        storeHousePtrFrame.setResistance(1.7f);
        storeHousePtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        storeHousePtrFrame.setDurationToClose(200);
        storeHousePtrFrame.setDurationToCloseHeader(500);
        // default is false
        storeHousePtrFrame.setPullToRefresh(false);
        // default is true
        storeHousePtrFrame.setKeepHeaderWhenRefresh(true);
        //进入界面刷新加载数据
//        storeHousePtrFrame.autoRefresh();
    }

    /**
     * 从服务器获取数据
     *
     * @Author lish
     * @Date 2019-10-12 9:05
     */
    private void getAllLog() {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
//        mAsyncHttpclient.setTimeout(60 * 1000);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);
        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/selectSettlementList";
        RequestParams params = new RequestParams();
//        page=1&pageSize=10&productName=140/素&startTime=2019-10-10&endTime=2019-10-10
        params.put("page", pager);
        params.put("pageSize", pagerSize);
        params.put("productName", pName.getText().toString().trim());
        params.put("startTime", tvStartTime.getText().toString().trim());
        params.put("endTime", tvEndTime.getText().toString().trim());

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {
                String successStr = new String(responseByte);
//                successStr = successStr.substring(1, successStr.length() - 1);
//                successStr = successStr.replace("\\", "");
                Log.e("sas", successStr);

                analysisResp(successStr);
                storeHousePtrFrame.refreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(SettlementActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();

                storeHousePtrFrame.refreshComplete();
            }
        });
    }

    private void analysisResp(String successStr) {
//                {"pages":9,"count":89,"list":[]}
        try {
            JSONObject object = new JSONObject(successStr);
            TotalPager = object.getInt("pages");
            if (TotalPager == 0) {
                Toast.makeText(SettlementActivity.this, "未查到数据！！！", Toast.LENGTH_LONG).show();
            } else {
                JSONArray array = object.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject beanObject = array.getJSONObject(i);
                    SettlementBean bean = new SettlementBean();
                    if (beanObject.has("deliveryTime"))
                        bean.setDeliverySupplierDate(TimeUtils.longToString(beanObject.getLong("deliveryTime"), "yyyy-MM-dd"));
                    else bean.setDeliverySupplierDate("");
                    bean.setIsIncludeTax(beanObject.getString("isIncludeTax"));
                    bean.setMmi(beanObject.getString("mmi"));
                    bean.setNumberOfCheckouts(beanObject.getInt("numberOfCheckouts") + "");
                    if (beanObject.has("priceAdjustment"))
                        bean.setPriceAdjustment(beanObject.getInt("priceAdjustment") + "");
                    else bean.setPriceAdjustment("");
                    bean.setProductName(beanObject.getString("productName"));
                    bean.setSupplierName(beanObject.getString("supplierName"));
                    mLogs.add(bean);
                }
                pager++;
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
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

    private String startTime = "";
    private String endTime = "";
    private String lastData;
    private CustomDatePicker startTimePicker, endTimePicker;

    /**
     * 初始化时间选择控件
     *
     * @author lish
     * created at 2018-08-13 13:42
     */
    private void initDatePicker() {

        startTimePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                startTime = time;
                tvStartTime.setText(startTime.substring(0, 10));
                showEndTimePicker();
            }
        }, "1970-01-01 00:00", lastData); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        startTimePicker.showSpecificTime(false); // 不显示时和分
        startTimePicker.setIsLoop(false); // 不允许循环滚动
        startTimePicker.setTitle("请选择开始时间");
        startTimePicker.show(startTime);
    }

    /**
     * 结束时间选择器
     *
     * @author lish
     * created at 2018-08-13 15:53
     */
    private void showEndTimePicker() {
        endTimePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time.substring(0, 10));
            }
        }, startTime, lastData); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        endTimePicker.showSpecificTime(false); // 显示时和分
        endTimePicker.setIsLoop(false); // 允许循环滚动
        endTimePicker.setTitle("请选择结束时间");
        endTimePicker.show(lastData);
    }

    @OnClick({R.id.ll_time, R.id.tv_Search, R.id.im_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_time:

                initDatePicker();
                break;

            case R.id.tv_Search:
                storeHousePtrFrame.autoRefresh();
                break;
            case R.id.im_back:
                finish();
                break;
        }
    }

}
