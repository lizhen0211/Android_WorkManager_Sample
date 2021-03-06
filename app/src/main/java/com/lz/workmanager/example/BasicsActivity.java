package com.lz.workmanager.example;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.lz.workmanager.example.worker.CompressWorker;
import com.lz.workmanager.example.worker.MyCacheCleanupWorker;
import com.lz.workmanager.example.worker.PhotoCheckWorker;

public class BasicsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basics);
    }

    public void onOneTimeWorkRequestClick(View view) {
        oneTimeWorkRequest();
    }

    public void oneTimeWorkRequest() {
        OneTimeWorkRequest compressionWork =
                new OneTimeWorkRequest.Builder(CompressWorker.class)
                        .build();
        WorkManager.getInstance().enqueue(compressionWork);
    }

    public void onConstraintsClick(View view) {
        addConstraints();
    }

    //任务约束
    public void addConstraints() {
        // Create a Constraints object that defines when the task should run
        Constraints.Builder builder = new Constraints.Builder();
        builder = builder.setRequiresCharging(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setRequiresDeviceIdle(true);
        }
        // Many other constraints are available, see the
        // Constraints.Builder reference
        Constraints myConstraints = builder.build();

        /*myConstraints.requiresBatteryNotLow();//执行任务时电池电量不能偏低。
        myConstraints.requiresCharging();//在设备充电时才能执行任务。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myConstraints.requiresDeviceIdle();//设备空闲时才能执行。
        }
        myConstraints.requiresStorageNotLow();//设备储存空间足够时才能执行。*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myConstraints.setRequiresDeviceIdle(true);//在待机状态下执行
        }
        myConstraints.setRequiredNetworkType(NetworkType.NOT_ROAMING);//指定任务执行时的网络状态
        myConstraints.setRequiresBatteryNotLow(true);//不在电量不足时执行 默认false
        myConstraints.setRequiresCharging(true);//在充电时执行
        myConstraints.setRequiresStorageNotLow(true);//不在存储容量不足时执行

        // ...then create a OneTimeWorkRequest that uses those constraints
        OneTimeWorkRequest compressionWork =
                new OneTimeWorkRequest.Builder(CompressWorker.class)
                        .setConstraints(myConstraints)
                        .build();
        WorkManager.getInstance().enqueue(compressionWork);
    }

    //取消任务
    public void cancelWork(WorkRequest workRequest) {
        //您可以在排队后取消任务。要取消该任务，您需要其工作ID，
        //您可以从该WorkRequest对象获取该工作ID

        //取消唯一任务
        UUID compressionWorkId = workRequest.getId();
        WorkManager.getInstance().cancelWorkById(compressionWorkId);
        //取消一组带有相同标签的任务
        WorkManager.getInstance().cancelAllWorkByTag("");
        //取消所有任务
        WorkManager.getInstance().cancelAllWork();

        //WorkManager尽最大努力取消任务，但这本质上是不确定的 - 当您尝试取消任务时，任务可能已经在运行或已完成。
        //WorkManager还提供了在尽力而为的基础上取消 独特工作序列中的所有任务或具有指定标记的所有任务的方法。
    }

    //给任务加标签分组
    public void markWork() {
        //您可以通过为任何WorkRequest对象分配标记字符串来逻辑地对任务进行 分组 。
        //要设置标记，请调用 WorkRequest.Builder.addTag()
        OneTimeWorkRequest cacheCleanupTask =
                new OneTimeWorkRequest.Builder(MyCacheCleanupWorker.class)
                        //.setConstraints(myConstraints)
                        .addTag("cleanup")
                        .build();
        //这些WorkManager类提供了几种实用程序方法，使您可以使用特定标记操作所有任务。
        //例如， WorkManager.cancelAllWorkByTag() 取消具有特定标记的所有任务，
        //并 WorkManager.getStatusesByTag() 返回WorkStatus具有该标记的所有任务的所有任务的列表。
    }

    public void onPeriodicWorkRequestClick(View view) {
        periodicWorkRequest();
    }

    //重复的任务
    public void periodicWorkRequest() {
        PeriodicWorkRequest.Builder photoCheckBuilder = new PeriodicWorkRequest.Builder(PhotoCheckWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.SECONDS);
        // ...if you want, you can apply constraints to the builder here...

        // Create the actual work object:
        PeriodicWorkRequest photoCheckWork = photoCheckBuilder.build();
        // Then enqueue the recurring task:
        WorkManager.getInstance().enqueue(photoCheckWork);

        //在WorkManager尝试在您请求的时间间隔，受您带来的约束和它的其他要求运行任务。
    }
}
