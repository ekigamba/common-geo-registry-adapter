package org.commongeoregistry.adapter.dataaccess;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParentTreeNode extends TreeNode
{

  /**
   * 
   */
  private static final long serialVersionUID      = -942907390110427275L;

  public static final String JSON_PARENTS         = "parents";
  
  private List<ParentTreeNode> parents;
  
  /**
   * 
   * 
   * @param _geoObject
   * @param _hierarchyType
   */
  public ParentTreeNode(GeoObject _geoObject, HierarchyType _hierarchyType)
  {
    super(_geoObject, _hierarchyType);
    
    this.parents = Collections.synchronizedList(new LinkedList<ParentTreeNode>());
  }
  
  /**
   * Returns the parents of the {@link GeoObject} of this {@link ParentTreeNode}
   * 
   * @return parents of the {@link GeoObject} of this {@link ParentTreeNode}
   */
  public List<ParentTreeNode> getParents() 
  {
    return this.parents;
  }

  /**
   * Add a parent to the current node.
   * 
   * @param _parents
   */
  public void addParent(ParentTreeNode _parents)
  {
    this.parents.add(_parents);
  }
  
  /**
   * Returns the relationships of the {@link ParentTreeNode}.
   * 
   * @param _json the JSON being constructed.
   * 
   * @return JSON being constructed
   */
  @Override
  protected JsonObject relationshipsToJSON(JsonObject _json)
  {
    JsonArray jaParents = new JsonArray();
    for (int i = 0; i < this.parents.size(); ++i)
    {
      ParentTreeNode parent = this.parents.get(i);
      
      jaParents.add(parent.toJSON());
    }
    
    _json.add(JSON_PARENTS, jaParents);
    
    return _json;
  }
  
  /**
   * Constructs a {@link ParentTreeNode} from the given JSON string.
   * 
   * @param sJson
   * @param registry Adapter class containing cached metadata.
   * @return
   */
  public static ParentTreeNode fromJSON(String sJson, RegistryAdapter registry)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    
    GeoObject geoObj = GeoObject.fromJSON(registry, oJson.get(JSON_GEO_OBJECT).getAsJsonObject().toString());
    
    HierarchyType hierarchyType = null;
    if (oJson.has(JSON_HIERARCHY_TYPE))
    {
      hierarchyType = registry.getMetadataCache().getHierachyType(oJson.get(JSON_HIERARCHY_TYPE).getAsString()).get();
    }
    
    ParentTreeNode tn = new ParentTreeNode(geoObj, hierarchyType);
    
    if (oJson.has(JSON_PARENTS))
    {
      JsonArray jaParents = oJson.get(JSON_PARENTS).getAsJsonArray();
      for (int i = 0; i < jaParents.size(); ++i)
      {
        JsonObject joParent = jaParents.get(i).getAsJsonObject();
        
        ParentTreeNode tnParent = ParentTreeNode.fromJSON(joParent.toString(), registry);
        
        tn.addParent(tnParent);
      }
    }
    
    return tn;
  }

}
