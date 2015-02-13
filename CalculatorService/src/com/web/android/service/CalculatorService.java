package com.web.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class CalculatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return mCalculatorStub;
    }

    private final ICalculatorService.Stub mCalculatorStub = new ICalculatorService.Stub() {
        
        @Override
        public int add(int x, int y) throws RemoteException {
            return x + y;
        }
    };
}
