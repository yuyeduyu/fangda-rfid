/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.uhfsdkdemo.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.uhfsdkdemo.IpEditer;
import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.Server;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class SetAdminActivity extends AppCompatActivity {
    // Debugging
    private IpEditer ip;
    private EditText port;
    private Button button_ok;
    private String remoteIP;
    private String remotePort;

    private String remoteIPNormal;
    private String remotePortNormal;

    private CheckBox cb_adress;
    private CheckBox cb_adress_spare;
    private IpEditer ip_spare;
    private EditText port_spare;
    private String remoteIPSpare;
    //    private String remotePortSpare;
    Toolbar mToolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = mSharedPrefs.edit();
        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.layout_ip_admin);
        initTool();
        ip = (IpEditer) findViewById(R.id.ip_device);
        port = (EditText) findViewById(R.id.port_device);
        cb_adress = (CheckBox) findViewById(R.id.cb_adress);
        cb_adress_spare = (CheckBox) findViewById(R.id.cb_adress_spare);
        ip_spare = (IpEditer) findViewById(R.id.ip_device_spare);
//        port_spare = (EditText) findViewById(R.id.port_device_spare);
        Boolean adressIp = mSharedPrefs.getBoolean("adress_ip", true);
        if (adressIp) {
            cb_adress.setChecked(true);
            cb_adress_spare.setChecked(false);
        } else {
            cb_adress_spare.setChecked(true);
            cb_adress.setChecked(false);
        }
        cb_adress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_adress.setChecked(true);
                cb_adress_spare.setChecked(false);

            }
        });
        cb_adress_spare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_adress_spare.setChecked(true);
                cb_adress.setChecked(false);

            }
        });
        //读取配置
//remote_admin_ip 客户端正在用的ip remote_admin_ip_spare备用ip remote_admin_ip_normal
//        remoteIP = mSharedPrefs.getString("remote_admin_ip", Server.admin_server);
//        remotePort = mSharedPrefs.getString("remote_admin_port",Server.admin_port);
        //常用IP
        remoteIPNormal = mSharedPrefs.getString("remote_admin_ip_normal", Server.admin_server);
        remotePortNormal = mSharedPrefs.getString("remote_admin_port", Server.admin_port);
        //备用Ip端口
        remoteIPSpare = mSharedPrefs.getString("remote_admin_ip_spare", Server.admin_server_spare);
//        remotePortSpare = mSharedPrefs.getString("remote_admin_port_spare",Server.admin_port_spare);
        ip.setText(remoteIPNormal);
        port.setText(remotePortNormal);

        ip_spare.setText(remoteIPSpare);
//        port_spare.setText(remotePortSpare);

        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {


                remoteIPNormal = ip.getText().toString();
                remotePortNormal = port.getText().toString();

                //重新配置

                editor.putString("remote_admin_ip_normal", remoteIPNormal);
                editor.putString("remote_admin_port", remotePortNormal);
                remoteIPSpare = ip_spare.getText().toString();
//                remotePortSpare = port_spare.getText().toString();
                //重新配置
                editor.putString("remote_admin_ip_spare", remoteIPSpare);
//                editor.putString("remote_admin_port_spare", remotePortSpare);
                editor.commit();
                if (cb_adress.isChecked()) {
                    editor.putString("remote_admin_ip", remoteIPNormal);
                    editor.putString("remote_admin_port", remotePortNormal);
                    editor.putBoolean("adress_ip", true);
                    editor.commit();
                    finish();
                } else {
                    editor.putString("remote_admin_ip", remoteIPSpare);
                    editor.putString("remote_admin_port", remotePortNormal);
                    editor.putBoolean("adress_ip", false);
                    editor.commit();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
