package com.xiangzi.screenelf.elf;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

/**
 * ActionModel
 *
 * @author Frank
 *         2018/2/13
 *         15866818643@163.com
 */

public class ActionModel {
    private List<String> words = new ArrayList<>();
    private GifDrawable actionDrawable;
    private int width = 0;
    private int height = 0;
    private int duriation = 8000;

    List<String> getWord() {
        return words;
    }

    void addWord(String word) {
        words.add(word);
    }

    GifDrawable getActionDrawable() {
        return actionDrawable;
    }

    void setActionDrawable(GifDrawable actionDrawable) {
        width = actionDrawable.getIntrinsicWidth();
        height = actionDrawable.getIntrinsicHeight();
        this.actionDrawable = actionDrawable;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int getDuriation() {
        return duriation;
    }

    void setDuriation(int duriation) {
        this.duriation = duriation;
    }
}
