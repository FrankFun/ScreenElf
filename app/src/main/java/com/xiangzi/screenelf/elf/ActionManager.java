package com.xiangzi.screenelf.elf;

import com.xiangzi.screenelf.MyApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

/**
 * ActionManager
 *
 * @author Frank
 *         2018/2/13
 *         15866818643@163.com
 */

public class ActionManager {
    private List<ActionModel> actions = new ArrayList<>();
    private String[] paths = new String[]{"runL.gif", "runR.gif", "fear.gif", "sit.gif", "booble.gif", "fall.gif", "yoyo.gif", "bamboo_copter.gif", "woolen.gif"};
    private String[] words = new String[]{"走……走啊走", "走到九月九……", "Hey, there!", "休息一会儿", "吹泡泡", "摔倒了T_T……", "溜溜球~", "竹蜻蜓", "翻花绳"};
    private int[] duriations = new int[]{3000, 3000, 0, 10000, 8000, 3000, 9000, 3000, 6000};

    private ActionManager() {
        init();
    }

    private static class SingletonHolder {
        private static final ActionManager INSTANCE = new ActionManager();
    }

    public static ActionManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void init() {
        for (int i = 0; i < paths.length; i++) {
            ActionModel action = new ActionModel();
            action.addWord(words[i]);
            GifDrawable gifDrawable = getGifDrawable(paths[i]);
            if (gifDrawable == null) {
                continue;
            }
            action.setActionDrawable(gifDrawable);
            action.setDuriation(duriations[i]);
            actions.add(action);
        }
    }

    public List<ActionModel> getActions() {
        return actions;
    }

    private GifDrawable getGifDrawable(String path) {
        try {
            return new GifDrawable(MyApp.getInstance().getAssets(), path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
