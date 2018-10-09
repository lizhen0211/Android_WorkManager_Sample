package com.lz.workmanager.example;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

import static com.lz.workmanager.example.MathWorker.KEY_RESULT;
import static com.lz.workmanager.example.MathWorker.KEY_X_ARG;
import static com.lz.workmanager.example.MathWorker.KEY_Y_ARG;
import static com.lz.workmanager.example.MathWorker.KEY_Z_ARG;

public class AdvanceActivity extends Activity implements LifecycleOwner {

    public void onChainedTasksClick1(View view) {
        OneTimeWorkRequest workA = new OneTimeWorkRequest.Builder(WorkA.class).build();
        OneTimeWorkRequest workB = new OneTimeWorkRequest.Builder(WorkB.class).build();
        OneTimeWorkRequest workC = new OneTimeWorkRequest.Builder(WorkC.class).build();
        WorkManager.getInstance()
                .beginWith(workA)
                // Note: WorkManager.beginWith() returns a
                // WorkContinuation object; the following calls are
                // to WorkContinuation methods
                .then(workB)    // FYI, then() returns a new WorkContinuation instance
                .then(workC)
                .enqueue();
    }

    public void onChainedTasksClick2(View view) {
        OneTimeWorkRequest workA1 = new OneTimeWorkRequest.Builder(WorkA1.class).build();
        OneTimeWorkRequest workA2 = new OneTimeWorkRequest.Builder(WorkA2.class).build();
        OneTimeWorkRequest workA3 = new OneTimeWorkRequest.Builder(WorkA3.class).build();
        OneTimeWorkRequest workB = new OneTimeWorkRequest.Builder(WorkB.class).build();
        OneTimeWorkRequest workC1 = new OneTimeWorkRequest.Builder(WorkC1.class).build();
        OneTimeWorkRequest workC2 = new OneTimeWorkRequest.Builder(WorkC2.class).build();
        WorkManager.getInstance()
                // First, run all the A tasks (in parallel):
                .beginWith(workA1, workA2, workA3)
                // ...when all A tasks are finished, run the single B task:
                .then(workB)
                // ...then run the C tasks (in any order):
                .then(workC1, workC2)
                .enqueue();
    }

    public void onWorkContinuationClick(View view) {
        OneTimeWorkRequest workA = new OneTimeWorkRequest.Builder(WorkA.class).build();
        OneTimeWorkRequest workB = new OneTimeWorkRequest.Builder(WorkB.class).build();
        OneTimeWorkRequest workC = new OneTimeWorkRequest.Builder(WorkC.class).build();
        OneTimeWorkRequest workD = new OneTimeWorkRequest.Builder(WorkD.class).build();
        OneTimeWorkRequest workE = new OneTimeWorkRequest.Builder(WorkE.class).build();
        WorkContinuation chain1 = WorkManager.getInstance()
                .beginWith(workA)
                .then(workB);
        WorkContinuation chain2 = WorkManager.getInstance()
                .beginWith(workC)
                .then(workD);
        WorkContinuation chain3 = WorkContinuation
                .combine(chain1, chain2)
                .then(workE);
        chain3.enqueue();
    }

    /**
     * Unique work sequences
     * <p>
     * You can create a unique work sequence, by beginning the sequence with a call to beginUniqueWork() instead of beginWith(). Each unique work sequence has a name; the WorkManager only permits one work sequence with that name at a time. When you create a new unique work sequence, you specify what WorkManager should do if there's already an unfinished sequence with the same name:
     * <p>
     * Cancel the existing sequence and replace it with the new one
     * Keep the existing sequence and ignore your new request
     * Append your new sequence to the existing one, running the new sequence's first task after the existing sequence's last task finishes
     * <p>
     * Unique work sequences can be useful if you have a task that shouldn't be enqueued multiple times. For example, if your app needs to sync its data to the network, you might enqueue a sequence named "sync", and specify that your new task should be ignored if there's already a sequence with that name. Unique work sequences can also be useful if you need to gradually build up a long chain of tasks. For example, a photo editing app might let users undo a long chain of actions. Each of those undo operations might take a while, but they have to be performed in the correct order. In this case, the app could create an "undo" chain and append each undo operation to the chain as needed.
     */


    public void onInputAndOutputClick(View view) {
        // Create the Data object:
        Data myData = new Data.Builder()
                // We need to pass three integers: X, Y, and Z
                .putInt(KEY_X_ARG, 42)
                .putInt(KEY_Y_ARG, 421)
                .putInt(KEY_Z_ARG, 8675309)
                // ... and build the actual Data object:
                .build();

        // ...then create and enqueue a OneTimeWorkRequest that uses those arguments
        OneTimeWorkRequest mathWork = new OneTimeWorkRequest.Builder(MathWorker.class)
                .setInputData(myData)
                .build();
        WorkManager.getInstance().enqueue(mathWork);
        //获取任务执行结果
        LiveData<WorkStatus> statusById = WorkManager.getInstance().getStatusById(mathWork.getId());
        statusById
                .observe(this, new Observer<WorkStatus>() {
                    @Override
                    public void onChanged(@Nullable WorkStatus workStatus) {
                        if (workStatus == null) return;
                        Log.e("workmanager", "status:" + workStatus.getState().toString());
                        if (workStatus.getState().isFinished()) {
                            //获取结果
                            int myResult = workStatus.getOutputData().getInt(KEY_RESULT, 0);
                            Log.e("workmanager", "result:" + String.valueOf(myResult));
                            // ... do something with the result ...
                        }

                    }
                });

    }

    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

}
