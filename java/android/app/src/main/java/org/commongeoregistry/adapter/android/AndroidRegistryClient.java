package org.commongeoregistry.adapter.android;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import org.commongeoregistry.adapter.HttpRegistryClient;
import org.commongeoregistry.adapter.action.AbstractAction;
import org.commongeoregistry.adapter.constants.RegistryUrls;
import org.commongeoregistry.adapter.http.Connector;
import org.commongeoregistry.adapter.http.HttpResponse;
import org.commongeoregistry.adapter.http.ResponseException;
import org.commongeoregistry.adapter.http.ResponseProcessor;


public class AndroidRegistryClient extends HttpRegistryClient
{
  /**
   * 
   */
  private static final long serialVersionUID = 2367836756416546643L;
  
  private LocalObjectCache localObjectCache;
  private static final String TAG = AndroidRegistryClient.class.getName();

  /**
   *
   * @param connector URL to the common geo-registry
   */
  public AndroidRegistryClient(Connector connector, Context context)
  {
    super(connector, new AndroidSQLiteIdService());

    this.localObjectCache = new LocalObjectCache(context, this);
  }

  /**
   *
   * @param connector URL to the common geo-registry
   * @param localObjectCache LocalObjectCache to use with the client
   */
  public AndroidRegistryClient(Connector connector, LocalObjectCache localObjectCache)
  {
    super(connector, new AndroidSQLiteIdService());

    this.localObjectCache = localObjectCache;
  }

  /**
   * Returns a reference to the object that is managing the local persisted
   * cache on the Android device.
   * 
   * @return a reference to the object that is managing the local persisted
   * cache on the Android device.
   */
  public LocalObjectCache getLocalCache()
  {
    return this.localObjectCache;
  }
  
  /**
   * All modified objects that have been persisted will be pushed to the
   * 
   * common geo-registry.
   */
  public void pushObjectsToRegistry()
  {
    LocalObjectCache.AbstractActionsHolder abstractActionsHolder = this.localObjectCache.getUnpushedActionHistory();
    AbstractAction[] actions = abstractActionsHolder.getAbstractActions();
    int lastId = abstractActionsHolder.getLastId();

    if (actions.length > 0) {

      String sActions = AbstractAction.serializeActions(actions).toString();

      JsonObject params = new JsonObject();
      params.addProperty(RegistryUrls.EXECUTE_ACTIONS_PARAM_ACTIONS, sActions);

      HttpResponse resp = this.getConnector().httpPost(RegistryUrls.EXECUTE_ACTIONS, params.toString());

      try {
        ResponseProcessor.validateStatusCode(resp);

        this.localObjectCache.insertLastPushId(
                lastId,
                this.localObjectCache.getmDbHelper().getReadableDatabase()
        );
      } catch (ResponseException e) {
        Log.e(TAG, Log.getStackTraceString(e));
      }
    } else {
      Log.e(TAG, "There exists no unpushed actions");
    }
  }
}
