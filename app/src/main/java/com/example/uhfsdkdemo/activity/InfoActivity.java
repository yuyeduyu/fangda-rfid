package com.example.uhfsdkdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.Server;
import com.example.uhfsdkdemo.WaitDialog;
import com.example.uhfsdkdemo.utils.TimeUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;/*
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {
    Toolbar mToolbar;
    private TextView tv_0;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_9;
    private TextView tv_10;
    private TextView tv_11;
    private TextView tv_12;
    private TextView tv_13;
    private TextView tv_14;
    String epc="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initTool();
        tv_10 = (TextView) findViewById(R.id.tv_10);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_11 = (TextView) findViewById(R.id.tv_11);
        tv_12 = (TextView) findViewById(R.id.tv_12);
        tv_13 = (TextView) findViewById(R.id.tv_13);
        tv_14 = (TextView) findViewById(R.id.tv_14);
        Intent intent =getIntent();
        epc = intent.getStringExtra("epc");
//        epc = "170625022006";
        tv_0.setText("RFID:"+epc);
        selectData(epc);
        selectDJ(epc);
    }
    /**
     * 初始化 toolbar
     */
    private void initTool() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //查询
    private void selectData( final String data) {
        final WaitDialog waitDialog = new WaitDialog(InfoActivity.this);
        waitDialog.setContent("正在修改...");
        waitDialog.show();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" +remote_ip + ":" +remote_port+ Server.serveradress+"/findByRfid";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("rfId", data);

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                successStr = successStr.substring(1,successStr.length()-1);
                successStr=successStr.replace("\\", "");
                Log.e("sas",successStr);
//                [{\"rfidf\":\"170505008001\",\"productName\":\"114/素绉缎16\",\"color\":\"3#\"," +
//                "length":41.000,"
//                \"outTime\":\"May 9, 2017 12:00:00 AM\",\"dyeShopNumber\":\"GS170505008\"," +
//                \"crockCountNumber\":1,\"assist\":\"170505008\",\"excelServerRCID\":\"rc20170510000108\"," +
//                \"excelServerRN\":1,\"excelServerCN\":0,\"excelServerRC1\":\"\",\"excelServerWIID\":\"\"," +
//                \"excelServerRTID\":\"1022.1\",\"excelServerCHG\":0,\"status\":\"入库\"," +
//                \"storeroom\":\"色坯库\",\"barCode\":\"\",\"barCodeAssist\":\"R170505008001\"," +
//                \"rfid\":\"170505008001\"}]"

                if(successStr.equals("[]")){
                    Toast.makeText(InfoActivity.this, "未查到数据！！！", Toast.LENGTH_LONG).show();
                }else {
                    try {
                        JSONArray jsonArray=new JSONArray(successStr);
                   JSONObject jsonObject = jsonArray.getJSONObject(0);
                    //解析数据
                        String rfidf="";//RFIDF
                        String productName="";//品名
                        String color="";//颜色
                        String length="";//米数
                        String outTime="";//出厂日期
                        String dyeShopNumber="";//染厂编号
                        String crockCountNumber="";//本缸总数
                        String status="";//状态
                        String barCodeAssist="";//条形码辅助
                        String rfid="";//RFID
                        if(jsonObject.has("rfidf")){
                             rfidf = jsonObject.get("rfidf").toString();//RFIDF
                                tv_1.setText(rfidf);
                        }
                        if(jsonObject.has("productName")){
                            productName = jsonObject.getString("productName");
                            tv_2.setText(productName);
                        }
                        if(jsonObject.has("color")){
                            color = jsonObject.getString("color");//RFIDF
                            tv_3.setText(color);
                        }
                        if(jsonObject.has("length")){
                            length = jsonObject.getString("length");//RFIDF
                            tv_4.setText(length);
                        }
                        if(jsonObject.has("outTime")){
                            outTime = jsonObject.getString("outTime");//RFIDF
                            tv_5.setText(outTime);
                        }
                        if(jsonObject.has("dyeShopNumber")){
                            dyeShopNumber = jsonObject.getString("dyeShopNumber");//RFIDF
                            tv_6.setText(dyeShopNumber);
                        }
                        if(jsonObject.has("crockCountNumber")){
                            crockCountNumber = jsonObject.getString("crockCountNumber");//RFIDF
                            tv_7.setText(crockCountNumber);
                        }
                        if(jsonObject.has("status")){
                            status = jsonObject.getString("status");//RFIDF
                            tv_8.setText(status);
                        }
                        if(jsonObject.has("barCodeAssist")){
                            barCodeAssist = jsonObject.getString("barCodeAssist");//RFIDF
                            tv_9.setText(barCodeAssist);
                        }
                        if(jsonObject.has("rfid")){
                            rfid = jsonObject.getString("rfid");//RFIDF
                            tv_10.setText(rfid);
                        }
                        if(jsonObject.has("frozenMan")){
                            String frozenMan = jsonObject.getString("frozenMan");//冻结人
                            tv_12.setText(frozenMan);
                        }
                        if(jsonObject.has("startTime")){
                            String startTime = jsonObject.getString("startTime");//开始日期
                            tv_13.setText(TimeUtils.parseTime1(startTime));
                        }
                        if(jsonObject.has("endTime")){
                            String endTime = jsonObject.getString("endTime");//截止日期
                            tv_14.setText(TimeUtils.parseTime1(endTime));
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                    waitDialog.dismiss();
                    Toast.makeText(InfoActivity.this, "解析失败  ", Toast.LENGTH_LONG).show();
                }
                }

                waitDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(InfoActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });
    }
    //查询 冻结状态
    private void selectDJ( final String data) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" +remote_ip + ":" +remote_port+ Server.serveradress+"/findDJByCode";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("code", data);

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                successStr = successStr.substring(1,successStr.length()-1);
                successStr=successStr.replace("\\", "");
                Log.e("success",successStr);
                try {
                    JSONObject jsonObject=new JSONObject(successStr);
                    if(jsonObject.has("dJState")){
                        String dongJ = jsonObject.get("dJState").toString();//RFIDF
                        tv_11.setText(dongJ);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e",e.toString());
                    Toast.makeText(InfoActivity.this, "解析失败  ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(InfoActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
            }
        });
    }
}
