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

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;

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

    public static final float g = 9800f;
    public static final float m = 1000f;
    public static final float k = 5f;
    public static final float fs = 4000f;

    public static final int SPEECH_START = 1000;
    public static final int RECOGNIZE_RESULT = 1001;
    public static final int RECOGNIZE_START = 1002;
    public static final int TIMER_START = 1004;
    public static final int TIMER_STOP = 1005;
    public static final int RUN_LEFT = 1006;
    public static final int RUN_RIGHT = 1007;
    public static final int SLEEP = 1008;
    public static final int FLY = 1009;

    private ImageView iv_elf;
    private TextView tv_talk;
    private boolean isPushing = false;
    private Context mContext;
    private WindowManager wm;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private WindowManager.LayoutParams talkParams = new WindowManager.LayoutParams();
    private WindowManager.LayoutParams sampleParams = new WindowManager.LayoutParams();
    private String[] stayAnimationPath = new String[]{"sit.gif", "booble.gif", "fall.gif", "yoyo.gif", "bamboo_copter.gif", "woolen.gif"};
    private LineView lineView;
    private float elasticX, elasticY;
    private GifDrawable hangUpDrawable,
            walkLeftGifDrawable,
            stayAnimation,
            walkRightGifDrawable,
            flyDrawable,
            successDrawable;

    public ElfView(Context context) {
        super(context);
        mContext = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) wm.getDefaultDisplay().getSize(size);
        iv_elf = new ImageView(context);
        tv_talk = new TextView(context);
        lineView = new LineView(context);
        tv_talk.setBackground(context.getDrawable(R.drawable.app_pref_bg));
    }

    public void Go() {
        try {
            hangUpDrawable = new GifDrawable(mContext.getAssets(), "fear.gif");
            walkLeftGifDrawable = new GifDrawable(mContext.getAssets(), "runL.gif");
            walkRightGifDrawable = new GifDrawable(mContext.getAssets(), "runR.gif");
            flyDrawable = new GifDrawable(mContext.getAssets(), "fly.gif");
            successDrawable = new GifDrawable(mContext.getAssets(), "success.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        touch();
        mHandler.sendEmptyMessage(ElfView.TIMER_START);
        createSampleView();
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
                        if (Math.abs(event.getRawX() + event.getRawY() - lastX - lastY) >= 40) {
                            lineView.setVisibility(VISIBLE);
                        }
                        HangUp();
                        isPushing = true;
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        float controlX = (params.x - params.width * 1.1f) - sampleParams.x + size.x * 0.5f;
                        float controlY = (params.y + params.height * 1.3f) - sampleParams.y;
                        lineView.setControlPoint(controlX, controlY);
                        wm.updateViewLayout(iv_elf, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        isPushing = false;
                        upTime = System.currentTimeMillis();
                        lineView.setVisibility(GONE);
                        if (Math.abs(event.getRawX() + event.getRawY() - lastX - lastY) < 40) {
                            v.performClick();
                        } else {
                            elasticX = params.x > 0 ? ElfUtil.getV(params.x) : -ElfUtil.getV(params.x);
                            elasticY = -ElfUtil.getV((params.y + params.height * 1.3f) - sampleParams.y);
                            if (elasticY < 0 && ((params.y + params.height * 1.3f) - sampleParams.y) > 0) {
                                mHandler.sendEmptyMessage(FLY);
                            } else {
                                mHandler.sendEmptyMessage(TIMER_START);
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        iv_elf.setOnClickListener(v -> {
            Talk("Hey, there", tv_talk);
            mHandler.sendEmptyMessageDelayed(TIMER_START, 2000);
        });
    }

    private void setImageHWbyGifDrawable(GifDrawable gifDrawable) {
        iv_elf.setImageDrawable(gifDrawable);
        params.width = gifDrawable.getIntrinsicWidth();
        params.height = gifDrawable.getIntrinsicHeight();
        iv_elf.setVisibility(VISIBLE);
        wm.updateViewLayout(iv_elf, params);
    }


    public void WalkToLeft() {
        setImageHWbyGifDrawable(walkLeftGifDrawable);
        mHandler.sendEmptyMessage(ElfView.RUN_LEFT);

    }

    public void WalkToRight() {
        setImageHWbyGifDrawable(walkRightGifDrawable);
        mHandler.sendEmptyMessage(ElfView.RUN_RIGHT);
    }

    public void Sleep() {
        int radom = new Random().nextInt(stayAnimationPath.length);
        try {
            stayAnimation = new GifDrawable(mContext.getAssets(), stayAnimationPath[radom]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setImageHWbyGifDrawable(stayAnimation);
        mHandler.sendEmptyMessage(SLEEP);
    }

    public void HangUp() {
        if (!isPushing) {
            setImageHWbyGifDrawable(hangUpDrawable);
        }
    }

    public void Fly() {
        setImageHWbyGifDrawable(flyDrawable);
    }

    public void Success() {
        setImageHWbyGifDrawable(successDrawable);
    }

    public void Talk(final String s, TextView text) {
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
            text.setText(s);
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

    @SuppressLint("NewApi")
    private void createSampleView() {
        sampleParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        sampleParams.format = PixelFormat.RGBA_8888;
        sampleParams.x = 0;
        sampleParams.y = (int) (size.y * 0.25f) + size.x / 5;
        sampleParams.width = size.x;
        sampleParams.height = (int) (size.y * 0.25f);
        lineView.setVisibility(GONE);
        wm.addView(lineView, sampleParams);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            dismissTalk();
            switch (msg.what) {
                case ElfView.SPEECH_START:
                    Talk((String) msg.obj, tv_talk);
                    break;
                case ElfView.RECOGNIZE_RESULT:

                    break;
                case ElfView.RECOGNIZE_START:

                    break;
                case ElfView.TIMER_START:
                    mHandler.removeMessages(TIMER_START);
                    int j = (int) (Math.random() * 3);
                    switch (j) {
                        case 0:
                            Sleep();
                            break;
                        case 1:
                            mHandler.removeMessages(ElfView.RUN_LEFT);
                            WalkToRight();
                            break;
                        case 2:
                            mHandler.removeMessages(ElfView.RUN_RIGHT);
                            WalkToLeft();
                            break;
                    }
                    mHandler.sendEmptyMessageDelayed(ElfView.TIMER_START, 5000 + (int) (Math.random() * 3000));
                    break;
                case ElfView.TIMER_STOP:
                    mHandler.removeMessages(ElfView.TIMER_START);
                    mHandler.removeMessages(ElfView.RUN_LEFT);
                    mHandler.removeMessages(ElfView.RUN_RIGHT);
                    break;
                case ElfView.RUN_LEFT:
                    mHandler.removeMessages(ElfView.RUN_LEFT);
                    params.x = params.x - (int) (Math.random() * 2 + 1);
                    wm.updateViewLayout(iv_elf, params);
                    Talk("running left", tv_talk);
                    if (params.x - petW / 2 < (-400)) {
                        WalkToRight();
                    } else {
                        mHandler.sendEmptyMessageDelayed(ElfView.RUN_LEFT, 50);
                    }
                    break;
                case ElfView.RUN_RIGHT:
                    mHandler.removeMessages(ElfView.RUN_RIGHT);
                    params.x = params.x + (int) (Math.random() * 2 + 1);
                    wm.updateViewLayout(iv_elf, params);
                    Talk("running right", tv_talk);
                    if (params.x > (400 - petW / 2)) {
                        WalkToLeft();
                    } else {
                        mHandler.sendEmptyMessageDelayed(ElfView.RUN_RIGHT, 50);
                    }

                    break;
                case ElfView.SLEEP:
                    Talk("Z  z  ...", tv_talk);
                    mHandler.removeMessages(ElfView.RUN_LEFT);
                    mHandler.removeMessages(ElfView.RUN_RIGHT);
                    break;
                case ElfView.FLY:
                    Fly();
                    int b = -1;
                    params.x = (int) (params.x - (elasticX * 0.02));
                    params.y = (int) (params.y + (elasticY * 0.02));
                    if (Math.abs(elasticX) < 30) {
                        elasticX = 0;
                    } else {
                        if (elasticX > 0) {
                            elasticX = elasticX - fs * 0.02f;
                        } else {
                            elasticX = elasticX + fs * 0.02f;
                        }
                    }
                    elasticY = elasticY + g * 0.02f;

                    if ((params.x) < (-400) || (params.x) > (400)) {
                        elasticX = -elasticX;
                    }
                    if (params.y < -800) {
                        if (petW + (params.y + 800) > 0) {
                            iv_elf.setAlpha((params.y + 800f) / petW);
                        } else {
                            b = 0;
                            params.x = 0;
                            params.y = 0;
                            params.height = petW;
                        }
                    }
                    if (params.y + petW / 2 > 800) {
                        if (Math.abs(elasticY) < 600) {
                            b = 1;
                        } else {
                            elasticY = -elasticY * 0.5f;
                        }
                    }

                    if (b == 0) {
                        mHandler.removeMessages(ElfView.FLY);
                        iv_elf.setRotation(0);
                        iv_elf.setAlpha(1f);
                        Success();
                        mHandler.sendEmptyMessageDelayed(TIMER_START, 2000);
                    } else if (b == 1) {
                        iv_elf.setRotation(0);
                        iv_elf.setAlpha(1f);
                        iv_elf.setVisibility(VISIBLE);
                        wm.updateViewLayout(iv_elf, params);
                        mHandler.sendEmptyMessage(TIMER_START);
                    } else {
                        iv_elf.setRotation((float) (Math.atan2(elasticY, -elasticX) * 180 / Math.PI) + 90);
                        wm.updateViewLayout(iv_elf, params);
                        mHandler.sendEmptyMessageDelayed(ElfView.FLY, 20);
                    }
                    break;
            }
        }
    };

}
