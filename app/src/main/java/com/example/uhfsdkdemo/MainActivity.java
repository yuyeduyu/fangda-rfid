package com.example.uhfsdkdemo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;
import com.example.uhfsdkdemo.activity.InfoActivity;
import com.example.uhfsdkdemo.activity.MoreHandleActivity;
import com.example.uhfsdkdemo.activity.SetAdminActivity;
import com.example.uhfsdkdemo.utils.PopupMenuUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity implements OnClickListener ,OnItemClickListener{

	Toolbar mToolbar;
	private Button buttonClear;
	private Button buttonConnect;
	private Button buttonStart;
    private Button button_4;
    private Button buttonFun;
	private Button buttonIn;
	private Button buttonOut;
	private TextView textVersion ;
	private ListView listViewData;
	private ArrayList<EPC> listEPC;
	private ArrayList<Map<String, Object>> listMap;
	private boolean runFlag = true;
	private boolean startFlag = false;
	private boolean connectFlag = false;
	private UhfReader reader ; //����Ƶ��д�� 
    private TextView tv_numb;
	private ScreenStateReceiver screenReceiver ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOverflowShowingAlways();
		setContentView(R.layout.main);
		initView();
		//��ȡ��д��ʵ����������Null,�򴮿ڳ�ʼ��ʧ��
		reader = UhfReader.getInstance();
		if(reader == null){
			textVersion.setText("初始化失败");
			setButtonClickable(buttonClear, false);
			setButtonClickable(buttonStart, false);
			setButtonClickable(buttonConnect, false);
            /*setButtonClickable(buttonIn, false);
            setButtonClickable(buttonOut, false);
            setButtonClickable(button_4, false);*/
			return ;
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
	private void initView(){
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
		buttonOut.setOnClickListener(this);
		buttonIn.setOnClickListener(this);
		buttonStart.setOnClickListener(this);
        button_4.setOnClickListener(this);
		buttonConnect.setOnClickListener(this);
		buttonClear.setOnClickListener(this);
		setButtonClickable(buttonStart, false);
		listEPC = new ArrayList<EPC>();
		listViewData.setOnItemClickListener(this);

        listMap = new ArrayList<Map<String,Object>>();
        for(int i=0;i<10;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID", 0+i);
            map.put("EPC", "0000");
            map.put("COUNT",1+i);
            listMap.add(map);
        }
		listViewData.setAdapter(new SimpleAdapter(MainActivity.this,
				listMap, R.layout.listview_item,
				new String[]{"ID", "EPC", "COUNT","name","length","color","lock"}, new int[]{
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
	 * @author Administrator
	 *
	 */
	class InventoryThread extends Thread{
		private List<byte[]> epcList;

		@Override
		public void run() {
			super.run();
			while(runFlag){
				if(startFlag){
//					reader.stopInventoryMulti()
					epcList = reader.inventoryRealTime(); //ʵʱ�̴�
					if(epcList != null && !epcList.isEmpty()){
						//������ʾ��
						Util.play(1, 0);
						for(byte[] epc:epcList){
							String epcStr = Tools.Bytes2HexString(epc, epc.length);
							addToList(listEPC, epcStr);
						}
					}
					epcList = null ;
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
		private void addToList(final List<EPC> list, final String epc){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					//��һ�ζ�������
					if(list.isEmpty()){
						EPC epcTag = new EPC();
						epcTag.setEpc(epc);
						epcTag.setCount(1);
						list.add(epcTag);
					}else{
						for(int i = 0; i < list.size(); i++){
							EPC mEPC = list.get(i);
							//list���д�EPC
							if(epc.equals(mEPC.getEpc())){
							mEPC.setCount(mEPC.getCount() + 1);
							list.set(i, mEPC);
							break;
						}else if(i == (list.size() - 1)){
							//list��û�д�epc
							EPC newEPC = new EPC();
							newEPC.setEpc(epc);
							newEPC.setCount(1);
							list.add(newEPC);
							}
						}
					}
                    tv_numb.setText("读取到  "+list.size()+"  条数据");
					//��������ӵ�ListView
					listMap = new ArrayList<Map<String,Object>>();
					int idcount = 1;
					for(EPC epcdata:list){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ID", idcount);
						map.put("EPC", epcdata.getEpc());
						map.put("COUNT", epcdata.getCount());
						idcount++;
						listMap.add(map);
					}
					listViewData.setAdapter(new SimpleAdapter(MainActivity.this,
							listMap, R.layout.listview_item,
							new String[]{"ID", "EPC", "COUNT","name","length","color","lock"}, new int[]{
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
	private void setButtonClickable(Button button, boolean flag){
		button.setClickable(flag);
		if(flag){
			button.setTextColor(Color.BLACK);
		}else{
			button.setTextColor(Color.GRAY);
		}
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(screenReceiver);
		EventBus.getDefault().unregister(this);
		runFlag = false;
		if(reader != null){
			reader.close();
		}
		super.onDestroy();
	}
	/**
	 * ���listview
	 */
	private void clearData(){
		listEPC.removeAll(listEPC);
		listMap.clear();
		listViewData.setAdapter(null);
	}
	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_start:
			if(!startFlag){
				startFlag = true ;
				buttonStart.setText(R.string.stop_inventory);
			}else{
				startFlag = false;
				buttonStart.setText(R.string.inventory);
			}
			break;
		case R.id.button_connect:
			
			byte[] versionBytes = reader.getFirmware();
			if(versionBytes != null){
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
			case R.id.button_fun:
				//弹出操作界面
				PopupMenuUtil.getInstance()._show(MainActivity.this, mToolbar);
				break;
		case R.id.button_clear:
			clearData();
			break;
			case R.id.button_in:
				//入库
				new AlertDialog.Builder(MainActivity.this).setTitle("入库确认")//设置对话框标题

//					.setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容

						.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


							@Override

							public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
								if(listMap==null||listMap.size()<1){
									Toast.makeText(MainActivity.this,"数据为空",Toast.LENGTH_SHORT).show();
									return;
								}
								List<String> list = new ArrayList<String>();
								for(int i=0;i<listMap.size();i++){
									list.add(listMap.get(i).get("EPC").toString());
								}
								inData(list);
							}

						}).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



					@Override

					public void onClick(DialogInterface  dialog, int which) {//响应事件

						// TODO Auto-generated method stub


					}

				}).show();//在按键响应事件中显示此对话框
				break;
			case R.id.button_out:
				//出库
				new AlertDialog.Builder(MainActivity.this).setTitle("出库确认")//设置对话框标题

//					.setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容

						.setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮



							@Override

							public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
								if(listMap==null||listMap.size()<1){
									Toast.makeText(MainActivity.this,"数据为空",Toast.LENGTH_SHORT).show();
									return;
								}
								// TODO Auto-generated method stub
								List<String> list = new ArrayList<String>();
								for(int i=0;i<listMap.size();i++){
									list.add(listMap.get(i).get("EPC").toString());
								}
								outData(list);


							}

						}).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



					@Override

					public void onClick(DialogInterface  dialog, int which) {//响应事件

						// TODO Auto-generated method stub


					}

				}).show();//在按键响应事件中显示此对话框
				break;
            case R.id.button_4:
                //盘存
                new AlertDialog.Builder(MainActivity.this).setTitle("盘存确认")//设置对话框标题

//					.setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容

                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
								if(listMap==null||listMap.size()<1){
									Toast.makeText(MainActivity.this,"数据为空",Toast.LENGTH_SHORT).show();
									return;
								}
								List<String> list = new ArrayList<String>();
								for(int i=0;i<listMap.size();i++){
									list.add(listMap.get(i).get("EPC").toString());
								}
								if(list!=null){
									saveData(list);
								}
                            }

                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



                    @Override

                    public void onClick(DialogInterface  dialog, int which) {//响应事件

                        // TODO Auto-generated method stub


                    }

                }).show();//在按键响应事件中显示此对话框
                break;
		default:
			break;
		}
	}
    //盘存
    private void saveData( final List<String> datalist) {
        final WaitDialog waitDialog = new WaitDialog(MainActivity.this);
        waitDialog.setContent("正在修改...");
        waitDialog.show();
        AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
        String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

        String url = "http://" +remote_ip + ":" +remote_port+ "/fdsc-1.0-SNAPSHOT/inventory";

//		String url = "http://192.168.1.5:8080/fdsc/inComing";
//		String accountName = mSharedPrefs.getString("phone", null);

        RequestParams params = new RequestParams();
        params.put("rfids", new Gson().toJson(datalist));

        mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

                String successStr = new String(responseByte);
                if (successStr.equals("\"success\"")) {
                    //入库成功，清楚数据
                    Toast.makeText(MainActivity.this, "盘存成功  ", Toast.LENGTH_LONG).show();
                    tv_numb.setText("读取到  " + 0 + "  条数据");
                    clearData();
                } else {
                    //入库失败
                    Toast.makeText(MainActivity.this, "盘存失败  ", Toast.LENGTH_LONG).show();
                }

                waitDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
                if (waitDialog != null) {
                    waitDialog.dismiss();
                }

            }
        });
    }
	//入库
	private void inData( final List<String> datalist) {
		final WaitDialog waitDialog = new WaitDialog(MainActivity.this);
		waitDialog.setContent("正在修改...");
		waitDialog.show();
		AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
		SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
		String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

		String url = "http://" +remote_ip + ":" +remote_port+ "/fdsc-1.0-SNAPSHOT/inComing";

//		String url = "http://192.168.1.5:8080/fdsc/inComing";
		RequestParams params = new RequestParams();
		params.put("rfids", new Gson().toJson(datalist));

		mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

				String successStr = new String(responseByte);
				if (successStr.equals("\"success\"")) {
					//入库成功，清楚数据
					Toast.makeText(MainActivity.this, "入库成功  ", Toast.LENGTH_LONG).show();
                    tv_numb.setText("读取到  " + 0 + "  条数据");
					clearData();
				} else {
					//入库失败
					Toast.makeText(MainActivity.this, "入库失败  "+successStr, Toast.LENGTH_LONG).show();
				}

				waitDialog.dismiss();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
				Toast.makeText(MainActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
				if (waitDialog != null) {
					waitDialog.dismiss();
				}

			}
		});
	}
	//出库
	private void outData( final List<String> datalist) {
		final WaitDialog waitDialog = new WaitDialog(MainActivity.this);
		waitDialog.setContent("正在修改...");
		waitDialog.show();
		AsyncHttpClient mAsyncHttpclient = new AsyncHttpClient();
		SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String remote_ip = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
		String remote_port = mSharedPrefs.getString("remote_admin_port", Server.admin_port);

		String url = "http://" +remote_ip + ":" +remote_port+ "/fdsc-1.0-SNAPSHOT/outGoing";

//        String url = "http://192.168.1.5:8080/fdsc/outGoing";
//		String accountName = mSharedPrefs.getString("phone", null);

		RequestParams params = new RequestParams();
		params.put("rfids", new Gson().toJson(datalist));

		mAsyncHttpclient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {

				String successStr = new String(responseByte);
				if(successStr.equals("\"success\"")){
					//出库成功，清楚数据
					Toast.makeText(MainActivity.this, "出库成功  ", Toast.LENGTH_LONG).show();
                    tv_numb.setText("读取到  " + 0 +"  条数据");
					clearData();

				}else {
					//出库失败
					Toast.makeText(MainActivity.this, "出库失败  ", Toast.LENGTH_LONG).show();
				}

				waitDialog.dismiss();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
				Toast.makeText(MainActivity.this, "连接服务器失败  ", Toast.LENGTH_LONG).show();
				if (waitDialog != null) {
					waitDialog.dismiss();
				}

			}
		});
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

        new AlertDialog.Builder(MainActivity.this).setTitle("")//设置对话框标题
//					.setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容
                .setPositiveButton("查询", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                        intent.putExtra("epc", epc);
                        startActivity(intent);
                    }

                }).setNegativeButton("修改",new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface  dialog, int which) {//响应事件
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, MoreHandleActivity.class);
                intent.putExtra("epc", epc);
                startActivity(intent);
            }

        }).show();//在按键响应事件中显示此对话框

		Toast.makeText(getApplicationContext(), epc, Toast.LENGTH_SHORT).show();

    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
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
