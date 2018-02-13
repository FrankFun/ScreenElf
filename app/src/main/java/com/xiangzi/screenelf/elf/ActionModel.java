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

    public List<String> getWord() {
        return words;
    }

    public void addWord(String word) {
        words.add(word);
    }

    public GifDrawable getActionDrawable() {
        return actionDrawable;
    }

    public void setActionDrawable(GifDrawable actionDrawable) {
        width = actionDrawable.getIntrinsicWidth();
        height = actionDrawable.getIntrinsicHeight();
        this.actionDrawable = actionDrawable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDuriation() {
        return duriation;
    }

    public void setDuriation(int duriation) {
        this.duriation = duriation;
    }
}
