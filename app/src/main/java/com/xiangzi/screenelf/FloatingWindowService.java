package com.xiangzi.screenelf;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xiangzi.screenelf.elf.ElfView;

/**
 * FloatingWindowService
 *
 * @author wuyongxiang
 *         2018/2/5
 */
public class FloatingWindowService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ElfView petElf = new ElfView(getApplicationContext());
        petElf.Go();
    }

}





























