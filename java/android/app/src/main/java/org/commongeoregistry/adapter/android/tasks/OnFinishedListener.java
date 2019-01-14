package org.commongeoregistry.adapter.android.tasks;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 11/01/2019
 */

public interface OnFinishedListener {

    void onSuccess(Object[] objects);

    void onError(Exception e);
}
