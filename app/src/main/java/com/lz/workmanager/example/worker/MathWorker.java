package com.lz.workmanager.example.worker;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by lz on 2018/10/9.
 */
public class MathWorker extends Worker {
    // Define the parameter keys:
    public static final String KEY_X_ARG = "X";
    public static final String KEY_Y_ARG = "Y";
    public static final String KEY_Z_ARG = "Z";
    // ...and the result key:
    public static final String KEY_RESULT = "result";

    public MathWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Worker.Result doWork() {
        // Fetch the arguments (and specify default values):
        int x = getInputData().getInt(KEY_X_ARG, 0);
        int y = getInputData().getInt(KEY_Y_ARG, 0);
        int z = getInputData().getInt(KEY_Z_ARG, 0);

        // ...do the math...
        int result = myCrazyMathFunction(x, y, z);

        //...set the output, and we're done!
        Data output = new Data.Builder()
                .putInt(KEY_RESULT, result)
                .build();
        setOutputData(output);
        //RETRY 遇到暂时性失败，
        // 此时可使用WorkRequest.Builder.setBackoffCriteria(BackoffPolicy, long, TimeUnit)来重试
        return Result.SUCCESS;
    }

    private int myCrazyMathFunction(int x, int y, int z) {
        return x + y + z;
    }
}

