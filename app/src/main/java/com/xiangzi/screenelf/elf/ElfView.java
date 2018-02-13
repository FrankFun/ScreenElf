package com.xiangzi.screenelf.elf;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiangzi.screenelf.R;

/**
 * PetElf
 *
 * @author wuyongxiang
 *         2018/2/5
 */
public class ElfView extends View {
    final Point size = new Point();
    private int petW = 0;
    public static final String OPERATION = "operation";
    public static final int OPERATION_SHOW = 100;
    public static final int OPERATION_HIDE = 101;

    public static final int TIMER_START = 1004;
    public static final int TIMER_STOP = 1005;
    public static final int RUN_LEFT = 1006;
    public static final int RUN_RIGHT = 1007;

    private ImageView iv_elf;
    private TextView tv_talk;
    private boolean isPushing = false;
    private WindowManager wm;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private WindowManager.LayoutParams talkParams = new WindowManager.LayoutParams();

    public ElfView(Context context) {
        super(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) wm.getDefaultDisplay().getSize(size);
        iv_elf = new ImageView(context);
        tv_talk = new TextView(context);
        tv_talk.setBackground(context.getDrawable(R.drawable.app_pref_bg));
    }

    public void Go() {
        touch();
        mHandler.sendEmptyMessage(ElfView.TIMER_START);
        createBodyView();
        createTalkView();
    }

    private void touch() {
        ((View) iv_elf).setOnTouchListener(new OnTouchListener() {
            int lastX, lastY, dx, dy;
            int paramX, paramY;
            long downTime, upTime;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.sendEmptyMessage(ElfView.TIMER_STOP);
                        downTime = System.currentTimeMillis();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        HangUp();
                        isPushing = true;
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        wm.updateViewLayout(iv_elf, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        isPushing = false;
                        upTime = System.currentTimeMillis();
                        if (Math.abs(event.getRawX() + event.getRawY() - lastX - lastY) < 40) {
                            Talk("Hey, there");
                            mHandler.sendEmptyMessageDelayed(TIMER_START, 2000);
                        } else {
                            mHandler.sendEmptyMessage(TIMER_START);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void setImageHWbyGifDrawable(ActionModel action) {
        iv_elf.setImageDrawable(action.getActionDrawable());
        params.width = action.getWidth();
        params.height = action.getHeight();
        iv_elf.setVisibility(VISIBLE);
        wm.updateViewLayout(iv_elf, params);
    }


    public void WalkToLeft() {
        setImageHWbyGifDrawable(ActionManager.getInstance().getActions().get(0));
        mHandler.sendEmptyMessage(ElfView.RUN_LEFT);

    }

    public void WalkToRight() {
        setImageHWbyGifDrawable(ActionManager.getInstance().getActions().get(1));
        mHandler.sendEmptyMessage(ElfView.RUN_RIGHT);
    }


    public void HangUp() {
        if (!isPushing) {
            setImageHWbyGifDrawable(ActionManager.getInstance().getActions().get(2));
        }
    }

    public void Talk(final String s) {
        if (params.x < 0 && params.y < 0) {
            //TL
            talkParams.x = params.x + params.width / 2;
            talkParams.y = params.y + params.height / 2 + 30;
        } else if (params.x >= 0 && params.y < 0) {
            //TR
            talkParams.x = params.x - params.width / 2;
            talkParams.y = params.y + params.height / 2 + 30;
        } else if (params.x >= 0 && params.y >= 0) {
            //BL
            talkParams.x = params.x - params.width / 2;
            talkParams.y = params.y - params.height / 2 - 40;
        } else if (params.x < 0 && params.y >= 0) {
            //BR
            talkParams.x = params.x + params.width / 2;
            talkParams.y = params.y - params.height / 2 - 40;
        }
        if (s != null) {
            tv_talk.setVisibility(View.VISIBLE);
            tv_talk.setText(s);
            talkParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            talkParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wm.updateViewLayout(tv_talk, talkParams);
        } else {
            tv_talk.setVisibility(GONE);
        }

    }

    @SuppressWarnings("static-access")
    @SuppressLint("NewApi")
    private void createBodyView() {
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888; // 设置图片
        // 格式，效果为背景透明
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        petW = size.x / 5;
        params.width = petW;
        params.height = petW;
        params.x = 0;
        params.y = 0;
        iv_elf.setVisibility(VISIBLE);
        wm.addView(iv_elf, params);
    }

    @SuppressLint("NewApi")
    private void createTalkView() {
        talkParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        talkParams.format = PixelFormat.RGBA_8888;
        talkParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        talkParams.width = 1;
        talkParams.height = 1;
        tv_talk.setVisibility(VISIBLE);
        wm.addView(tv_talk, talkParams);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_START:
                    mHandler.removeMessages(TIMER_START);
                    int j = (int) (Math.random() * 3);
                    switch (j) {
                        case 0:
                            mHandler.removeMessages(RUN_RIGHT);
                            WalkToLeft();
                            mHandler.sendEmptyMessageDelayed(ElfView.TIMER_START, ActionManager.getInstance().getActions().get(j).getDuriation());
                            break;
                        case 1:
                            mHandler.removeMessages(RUN_LEFT);
                            WalkToRight();
                            mHandler.sendEmptyMessageDelayed(ElfView.TIMER_START, ActionManager.getInstance().getActions().get(j).getDuriation());
                            break;
                        default:
                            mHandler.removeMessages(RUN_LEFT);
                            mHandler.removeMessages(RUN_RIGHT);
                            int i = (int) (Math.random() * (ActionManager.getInstance().getActions().size() - 3) + 3);
                            Talk(ActionManager.getInstance().getActions().get(i).getWord().get(0));
                            setImageHWbyGifDrawable(ActionManager.getInstance().getActions().get(i));
                            mHandler.sendEmptyMessageDelayed(ElfView.TIMER_START, ActionManager.getInstance().getActions().get(i).getDuriation());
                            break;
                    }
//                    mHandler.sendEmptyMessageDelayed(ElfView.TIMER_START, 5000 + (int) (Math.random() * 3000));
                    break;
                case TIMER_STOP:
                    mHandler.removeMessages(ElfView.TIMER_START);
                    mHandler.removeMessages(ElfView.RUN_LEFT);
                    mHandler.removeMessages(ElfView.RUN_RIGHT);
                    break;
                case RUN_LEFT:
                    mHandler.removeMessages(ElfView.RUN_LEFT);
                    params.x = params.x - (int) (Math.random() * 2 + 1);
                    wm.updateViewLayout(iv_elf, params);
                    Talk(ActionManager.getInstance().getActions().get(0).getWord().get(0));
                    if (params.x - petW / 2 < (-400)) {
                        WalkToRight();
                    } else {
                        mHandler.sendEmptyMessageDelayed(ElfView.RUN_LEFT, 50);
                    }
                    break;
                case RUN_RIGHT:
                    mHandler.removeMessages(ElfView.RUN_RIGHT);
                    params.x = params.x + (int) (Math.random() * 2 + 1);
                    wm.updateViewLayout(iv_elf, params);
                    Talk(ActionManager.getInstance().getActions().get(1).getWord().get(0));
                    if (params.x > (400 - petW / 2)) {
                        WalkToLeft();
                    } else {
                        mHandler.sendEmptyMessageDelayed(RUN_RIGHT, 50);
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
