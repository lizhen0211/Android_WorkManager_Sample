package com.lz.workmanager.example.worker;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

/**
 * Created by lz on 2018/9/30.
 */
public class MyCacheCleanupWorker extends Worker {

    @NonNull
    @Override
    public Result doWork() {
        Log.e("workmanager", "Cleanup MyCache");
        return Result.SUCCESS;
    }
}
