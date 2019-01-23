package com.example.uhfsdkdemo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;
import com.example.uhfsdkdemo.R;

public class MoreHandleActivity extends AppCompatActivity implements OnClickListener{
	Toolbar mToolbar;
	private TextView textViewEPC;
	private Spinner spinnerMemBank;//������
	private EditText editPassword;//����
	private EditText editAddr;//��ʼ��ַ
	private EditText editLength;//��ȡ�����ݳ���
	private Button	buttonRead;
	private Button buttonWrite;
	private EditText editWriteData;//Ҫд�������
	private EditText editReadData;//��ȡ����չʾ��
	private Button buttonClear;
	private final String[] strMemBank = {"RESERVE", "EPC", "TID", "USER"};//RESERVE EPC TID USER�ֱ��Ӧ0,1,2,3
	private ArrayAdapter<String> adatpterMemBank;
	private Spinner spinnerLockType;//Ҫ������������
	private Button buttonLock;//������ť
	private EditText editKillPassword;//��������
	private Button buttonKill;//���ٰ�ť
	private ArrayAdapter<CharSequence> adapterLockType;
	private int membank;//������
	private int lockMembank; 
	private int addr = 0;//��ʼ��ַ
	private int length = 1;//��ȡ���ݵĳ���
	private int lockType;//
	private Button buttonBack ;
	
	private UhfReader reader;
	String epc = "";
	
	private String TAG = "MorehandleActivity";//DEBUG
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iso1800_6c);
		epc = getIntent().getStringExtra("epc");
		initView();
		listener();
		textViewEPC.setText(epc);
		//��ȡ��д��ʵ����������Null,�򴮿ڳ�ʼ��ʧ��
		reader = UhfReader.getInstance();
		if(reader == null){
			Toast.makeText(getApplicationContext(), "初始化失败", 0).show();
			return ;
		}
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
		initTool();
		textViewEPC = (TextView) findViewById(R.id.textViewEPC);
		this.spinnerMemBank = (Spinner) findViewById(R.id.spinner_membank);
		this.editAddr = (EditText) findViewById(R.id.edittext_addr);
		this.editLength = (EditText) findViewById(R.id.edittext_length);
		this.editPassword = (EditText) findViewById(R.id.editTextPassword);
		this.buttonRead = (Button) findViewById(R.id.button_read);
		this.buttonWrite = (Button) findViewById(R.id.button_write);
		this.buttonClear = (Button) findViewById(R.id.button_readClear);
		this.buttonLock = (Button) findViewById(R.id.button_lock_6c);
		this.buttonKill = (Button) findViewById(R.id.button_kill_6c);
		this.buttonBack = (Button) findViewById(R.id.button_back);
		this.editKillPassword = (EditText) findViewById(R.id.edit_kill_password);
		this.editWriteData = (EditText) findViewById(R.id.edittext_write);
		this.editReadData = (EditText) findViewById(R.id.linearLayout_readData);
		this.adatpterMemBank = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strMemBank);
		this.adatpterMemBank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinnerLockType = (Spinner) findViewById(R.id.spinner_lock_type);
		this.adapterLockType =  ArrayAdapter.createFromResource(this, R.array.arr_lockType, android.R.layout.simple_spinner_item);
		this.adapterLockType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinnerLockType.setAdapter(adapterLockType);
		this.spinnerMemBank.setAdapter(adatpterMemBank);
		
		setButtonClickable(buttonKill, false);
		setButtonClickable(buttonLock, false);
	}

	//����
	private void listener(){
		this.buttonClear.setOnClickListener(this);
		this.buttonRead.setOnClickListener(this);
		this.buttonWrite.setOnClickListener(this);
		this.buttonKill.setOnClickListener(this);
		this.buttonLock.setOnClickListener(this);
		this.buttonBack.setOnClickListener(this);
		spinnerMemBank.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				membank = arg2;
				lockMembank = arg2 + 1;
				Log.i(TAG, "memeBank " + membank);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		spinnerLockType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				lockType = arg2 ;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
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
	public void onClick(View v) {
		byte[] accessPassword = Tools.HexString2Bytes(editPassword.getText().toString());
		addr = Integer.valueOf(editAddr.getText().toString());
		length = Integer.valueOf(editLength.getText().toString());
		switch (v.getId()) {
		//����ǩ����
		case R.id.button_read:
			if(accessPassword.length != 4){
				Toast.makeText(getApplicationContext(), "����Ϊ4���ֽ�", Toast.LENGTH_SHORT).show();
				return;
			}
			//��ȡ����������
			byte[] data = reader.readFrom6C(membank, addr, length, accessPassword);
			if(data != null && data.length > 1){
				String dataStr =  Tools.Bytes2HexString(data, data.length);
//				Toast.makeText(getApplicationContext(),dataStr, 0).show();
				editReadData.append("�����ݣ�" + dataStr + "\n");
			}else{
//				Toast.makeText(getApplicationContext(), "������ʧ��", Toast.LENGTH_SHORT).show();
				if(data != null){
					editReadData.append("������ʧ�ܣ������룺" + (data[0]&0xff) + "\n");
					return;
				}
				editReadData.append("������ʧ�ܣ�����Ϊ��"  + "\n");
			}
			break;
		//д��ǩ����
		case R.id.button_write:
			if(accessPassword.length != 4){
				Toast.makeText(getApplicationContext(), "����Ϊ4���ֽ�", Toast.LENGTH_SHORT).show();
				return;
			}
			String writeData = editWriteData.getText().toString();
			if(writeData.length()%4 != 0){
				Toast.makeText(getApplicationContext(), "д�����ݵĳ�������Ϊ��λ��1word = 2bytes", Toast.LENGTH_SHORT).show();
			}
			byte[] dataBytes = Tools.HexString2Bytes(writeData);
			//dataLen = dataBytes/2 dataLen������Ϊ��λ��
			boolean writeFlag = reader.writeTo6C(accessPassword, membank, addr, dataBytes.length/2, dataBytes);
			if(writeFlag){
				editReadData.append("д���ݳɹ���"  + "\n");
			}else{
				editReadData.append("д����ʧ�ܣ�"  + "\n");
			}
			break;
		//������ǩ
		case R.id.button_lock_6c:
			
			break;
		//���ٱ�ǩ
		case R.id.button_kill_6c:
			
			break;
		//�������
		case R.id.button_readClear:
			editReadData.setText("");
			break;
		//����
		case R.id.button_back:
			finish();
			break;
		default:
			break;
		}
		
	}

}
