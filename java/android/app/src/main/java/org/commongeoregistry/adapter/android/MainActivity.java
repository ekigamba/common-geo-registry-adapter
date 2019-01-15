package org.commongeoregistry.adapter.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.commongeoregistry.adapter.action.AbstractAction;
import org.commongeoregistry.adapter.android.tasks.AsyncTaskCallable;
import org.commongeoregistry.adapter.android.tasks.GenericAsyncTask;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // prepareGeoRegistryInterface();
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

                int count = 8;

                /*GeoObjectType[] geoObjectTypes = generateGeoobjectTypes(client, count);

                for (GeoObjectType geoObjectType: geoObjectTypes) {
                    try {
                        client.getLocalCache().
                    }  catch (ResponseException e) {
                        Log.e(TAG, Log.getStackTraceString(e));
                    }
                }*/

                /*GeoObject[] geoObjects = generateGeoObjects(client, count);

                for (GeoObject geoObject: geoObjects) {
                    client.getLocalCache().createGeoObject(geoObject);
                }*/

                //GeoObject[] androidGeoObjects = client.getChildGeoObjects()

                AbstractAction[] abstractActions = client.getLocalCache().getAllActionHistory();
                client.pushObjectsToRegistry();
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

    private GeoObject[] generateGeoObjects(@NonNull AndroidRegistryClient androidRegistryClient, int count) {
        GeoObject geoObject;
        GeoObject[] geoObjects = new GeoObject[count];

        String baseCodeName = "androidGeoObject";
        String baseLocalizedName = "Android Geo-Object ";

        for (int i = 0; i < count; i++) {
            geoObject = androidRegistryClient.newGeoObjectInstance("sampleLeaf3Code");
            geoObject.setCode(baseCodeName + i);
            geoObject.setLocalizedDisplayLabel(baseLocalizedName + i);

            geoObjects[i] = geoObject;
        }

        return geoObjects;
    }

    private GeoObjectType[] generateGeoobjectTypes(@NonNull AndroidRegistryClient androidRegistryClient, int count) {
        GeoObjectType geoObjectType;
        GeoObjectType[] geoObjectTypes = new GeoObjectType[count];

        String baseCodeName = "androidGeoObjectType";
        String baseLocalizedName = "Android Geo-object Type ";

        for (int i = 0; i < count; i++) {
            geoObjectType = new GeoObjectType(baseCodeName + "Code" + i, GeometryType.POINT, baseLocalizedName + i, "", false, androidRegistryClient);
            geoObjectTypes[i] = geoObjectType;
        }

        return geoObjectTypes;
    }

    @Override
    protected void onResume() {
        super.onResume();

        prepareGeoRegistryInterface();
    }
}
