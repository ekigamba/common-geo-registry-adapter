package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Contains tests that run in Android and require a common geo registry server running.
 */
public class AndroidIntegrationTest
{
    private USATestData data;

    private AndroidRegistryClient client;

    private USATestData.TestGeoObjectInfo UTAH;

    private USATestData.TestGeoObjectInfo CALIFORNIA;

    private USATestData.TestGeoObjectInfo TEST_ADD_CHILD;

    @Before
    public void setUp()
    {
        Context context = InstrumentationRegistry.getTargetContext();

        // TODO : Not sure how to parameterize this
        AndroidHttpCredentialConnector connector = new AndroidHttpCredentialConnector(); // TODO : This needs to save the id cache to the android database
        connector.setCredentials("admin", "_nm8P4gfdWxGqNRQ#8");
        connector.setServerUrl("https://192.168.0.23:8443/georegistry");
        connector.initialize();

        client = new AndroidRegistryClient(connector, context);
        client.refreshMetadataCache();
        client.getIdSerivce().populate(500);

        data = new USATestData(client);
        // These objects are predefined:
        TEST_ADD_CHILD = data.newTestGeoObjectInfo("TEST_ADD_CHILD", data.DISTRICT);
        data.setUp();

        // These objects do not exist in the database yet:
        UTAH = data.newTestGeoObjectInfo("Utah", data.STATE);
        CALIFORNIA = data.newTestGeoObjectInfo("California", data.STATE);
    }

    @Test
    public void testCreateGetUpdateGeoObject()
    {
        // TODO : The AndroidRegistryCient seems to be logging in every time we make a request.
        // This may not be sustainable because we may run out of available sessions.
        // We should be managing the log in / log out state somehow.

        // 1. Create a Geo Object locally
        GeoObject goUtah = UTAH.newGeoObject();

        // 2. Send the new GeoObject to the server to be applied to the database
        GeoObject go2 = client.createGeoObject(goUtah);
        UTAH.setUid(go2.getUid());
        UTAH.assertEquals(go2);

        // 3. Retrieve the new GeoObject from the server
        GeoObject go3 = client.getGeoObject(go2.getUid(), go2.getType().getCode());
        UTAH.assertEquals(go3);

        // 4. Update the GeoObject
        final String newLabel = "MODIFIED DISPLAY LABEL";
        go3.setLocalizedDisplayLabel(newLabel);
        UTAH.setDisplayLabel(newLabel);
        GeoObject go4 = client.updateGeoObject(go3);
        UTAH.assertEquals(go4);

        // 5. Fetch it one last time to make sure our update worked
        GeoObject go5 = client.getGeoObject(go4.getUid(), go4.getType().getCode());
        UTAH.assertEquals(go5);
    }

    @Test
    public void testGetParentGeoObjects()
    {
        String childId = data.CO_D_TWO.getUid();
        String childTypeCode = data.CO_D_TWO.getUniversal().getCode();
        String[] childrenTypes = new String[]{data.COUNTRY.getCode(), data.STATE.getCode()};

        // Recursive
        ParentTreeNode tn = client.getParentGeoObjects(childId, childTypeCode, childrenTypes, true);
        data.CO_D_TWO.assertEquals(tn, childrenTypes, true);
        Assert.assertEquals(tn.toJSON().toString(), ParentTreeNode.fromJSON(tn.toJSON().toString(), client).toJSON().toString());

        // Not recursive
        ParentTreeNode tn2 = client.getParentGeoObjects(childId, childTypeCode, childrenTypes, false);
        data.CO_D_TWO.assertEquals(tn2, childrenTypes, false);
        Assert.assertEquals(tn2.toJSON().toString(), ParentTreeNode.fromJSON(tn2.toJSON().toString(), client).toJSON().toString());

        // Test only getting countries
        String[] countryArr = new String[]{data.COUNTRY.getCode()};
        ParentTreeNode tn3 = client.getParentGeoObjects(childId, childTypeCode, countryArr, true);
        data.CO_D_TWO.assertEquals(tn3, countryArr, true);
        Assert.assertEquals(tn3.toJSON().toString(), ParentTreeNode.fromJSON(tn3.toJSON().toString(), client).toJSON().toString());
    }

    @Test
    public void testGetChildGeObjects()
    {
        String[] childrenTypes = new String[]{data.STATE.getCode(), data.DISTRICT.getCode()};

        // Recursive
        ChildTreeNode tn = client.getChildGeoObjects(data.USA.getUid(), data.USA.getUniversal().getCode(), childrenTypes, true);
        data.USA.assertEquals(tn, childrenTypes, true);
        Assert.assertEquals(tn.toJSON().toString(), ChildTreeNode.fromJSON(tn.toJSON().toString(), client).toJSON().toString());

        // Not recursive
        ChildTreeNode tn2 = client.getChildGeoObjects(data.USA.getUid(), data.USA.getUniversal().getCode(), childrenTypes, false);
        data.USA.assertEquals(tn2, childrenTypes, false);
        Assert.assertEquals(tn2.toJSON().toString(), ChildTreeNode.fromJSON(tn2.toJSON().toString(), client).toJSON().toString());

        // Test only getting districts
        String[] distArr = new String[]{data.DISTRICT.getCode()};
        ChildTreeNode tn3 = client.getChildGeoObjects(data.USA.getUid(), data.USA.getUniversal().getCode(), distArr, true);
        data.USA.assertEquals(tn3, distArr, true);
        Assert.assertEquals(tn3.toJSON().toString(), ChildTreeNode.fromJSON(tn3.toJSON().toString(), client).toJSON().toString());
    }

    @Test
    public void testExecuteActions()
    {
        // Create a new GeoObject locally
        GeoObject goCali = CALIFORNIA.newGeoObject();
        client.getLocalCache().createGeoObject(goCali);

        // Update that GeoObject
        final String newLabel = "MODIFIED DISPLAY LABEL";
        goCali.setLocalizedDisplayLabel(newLabel);
        client.getLocalCache().updateGeoObject(goCali);

        Assert.assertEquals(2, client.getLocalCache().getAllActionHistory().length);
        client.pushObjectsToRegistry();

        // Fetch California and make sure it has our new display label
        GeoObject goCali2 = client.getGeoObjectByCode(CALIFORNIA.getCode(), CALIFORNIA.getUniversal().getCode());

        CALIFORNIA.setUid(goCali2.getUid());
        CALIFORNIA.setDisplayLabel(newLabel);
        CALIFORNIA.assertEquals(goCali2);

        // Update that GeoObject again
        final String newLabel2 = "MODIFIED DISPLAY LABEL2";
        goCali.setLocalizedDisplayLabel(newLabel2);
        client.getLocalCache().updateGeoObject(goCali);

        // Make sure that when we push it only pushes our new update and not the old ones again
        Assert.assertEquals(1, client.getLocalCache().getUnpushedActionHistory().length);
    }

    @Test
    public void testAddChild()
    {
        ParentTreeNode ptnTestState = client.addChild(data.WASHINGTON.getUid(), data.WASHINGTON.getUniversal().getCode(), TEST_ADD_CHILD.getUid(), TEST_ADD_CHILD.getUniversal().getCode(), data.LOCATED_IN.getCode());

        boolean found = false;
        for (ParentTreeNode ptnUSA : ptnTestState.getParents())
        {
            if (ptnUSA.getGeoObject().getCode().equals(data.WASHINGTON.getCode()))
            {
                found = true;
                break;
            }
        }
        Assert.assertTrue("Did not find our test object in the list of returned children", found);
        TEST_ADD_CHILD.assertEquals(ptnTestState.getGeoObject());

        ChildTreeNode ctnUSA2 = client.getChildGeoObjects(data.WASHINGTON.getUid(), data.WASHINGTON.getUniversal().getCode(), new String[]{data.DISTRICT.getCode()}, false);

        found = false;
        for (ChildTreeNode ctnState : ctnUSA2.getChildren())
        {
            if (ctnState.getGeoObject().getCode().equals(TEST_ADD_CHILD.getCode()))
            {
                found = true;
                break;
            }
        }
        Assert.assertTrue("Did not find our test object in the list of returned children", found);
    }
}
