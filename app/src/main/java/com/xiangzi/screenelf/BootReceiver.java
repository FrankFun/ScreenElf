package com.xiangzi.screenelf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xiangzi.screenelf.elf.ElfView;

/**
 * @author FRANK
 * @Email 15866818643@163.com
 * @Date 2018/8/2 13:40
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent show = new Intent(context, FloatingWindowService.class);
        show.putExtra(ElfView.OPERATION, ElfView.OPERATION_SHOW);
        context.startService(show);
    }
}
