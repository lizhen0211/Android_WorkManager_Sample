package com.lz.workmanager.example.worker;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

/**
 * Created by lz on 2018/10/9.
 */
public class WorkC2 extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        Log.e("workmanager", Thread.currentThread().getName() + " WorkC2");
        return Result.SUCCESS;
    }
}
