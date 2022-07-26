package com.codeitsolo.notesapp.utils;

import android.content.Context;

import com.codeitsolo.notesapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorManager {
    private static volatile ColorManager instance = null;
    int [] cardColors;

    private ColorManager(Context context) {
        cardColors = context.getResources().getIntArray(R.array.card_colors);
    }

    public static ColorManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ColorManager.class) {
                if (instance == null) {
                    instance = new ColorManager(context);
                }
            }
        }
        return instance;
    }

    public int getRandomColor() {
        Random random = new Random();
        int randomIndex = random.nextInt(cardColors.length);
        return cardColors[randomIndex];
    }
}
