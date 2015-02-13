package com.example.calculatorcliente;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.web.android.service.ICalculatorService;

public class MainActivity extends Activity {

    private EditText mFirst;
    private EditText mSecond;
    private Button mAdd;
    private TextView mResultText;

    private ICalculatorService mRemoteCalculator;
    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFirst = (EditText) findViewById(R.id.firstValue);
        mSecond = (EditText) findViewById(R.id.secondValue);
        mResultText = (TextView) findViewById(R.id.resultText);
        mAdd = (Button) findViewById(R.id.add);
        
        setListeners();
        initConnection();
    }

    private void initConnection() {
        mServiceConnection = new ServiceConnection() {
            
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mRemoteCalculator = null;
            }
            
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // service is a proxy object
                mRemoteCalculator = ICalculatorService.Stub.asInterface((IBinder)service);
            }
        };
        
        if(mRemoteCalculator == null) {
            final Intent intent = new Intent("com.web.android.service.ICalculatorService");
            bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private void setListeners() {
        this.mAdd.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final int x = Integer.valueOf(mFirst.getText().toString());
                final int y = Integer.valueOf(mSecond.getText().toString());
                
                switch(v.getId()) {
                    case R.id.add: {
                        try {
                            final int result = mRemoteCalculator.add(x, y);
                            mResultText.setText(String.valueOf(result));
                        } catch(RemoteException e) {
                            e.printStackTrace();
                        }
                    } break;
                }
            }
        });
    }
}
