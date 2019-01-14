package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.commongeoregistry.adapter.android.tasks.AsyncTaskCallable;
import org.commongeoregistry.adapter.android.tasks.GenericAsyncTask;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareGeoRegistryInterface();
    }


    private void prepareGeoRegistryInterface() {

        GenericAsyncTask genericAsyncTask = new GenericAsyncTask(new AsyncTaskCallable() {
            @Override
            public Object[] call() throws Exception {
                // TODO : Not sure how to parameterize this
                AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector(); // TODO : This needs to save the id cache to the android database
                connector.setCredentials(BuildConfig.GEOREGISTRY_USERNAME, BuildConfig.GEOREGISTRY_PASSWORD);
                connector.setServerUrl(BuildConfig.GEOREGISTRY_URL);
                connector.initialize();

                AndroidRegistryClient client = new AndroidRegistryClient(connector, MainActivity.this);
                client.refreshMetadataCache();
                client.getIdSerivce().populate(10);

                String id = client.getLocalCache().nextRegistryId();
/*
        data = new USATestData(client);
        // These objects are predefined:
        TEST_ADD_CHILD = data.newTestGeoObjectInfo("TEST_ADD_CHILD", data.DISTRICT);
        data.setUp();

        // These objects do not exist in the database yet:
        UTAH = data.newTestGeoObjectInfo("Utah", data.STATE);
        CALIFORNIA = data.newTestGeoObjectInfo("California", data.STATE);*/
                return null;
            }
        });

        genericAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
