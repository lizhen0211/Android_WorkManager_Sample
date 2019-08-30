package com.lz.workmanager.example.worker;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

/**
 * Created by lz on 2018/10/9.
 */
public class WorkA3 extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        Log.e("workmanager", "WorkA3");
        return Result.SUCCESS;
    }
}
