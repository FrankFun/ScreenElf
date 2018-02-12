package com.xiangzi.screenelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xiangzi.screenelf.elf.ElfView;

/**
 * Floating_windowActivity
 *
 * @author wuyongxiang
 *         2018/2/5
 */


public class Floating_windowActivity extends Activity {

    boolean go = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (go) {
            dismiss();
        } else {
            go();
        }
        finish();
    }

    public void go() {
        Intent show = new Intent(this, FloatingWindowService.class);
        show.putExtra(ElfView.OPERATION, ElfView.OPERATION_SHOW);
        startService(show);
    }

    public void dismiss() {
        Intent hide = new Intent(this, FloatingWindowService.class);
        hide.putExtra(ElfView.OPERATION, ElfView.OPERATION_HIDE);
        stopService(hide);
    }
}