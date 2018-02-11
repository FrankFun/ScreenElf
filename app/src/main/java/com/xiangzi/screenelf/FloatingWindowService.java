package com.xiangzi.screenelf;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * FloatingWindowService
 *
 * @author wuyongxiang
 *         2018/2/5
 */
public class FloatingWindowService extends Service {

    private PetElf petElf;
    private View myElfView, talkview;
//    private List<String> homeList; // 桌面应用程序包名列表

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        homeList = ElfUtil.getHomes(this);

        myElfView = LayoutInflater.from(this).inflate(R.layout.petelf, null);
        talkview = LayoutInflater.from(this).inflate(R.layout.item_talk, null);
        TextView talkrighttop_tx = talkview.findViewById(R.id.talkrighttop_tx);
        ImageView elfbody = myElfView.findViewById(R.id.elfbody);

        petElf = new PetElf(getApplicationContext(), elfbody, myElfView, talkview, talkrighttop_tx);
        petElf.setPushAnimationPath("a80_3.gif");
        petElf.setStayAnimationPath(new String[]{"a80_1.gif", "a80_2.gif", "a80_5.gif", "a80_6.gif", "a80_8.gif", "a80_9.gif"});
        petElf.setTalkAnimationPath("a80_3.gif");
        petElf.setSpeak("雅蠛蝶");
        petElf.setWalkToLeftAnimationPath("runL.gif");
        petElf.setWalkToRightAnimationPath("runR.gif");
        petElf.setFlyAnimationPath("fly.gif");
        petElf.setSuccessAnimationPath("success.gif");
        petElf.Go();
    }

}





























