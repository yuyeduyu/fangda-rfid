package com.example.uhfsdkdemo.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.Server;
import com.example.uhfsdkdemo.adapter.DiaoBoAdapter;
import com.example.uhfsdkdemo.bean.DiaoBoResultBean;
import com.example.uhfsdkdemo.event.Mesg;
import com.example.uhfsdkdemo.utils.CustomDatePickerUtils.CustomDatePicker;
import com.example.uhfsdkdemo.utils.CustomDatePickerUtils.GetDataUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;

public class SearchDiaoBoFragment extends Fragment {

    View v;
    @BindView(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout storeHousePtrFrame;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    LinearLayout llItemTitle;
    @BindView(R.id.time)
    TextView tvTime;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_color)
    EditText etColor;
    private int pager = 1;//数据库查询页数
    private int pagerCount = 1;//数据库中数据页数
    private int pagerSize = 100;//数据库每次查询数量
    private DiaoBoAdapter adapter;
    ArrayList<DiaoBoResultBean.DiaoBoBean> mLogs = new ArrayList<>();

    private String startTime = "";
    private String endTime = "";
    private CustomDatePicker startTimePicker, endTimePicker;
    public static SearchDiaoBoFragment newInstance() {
        SearchDiaoBoFragment fragment = new SearchDiaoBoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_search, null);
        }
        // 缓存的v需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        ButterKnife.bind(this, v);
        return v;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initRecyview();

        initDatePicker();

        initRefresh();
    }

    private void initRecyview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DiaoBoAdapter(getActivity(), mLogs);
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
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
                if (pager > pagerCount)
                    Toast.makeText(getActivity(), "数据已全部加载", Toast.LENGTH_SHORT).show();
                else
                    selectData();

                storeHousePtrFrame.refreshComplete();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //下拉刷新
                pager = 1;
                mLogs.clear();
                adapter.notifyDataSetChanged();
                selectData();

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
        storeHousePtrFrame.autoRefresh();
    }

    /**
     * 分页查询数据
     *
     * @author lish
     * created at 2018-08-01 16:10
     */
    private void selectData() {
        if (startTime.equals("") || endTime.equals("")) {
            Toast.makeText(getActivity(), "请选择起止时间", Toast.LENGTH_LONG).show();
            storeHousePtrFrame.refreshComplete();
            return;
        }
        tvTime.setText(startTime + " 至 " + endTime);
        String url = "";
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);
        url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/selectAllocationList";
        RequestParams params = new RequestParams();
        params.put("page", pager);
        Log.e("pager", pager + "    " + pager * pagerSize);
        params.put("pageSize", pagerSize);
        params.put("productName", etName.getText().toString().trim());
        params.put("color", etColor.getText().toString().trim());
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {
                storeHousePtrFrame.refreshComplete();
                String successStr = new String(responseByte);
                Log.e("sas", successStr);
                try {
                    JSONObject object = new JSONObject(successStr);
                    pagerCount = object.getInt("pages");
                    EventBus.getDefault().post(new Mesg(Mesg.updatediaoBoTabCount,object.getInt("count")));
                    if (pagerCount == 0)
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_LONG).show();
                    else {
                        analysisResp(successStr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "数据解析异常", Toast.LENGTH_LONG).show();
                    storeHousePtrFrame.refreshComplete();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "连接服务器失败  ", Toast.LENGTH_LONG).show();
                storeHousePtrFrame.refreshComplete();
            }
        });
    }

    private void analysisResp(String successStr) {
        //GSON直接解析成对象
        DiaoBoResultBean resultBean = new Gson().fromJson(successStr, DiaoBoResultBean.class);
        //对象中拿到集合
        List<DiaoBoResultBean.DiaoBoBean> userBeanList = resultBean.getList();

        if (userBeanList.size() < 1) {
            Toast.makeText(getActivity(), "未查到数据", Toast.LENGTH_SHORT).show();
        } else {
            pager++;
            mLogs.addAll(userBeanList);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.time, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time:
                startTimePicker.show(startTime);
                break;
            case R.id.search:
                pager = 1;
                mLogs.clear();
                adapter.notifyDataSetChanged();
                storeHousePtrFrame.autoRefresh();
                break;
        }
    }

    /**
     * 初始化时间选择控件
     *
     * @author lish
     * created at 2018-08-13 13:42
     */
    private String lastData;

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        startTime = now.split(" ")[0];
        endTime = now.split(" ")[0];
        tvTime.setText(startTime + " 至 " + endTime);
        lastData = GetDataUtils.getDateStrByMint(now, 0);
        startTimePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                startTime = time.split(" ")[0];
                tvTime.setText(startTime + " 至 ");
                showEndTimePicker();
            }
        }, "2010-01-01 00:00", lastData); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        startTimePicker.showSpecificTime(false); // 不显示时和分
        startTimePicker.setIsLoop(false); // 不允许循环滚动
        startTimePicker.setTitle("请选择开始时间");
    }

    /**
     * 结束时间选择器
     *
     * @author lish
     * created at 2018-08-13 15:53
     */
    private void showEndTimePicker() {
        endTimePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                endTime = time.split(" ")[0];
                tvTime.setText(startTime + " 至 " + endTime);
            }
        }, startTime + " 00:00", lastData); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        endTimePicker.showSpecificTime(false); // 显示时和分
        endTimePicker.setIsLoop(false); // 允许循环滚动
        endTimePicker.setTitle("请选择结束时间");
        endTimePicker.show(startTime);
    }

}
