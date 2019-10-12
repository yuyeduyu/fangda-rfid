package com.example.uhfsdkdemo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;
import com.example.uhfsdkdemo.EPC;
import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.ScreenStateReceiver;
import com.example.uhfsdkdemo.Server;
import com.example.uhfsdkdemo.SettingPower;
import com.example.uhfsdkdemo.Util;
import com.example.uhfsdkdemo.WaitDialog;
import com.example.uhfsdkdemo.bean.InfoData;
import com.example.uhfsdkdemo.bean.RespBean;
import com.example.uhfsdkdemo.utils.PopupMenuUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RFIDActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {
    Toolbar mToolbar;
    private Button buttonClear;
    private Button button_give;
    private Button buttonFun;
    private Button button_lock;
    private Button buttonConnect;
    private Button buttonStart;
    private Button button_4;
    private Button buttonIn;
    private Button buttonOut;
    private TextView textVersion;
    private ListView listViewData;
    private ArrayList<EPC> listEPC;
    private ArrayList<Map<String, Object>> listMap;
    private boolean runFlag = true;
    private boolean startFlag = false;
    private boolean connectFlag = false;
    private UhfReader reader; //����Ƶ��д��
    private TextView tv_numb;
    private ScreenStateReceiver screenReceiver;

    //选择的设备名称
    private String equnitorName;
    private String[] languages;//所有设备名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOverflowShowingAlways();
        setContentView(R.layout.main);
        initView();
        //��ȡ��д��ʵ����������Null,�򴮿ڳ�ʼ��ʧ��
        reader = UhfReader.getInstance();
        if (reader == null) {
            textVersion.setText("初始化失败");
            setButtonClickable(buttonClear, false);
            setButtonClickable(buttonStart, false);
            setButtonClickable(buttonConnect, false);
            /*setButtonClickable(buttonIn, false);
            setButtonClickable(buttonOut, false);
            setButtonClickable(button_4, false);*/
            return;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //��ȡ�û����ù���,������
        SharedPreferences shared = getSharedPreferences("power", 0);
        int value = shared.getInt("value", 26);
        Log.e("", "value" + value);
        reader.setOutputPower(value);


        //��ӹ㲥��Ĭ������ʱ���ߣ�����ʱ����
        screenReceiver = new ScreenStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);

        /**************************/

//		String serialNum = "";
//        try {
//            Class<?> classZ = Class.forName("android.os.SystemProperties");
//            Method get = classZ.getMethod("get", String.class);
//            serialNum = (String) get.invoke(classZ, "ro.serialno");
//        } catch (Exception e) {
//        }
//        Log.e("serialNum", serialNum);

        /*************************/

        Thread thread = new InventoryThread();
        thread.start();
        //��ʼ��������
        Util.initSoundPool(this);
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

    private void initView() {
        EventBus.getDefault().register(this);
        initTool();
        buttonFun = (Button) findViewById(R.id.button_fun);
        buttonFun.setOnClickListener(this);
        tv_numb = (TextView) findViewById(R.id.tv_numb);
        buttonStart = (Button) findViewById(R.id.button_start);
        button_4 = (Button) findViewById(R.id.button_4);
        buttonConnect = (Button) findViewById(R.id.button_connect);
        buttonClear = (Button) findViewById(R.id.button_clear);
        listViewData = (ListView) findViewById(R.id.listView_data);
        textVersion = (TextView) findViewById(R.id.textView_version);
        buttonIn = (Button) findViewById(R.id.button_in);
        buttonOut = (Button) findViewById(R.id.button_out);
        button_give = (Button) findViewById(R.id.button_give);
        button_lock = (Button) findViewById(R.id.button_lock);
        button_give.setOnClickListener(this);
        button_lock.setOnClickListener(this);
        buttonOut.setOnClickListener(this);
        buttonIn.setOnClickListener(this);
        buttonStart.setOnClickListener(this);
        button_4.setOnClickListener(this);
        buttonConnect.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        setButtonClickable(buttonStart, false);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        languages = getResources().getStringArray(R.array.arr_equnitor);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                SharedPreferences pref = RFIDActivity.this.getSharedPreferences("equnitor", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("equnitor", pos);
                editor.commit();
                equnitorName = languages[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        SharedPreferences pref = getSharedPreferences("equnitor", MODE_PRIVATE);
        int pos = pref.getInt("equnitor", -1);//第二个参数为默认值
        equnitorName = languages[0];
        if (pos != -1) {
            equnitorName = languages[pos];
            spinner.setSelection(pos, true);
        }
        listEPC = new ArrayList<EPC>();
        listViewData.setOnItemClickListener(this);

//        testData();
    }

    /**
     * c测试数据
     *
     * @Author lish
     * @Date 2018-10-31 17:11
     */
    private void testData() {
        listMap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID", 0 + i);
            map.put("EPC", "0000"+i);
            map.put("COUNT", 1 + i);
            listMap.add(map);
        }
        listViewData.setAdapter(new SimpleAdapter(RFIDActivity.this,
                listMap, R.layout.listview_item,
                new String[]{"ID", "EPC", "COUNT", "name", "length", "color", "lock"}, new int[]{
                R.id.textView_list_item_id,
                R.id.textView_list_item_barcode,
                R.id.textView_list_item_count,
                R.id.name,
                R.id.mNum,
                R.id.color,
                R.id.lock,
        }));
    }


    @Override
    protected void onPause() {
        startFlag = false;
        super.onPause();
    }

    /**
     * �̴��߳�
     *
     * @author Administrator
     */
    class InventoryThread extends Thread {
        private List<byte[]> epcList;

        @Override
        public void run() {
            super.run();
            while (runFlag) {
                if (startFlag) {
//					reader.stopInventoryMulti()
                    epcList = reader.inventoryRealTime(); //ʵʱ�̴�
                    if (epcList != null && !epcList.isEmpty()) {
                        //������ʾ��
                        Util.play(1, 0);
                        for (byte[] epc : epcList) {
                            String epcStr = Tools.Bytes2HexString(epc, epc.length);
                            addToList(listEPC, epcStr);
                        }
                    }
                    epcList = null;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //����ȡ��EPC��ӵ�LISTVIEW
    private void addToList(final List<EPC> list, final String epc) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //��һ�ζ�������
                if (list.isEmpty()) {
                    EPC epcTag = new EPC();
                    epcTag.setEpc(epc);
                    epcTag.setCount(1);
                    list.add(epcTag);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        EPC mEPC = list.get(i);
                        //list���д�EPC
                        if (epc.equals(mEPC.getEpc())) {
                            mEPC.setCount(mEPC.getCount() + 1);
                            list.set(i, mEPC);
                            break;
                        } else if (i == (list.size() - 1)) {
                            //list��û�д�epc
                            EPC newEPC = new EPC();
                            newEPC.setEpc(epc);
                            newEPC.setCount(1);
                            list.add(newEPC);
                        }
                    }
                }
                tv_numb.setText("读取到  " + list.size() + "  条数据");
                //��������ӵ�ListView
                listMap = new ArrayList<Map<String, Object>>();
                int idcount = 1;
                for (EPC epcdata : list) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ID", idcount);
                    map.put("EPC", epcdata.getEpc());
                    map.put("COUNT", epcdata.getCount());
                    idcount++;
                    listMap.add(map);
                }
                listViewData.setAdapter(new SimpleAdapter(RFIDActivity.this,
                        listMap, R.layout.listview_item,
                        new String[]{"ID", "EPC", "COUNT", "name", "length", "color", "lock"}, new int[]{
                        R.id.textView_list_item_id,
                        R.id.textView_list_item_barcode,
                        R.id.textView_list_item_count,
                        R.id.name,
                        R.id.mNum,
                        R.id.color,
                        R.id.lock,
                }));
            }
        });
    }

    //���ð�ť�Ƿ����
    private void setButtonClickable(Button button, boolean flag) {
        button.setClickable(flag);
        if (flag) {
            button.setTextColor(Color.BLACK);
        } else {
            button.setTextColor(Color.GRAY);
        }
    }

    @Override
    protected void onDestroy() {
        if (screenReceiver != null)
            unregisterReceiver(screenReceiver);
        EventBus.getDefault().unregister(this);
        runFlag = false;
        if (reader != null) {
            reader.close();
        }
        super.onDestroy();
    }

    private void clearData() {
        listEPC.removeAll(listEPC);
        listMap.clear();
        listViewData.setAdapter(null);

    }

    /**
     * ���listview
     */
    private void clearData(ArrayList<RespBean> respBeans) {
        if (respBeans.size() == (listMap == null ? 0 : listMap.size())) {
            tv_numb.setText("读取到  " + 0 + "  条数据");
            listEPC.removeAll(listEPC);
            if (listMap != null) {
                listMap.clear();
            }
            listViewData.setAdapter(null);
        } else {
            tv_numb.setText("读取到  " + ((listMap == null ? 0 : listMap.size()) - respBeans.size()) + "  条数据");
            listEPC.removeAll(listEPC);
            if (listMap != null) {
                for (int i = 0; i < listMap.size(); i++) {
                    for (int j = 0; j < respBeans.size(); j++) {
                        if (respBeans.get(j).equals(listMap.get(i).get("barcode").toString())) {
                            listMap.remove(i);
                            i--;
                            break;
                        }
                    }
                }
                listViewData.setAdapter(new SimpleAdapter(RFIDActivity.this,
                        listMap, R.layout.listview_item,
                        new String[]{"ID", "EPC", "COUNT", "name", "length", "color", "lock"}, new int[]{
                        R.id.textView_list_item_id,
                        R.id.textView_list_item_barcode,
                        R.id.textView_list_item_count,
                        R.id.name,
                        R.id.mNum,
                        R.id.color,
                        R.id.lock,
                }));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FTPEvent(String event) {
        List<String> list = new ArrayList<String>();
        switch (event) {
            case "2":
                //调拨
                if (listMap == null || listMap.size() < 1) {
                    Toast.makeText(RFIDActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < listMap.size(); i++) {
                    if (listMap.get(i).get("EPC").toString().length() < 20) {
                        list.add(listMap.get(i).get("EPC").toString());
                    }
                }
                GiveData(list);
                break;
            case "1":
                //冻结
                if (listMap == null || listMap.size() < 1) {
                    Toast.makeText(RFIDActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < listMap.size(); i++) {
                    if (listMap.get(i).get("EPC").toString().length() < 20) {
                        list.add(listMap.get(i).get("EPC").toString());
                    }
                }
                LockData(list);
                break;
            case "3":
                //入库
                if (listMap == null || listMap.size() < 1) {
                    Toast.makeText(RFIDActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < listMap.size(); i++) {
                    if (listMap.get(i).get("EPC").toString().length() < 20) {
                        list.add(listMap.get(i).get("EPC").toString());
                    }
                }
                inData(list);
                break;
            case "4":
                //出库
                if (listMap == null || listMap.size() < 1) {
                    Toast.makeText(RFIDActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < listMap.size(); i++) {
                    if (listMap.get(i).get("EPC").toString().length() < 20) {
                        list.add(listMap.get(i).get("EPC").toString());
                    }
                }
                outData(list);
                break;
            case "5":
                //盘存
                if (listMap == null || listMap.size() < 1) {
                    Toast.makeText(RFIDActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < listMap.size(); i++) {
                    if (listMap.get(i).get("EPC").toString().length() < 20) {
                        list.add(listMap.get(i).get("EPC").toString());
                    }
                }
                if (list != null) {
                    saveData(list);
                }
                break;
            case "6":
                //出库查询
                startActivity(new Intent(RFIDActivity.this, SearchActivity.class));
                break;
            case "7":
                //一键翻译
                if (listMap == null || listMap.size() < 1) {
                    Toast.makeText(RFIDActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchDataInfo(listMap);
                break;
            case "8":
                //订单查询
                startActivity(new Intent(RFIDActivity.this, OrderActivity.class));
                break;
            case "9":
                //结算查询
                startActivity(new Intent(RFIDActivity.this, SettlementActivity.class));
                break;
        }
    }

    /**
     * 一键翻译
     *
     * @param list
     * @Author lish
     * @Date 2018-10-30 13:55
     */
    private void searchDataInfo(List<Map<String, Object>> list) {

        for (int i = 0; i < list.size(); i++) {
            selectData(listMap.get(i).get("EPC").toString().trim(), i);
            selectDJ(listMap.get(i).get("EPC").toString().trim(), i);
//            selectData("170806017001", i);
//            selectDJ("170806017001", i);
        }
    }

    //查询 冻结状态
    private void selectDJ(final String data, final int i) {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/findDJByCode";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("code", data);
//        params.put("code", "170726039002");
        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                successStr = successStr.substring(1, successStr.length() - 1);
                successStr = successStr.replace("\\", "");
                Log.e("success", successStr);
                try {
                    JSONObject jsonObject = new JSONObject(successStr);
                    if (jsonObject.has("dongJ")) {
                        String dongJ = jsonObject.get("dongJ").toString();//RFIDF
                        InfoData infoData = new InfoData();
                        infoData.setId(i);
                        infoData.setLock(dongJ);
                        EventBus.getDefault().post(infoData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });
    }

    //查询
    private void selectData(final String data, final int i) {
        /*InfoData infoData = new InfoData();
        infoData.setColor("颜色"+i);
		infoData.setId(i);
		infoData.setLength("米数"+i);
		infoData.setName("名称"+i);
		infoData.setLock(i%2==0);
		EventBus.getDefault().post(infoData);*/

        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/findByRfid";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("rfId", data);

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                successStr = successStr.substring(1, successStr.length() - 1);
                successStr = successStr.replace("\\", "");
                Log.e("sas", successStr);
//                [{\"rfidf\":\"170505008001\",\"productName\":\"114/素绉缎16\",\"color\":\"3#\"," +
//                "length":41.000,"
//                \"outTime\":\"May 9, 2017 12:00:00 AM\",\"dyeShopNumber\":\"GS170505008\"," +
//                \"crockCountNumber\":1,\"assist\":\"170505008\",\"excelServerRCID\":\"rc20170510000108\"," +
//                \"excelServerRN\":1,\"excelServerCN\":0,\"excelServerRC1\":\"\",\"excelServerWIID\":\"\"," +
//                \"excelServerRTID\":\"1022.1\",\"excelServerCHG\":0,\"status\":\"入库\"," +
//                \"storeroom\":\"色坯库\",\"barCode\":\"\",\"barCodeAssist\":\"R170505008001\"," +
//                \"rfid\":\"170505008001\"}]"

                if (successStr.equals("[]")) {
//                    Toast.makeText(MainActivity.this, "未查到数据！！！", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(successStr);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        //解析数据
                        String rfidf = "";//RFIDF
                        String productName = "";//品名
                        String color = "";//颜色
                        String length = "";//米数

                        InfoData infoData = new InfoData();
//                        infoData.setColor("颜色"+i);
                        infoData.setId(i);
//                        infoData.setLength("米数"+i);
//                        infoData.setName("名称"+i);
//                        infoData.setLock(i%2==0);

                        if (jsonObject.has("productName")) {
                            productName = jsonObject.getString("productName");
                            infoData.setName(productName);
                        }
                        if (jsonObject.has("color")) {
                            color = jsonObject.getString("color");//RFIDF
                            infoData.setColor(color);
                        }
                        if (jsonObject.has("length")) {
                            length = jsonObject.getString("length");//RFIDF
                            infoData.setLength(length);
                        }
                        EventBus.getDefault().post(infoData);
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        waitDialog.dismiss();
                        Toast.makeText(RFIDActivity.this, "解析失败  ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(RFIDActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FTPEvent(InfoData event) {
        if (event.getName() != null)
            listMap.get(event.getId()).put("name", event.getName());
        if (event.getColor() != null)
            listMap.get(event.getId()).put("color", event.getColor());
        if (event.getLength() != null)
            listMap.get(event.getId()).put("length", event.getLength());
        if (event.getLock() != null) {
            if (event.getLock().equals(InfoData.isLocked)) {
                listMap.get(event.getId()).put("lock", String.valueOf(R.mipmap.lock));
            } else
                listMap.get(event.getId()).put("lock", String.valueOf(0));
        }
        Collections.sort(listMap, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double name1 = Double.valueOf(o1.get("length").toString().substring(0, o1.get("length").toString().length() - 1).toString());//name1是从你list里面拿出来的一个
                Double name2 = Double.valueOf(o2.get("length").toString().substring(0, o2.get("length").toString().length() - 1).toString()); //name1是从你list里面拿出来的第二个name
                return name1.compareTo(name2);
            }
        });
        listViewData.setAdapter(new SimpleAdapter(RFIDActivity.this,
                listMap, R.layout.listview_item,
                new String[]{"ID", "EPC", "COUNT", "name", "length", "color", "lock"}, new int[]{
                R.id.textView_list_item_id,
                R.id.textView_list_item_barcode,
                R.id.textView_list_item_count,
                R.id.name,
                R.id.mNum,
                R.id.color,
                R.id.lock,
        }));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_fun:
                //弹出操作界面
                if (startFlag) {
//                    Toast.makeText(RFIDActivity.this, "请先停止识别", Toast.LENGTH_SHORT).show();
//                    return;
                    startFlag = false;
                    buttonStart.setText(R.string.inventory);
                }
                PopupMenuUtil.getInstance()._show(RFIDActivity.this, mToolbar);
                break;
            case R.id.button_start:
                if (!startFlag) {
                    startFlag = true;
                    buttonStart.setText(R.string.stop_inventory);
                } else {
                    startFlag = false;
                    buttonStart.setText(R.string.inventory);
                }
                break;
            case R.id.button_connect:

                byte[] versionBytes = reader.getFirmware();
                if (versionBytes != null) {
//				reader.setWorkArea(3);//���ó�ŷ��
                    Util.play(1, 0);
                    String version = new String(versionBytes);
//				textVersion.setText(new String(versionBytes));
                    setButtonClickable(buttonConnect, false);
                    setButtonClickable(buttonStart, true);
                }
                setButtonClickable(buttonConnect, false);
                setButtonClickable(buttonStart, true);
                break;

            case R.id.button_clear:
                clearData();
                break;
            default:
                break;
        }
    }

    //盘存
    private void saveData(final List<String> datalist) {
        final WaitDialog waitDialog = new WaitDialog(RFIDActivity.this);
        waitDialog.setContent("正在修改...");
        waitDialog.show();
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/inventory";

//		String url = "http://192.168.1.5:8080/fdsc/inComing";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("rfids", new Gson().toJson(datalist));
        params.put("equInfor", equnitorName);
        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                analysisResp(successStr, "盘存", waitDialog);
               /* if (successStr.equals("\"success\"")) {
                    //入库成功，清楚数据
                    Toast.makeText(RFIDActivity.this, "盘存成功  ", Toast.LENGTH_LONG).show();
                    tv_numb.setText("读取到  " + 0 + "  条数据");
                    clearData();
                } else {
                    //入库失败
                    Toast.makeText(RFIDActivity.this, "盘存失败  ", Toast.LENGTH_LONG).show();
                }

                waitDialog.dismiss();
*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(RFIDActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });
    }

    //入库
    private void inData(final List<String> datalist) {
        final WaitDialog waitDialog = new WaitDialog(RFIDActivity.this);
        waitDialog.setContent("正在修改...");
        waitDialog.show();
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/inComing";
        Log.e("url", url);
//		String url = "http://192.168.1.5:8080/fdsc/inComing";
        RequestParams params = new RequestParams();
        params.put("rfids", new Gson().toJson(datalist));
//        params.put("equInfor", equnitorName);
        params.put("equInfor", "门店");

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                analysisResp(successStr, "激活", waitDialog);
				/*if (successStr.equals("\"success\"")) {
					//入库成功，清楚数据
					Toast.makeText(RFIDActivity.this, "入库成功  ", Toast.LENGTH_LONG).show();
                    tv_numb.setText("读取到  " + 0 + "  条数据");
					clearData();
				} else {
					//入库失败
					Toast.makeText(RFIDActivity.this, "入库失败  "+successStr, Toast.LENGTH_LONG).show();
				}

				waitDialog.dismiss();*/

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(RFIDActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });
    }

    //出库
    private void outData(final List<String> datalist) {
        final WaitDialog waitDialog = new WaitDialog(RFIDActivity.this);
        waitDialog.setContent("正在修改...");
        waitDialog.show();
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/outGoing";

//        String url = "http://192.168.1.5:8080/fdsc/outGoing";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("rfids", new Gson().toJson(datalist));
        params.put("equInfor", equnitorName);
        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                analysisResp(successStr, "出库", waitDialog);
				/*if(successStr.equals("\"success\"")){
					//出库成功，清楚数据
					Toast.makeText(RFIDActivity.this, "出库成功  ", Toast.LENGTH_LONG).show();
                    tv_numb.setText("读取到  " + 0 +"  条数据");
					clearData();

				}else {
					//出库失败
					Toast.makeText(RFIDActivity.this, "出库失败  ", Toast.LENGTH_LONG).show();
				}

				waitDialog.dismiss();*/

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(RFIDActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });

    }

    //冻结
    private void LockData(final List<String> datalist) {
        final WaitDialog waitDialog = new WaitDialog(RFIDActivity.this);
        waitDialog.setContent("正在提交数据...");
        waitDialog.show();
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/frozenByRfid";
        RequestParams params = new RequestParams();
        params.put("rfids", new Gson().toJson(datalist));
        params.put("equInfor", equnitorName);
        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                analysisResp(successStr, "冻结", waitDialog);
				/*if(successStr.equals("\"success\"")){
					//出库成功，清楚数据
					Toast.makeText(RFIDActivity.this, "冻结成功  ", Toast.LENGTH_LONG).show();
					tv_numb.setText("读取到  " + 0 +"  条数据");
					clearData();

				}else {
					//出库失败
					Toast.makeText(RFIDActivity.this, "冻结失败  ", Toast.LENGTH_LONG).show();
				}

				waitDialog.dismiss();*/

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(RFIDActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });
    }

    //调拨
    private void GiveData(final List<String> datalist) {
        final WaitDialog waitDialog = new WaitDialog(RFIDActivity.this);
        waitDialog.setContent("正在提交数据...");
        waitDialog.show();
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" + remote_ip + ":" + remote_port + Server.serveradress + "/allocationByRfid";

//        String url = "http://192.168.1.5:8080/fdsc/outGoing";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("rfids", new Gson().toJson(datalist));
        params.put("equInfor", equnitorName);
        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                analysisResp(successStr, "调拨", waitDialog);
				/*if(successStr.equals("\"success\"")){
					//出库成功，清楚数据
					Toast.makeText(RFIDActivity.this, "调拨成功  ", Toast.LENGTH_LONG).show();
					tv_numb.setText("读取到  " + 0 +"  条数据");
					clearData();

				}else {
					//出库失败
					Toast.makeText(RFIDActivity.this, "调拨失败  ", Toast.LENGTH_LONG).show();
				}

				waitDialog.dismiss();*/

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(RFIDActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });

    }

    private void analysisResp(String successStr, String type, WaitDialog waitDialog) {
        ArrayList<RespBean> respBeans = new ArrayList<>();
        successStr = successStr.substring(1, successStr.length() - 1).replace("\\", "");
        //创建一个Gson对象
        Gson gson = new Gson();
        //创建一个JsonParser
        JsonParser parser = new JsonParser();
        //通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
        JsonElement el = parser.parse(successStr);

        //把JsonElement对象转换成JsonArray
        JsonArray jsonArray = null;
        if (el.isJsonArray()) {
            jsonArray = el.getAsJsonArray();
        }
        //遍历JsonArray对象
        RespBean product = null;
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            JsonElement e = (JsonElement) it.next();
            //JsonElement转换为JavaBean对象
            product = gson.fromJson(e, RespBean.class);
            respBeans.add(product);
        }
        if (respBeans.size() > 0) {
            //入库成功，清楚数据
            Toast.makeText(RFIDActivity.this, type + "总数:" + (listMap == null ? 0 : listMap.size()) + "  成功" + respBeans.size()
                    + "  失败" + ((listMap == null ? 0 : listMap.size()) - respBeans.size()), Toast.LENGTH_LONG).show();
            clearData(respBeans);
        } else {
            //入库失败
            Toast.makeText(RFIDActivity.this, type + "失败  " + successStr, Toast.LENGTH_LONG).show();
        }

        if (waitDialog != null)
            waitDialog.dismiss();
    }

    private int value = 2600;
//	private int values = 432 ;
//	private int mixer = 0;
//	private int if_g = 0;

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        TextView epcTextview = (TextView) view.findViewById(R.id.textView_list_item_barcode);
        final String epc = epcTextview.getText().toString();
        //ѡ��EPC
//		reader.selectEPC(Tools.HexString2Bytes(epc));

        new AlertDialog.Builder(RFIDActivity.this).setTitle("")//设置对话框标题
//					.setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容
                .setPositiveButton("查询", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(RFIDActivity.this, InfoActivity.class);
                        intent.putExtra("epc", epc);
                        startActivity(intent);
                    }

                }).setNegativeButton("修改", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                // TODO Auto-generated method stub
                Intent intent = new Intent(RFIDActivity.this, MoreHandleActivity.class);
                intent.putExtra("epc", epc);
                startActivity(intent);
            }

        }).show();//在按键响应事件中显示此对话框

//		Toast.makeText(getApplicationContext(), epc, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingPower.class);
                startActivity(intent);
                break;
            case R.id.ip:
                Intent intent1 = new Intent(this, SetAdminActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * ��actionbar����ʾ�˵���ť
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
