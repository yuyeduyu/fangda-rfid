package com.example.uhfsdkdemo;

import com.android.hdhe.uhf.reader.UhfReader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * ���ù��ʣ����ھ���
 * @author Administrator
 *
 */
public class SettingPower extends AppCompatActivity implements OnClickListener{
	Toolbar mToolbar;
	private Button buttonMin;
	private Button buttonPlus;
	private Button buttonSet;
	private EditText editValues ;
	private int value = 26 ;//��ʼֵΪ���2600Ϊ26dbm(value��Χ16dbm~26dbm)
	private UhfReader reader ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_power);
		super.onCreate(savedInstanceState);
		initTool();
		initView();
		reader = UhfReader.getInstance();
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
		buttonMin = (Button) findViewById(R.id.button_min);
		buttonPlus = (Button) findViewById(R.id.button_plus);
		buttonSet = (Button) findViewById(R.id.button_set);
		editValues = (EditText) findViewById(R.id.editText_power);
		
		buttonMin.setOnClickListener(this);
		buttonPlus.setOnClickListener(this);
		buttonSet.setOnClickListener(this);
		value =  getSharedValue();
		editValues.setText("" +value);
		
	}
	
	//��ȡ�洢Value
	private int getSharedValue(){
		SharedPreferences shared = getSharedPreferences("power", 0);
		return shared.getInt("value", 26);
	}

	//����Value
	private void saveSharedValue(int value){
		SharedPreferences shared = getSharedPreferences("power", 0);
		Editor editor = shared.edit();
		editor.putInt("value", value);
		editor.commit();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_min://��
			if(value > 16){
				value = value - 1;
			}
			editValues.setText(value + "");
			break;
		case R.id.button_plus://��
			if(value < 26){
				value = value + 1;
			}
			editValues.setText(value + "");
			break;
		case R.id.button_set://����
			if(reader.setOutputPower(value)){
				saveSharedValue(value);
				Toast.makeText(getApplicationContext(), "���óɹ�", 0).show();
			}else{
				Toast.makeText(getApplicationContext(), "����ʧ��", 0).show();
			}
			break;

		default:
			break;
		}
		
	}
	
	
}
