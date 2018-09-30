package com.lz.workmanager.example;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

/**
 * Created by lz on 2018/9/30.
 */
public class PhotoCheckWorker extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        Log.e("workmanager", "Check Photo");
        return Result.SUCCESS;
    }
}
