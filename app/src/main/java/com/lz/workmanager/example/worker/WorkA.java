package com.lz.workmanager.example.worker;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

/**
 * Created by lz on 2018/10/9.
 */
public class WorkA extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("workmanager", Thread.currentThread().getName() + " WorkA");
        return Result.SUCCESS;
    }
}
