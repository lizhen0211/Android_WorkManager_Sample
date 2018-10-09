package com.lz.workmanager.example;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

/**
 * Created by lz on 2018/10/9.
 */
public class WorkC extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        Log.e("workmanager", "WorkC");
        return Result.SUCCESS;
    }
}