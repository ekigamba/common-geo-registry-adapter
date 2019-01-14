package org.commongeoregistry.adapter.android.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;


/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 11/01/2019
 */

public class GenericAsyncTask extends AsyncTask<Void, Void, Object[]> {

    private static final String TAG = GenericAsyncTask.class.getName();
    private AsyncTaskCallable toCall;
    private OnFinishedListener onFinishedListener;

    private Exception exception;

    public GenericAsyncTask(@NonNull AsyncTaskCallable toCall) {
        this.toCall = toCall;
    }

    @Override
    protected Object[] doInBackground(Void... voids) {
        try {
            return toCall.call();
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            exception = e;
            this.cancel(true);

            return null;
        }
    }

    @Override
    protected void onPostExecute(Object[] objects) {
        if (onFinishedListener != null) {
            onFinishedListener.onSuccess(objects);
        }
    }

    @Override
    protected void onCancelled() {
        if (onFinishedListener != null) {
            Exception cancelException = exception == null ?
                    new AsyncTaskCancelledException() :
                    exception;

            onFinishedListener.onError(cancelException);
        }
    }

    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }
}
