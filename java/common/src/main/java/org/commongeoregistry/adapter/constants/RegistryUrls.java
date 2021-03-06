package org.commongeoregistry.adapter.constants;

public class RegistryUrls
{
  public static final String REGISTRY_CONTROLLER_URL  = "cgr";
  
  /**
   * Geo Objects
   */
  
  public static final String GEO_OBJECT_GET            = "geoobject/get";
  public static final String GEO_OBJECT_GET_PARAM_ID   = "id";
  public static final String GEO_OBJECT_GET_PARAM_TYPE_CODE = "typeCode";
  
  public static final String GEO_OBJECT_GET_CODE      = "geoobject/get-code";
  public static final String GEO_OBJECT_GET_CODE_PARAM_CODE = "code";
  public static final String GEO_OBJECT_GET_CODE_PARAM_TYPE_CODE = "typeCode";

  public static final String GEO_OBJECT_UPDATE        = "geoobject/update";
  public static final String GEO_OBJECT_UPDATE_PARAM_GEOOBJECT = "geoObject";
  
  public static final String GEO_OBJECT_CREATE        = "geoobject/create";
  public static final String GEO_OBJECT_CREATE_PARAM_GEOOBJECT = "geoObject";
  
  public static final String GEO_OBJECT_GET_CHILDREN = "geoobject/getchildren";
  public static final String GEO_OBJECT_GET_CHILDREN_PARAM_PARENTID = "parentId";
  public static final String GEO_OBJECT_GET_CHILDREN_PARAM_PARENT_TYPE_CODE = "parentTypeCode";
  public static final String GEO_OBJECT_GET_CHILDREN_PARAM_CHILDREN_TYPES = "childrenType";
  public static final String GEO_OBJECT_GET_CHILDREN_PARAM_RECURSIVE = "recursive";

  public static final String GEO_OBJECT_GET_PARENTS   = "geoobject/get-parent-geoobjects";
  public static final String GEO_OBJECT_GET_PARENTS_PARAM_CHILDID = "childId";
  public static final String GEO_OBJECT_GET_PARENTS_PARAM_CHILD_TYPE_CODE = "childTypeCode";
  public static final String GEO_OBJECT_GET_PARENTS_PARAM_PARENT_TYPES = "parentTypes";
  public static final String GEO_OBJECT_GET_PARENTS_PARAM_RECURSIVE = "recursive";
  
  public static final String GEO_OBJECT_ADD_CHILD     = "geoobject/addchild";
  public static final String GEO_OBJECT_ADD_CHILD_PARAM_PARENTID = "parentId";
  public static final String GEO_OBJECT_ADD_CHILD_PARAM_PARENT_TYPE_CODE = "parentTypeCode";
  public static final String GEO_OBJECT_ADD_CHILD_PARAM_CHILDID = "childId";
  public static final String GEO_OBJECT_ADD_CHILD_PARAM_CHILD_TYPE_CODE = "childTypeCode";
  public static final String GEO_OBJECT_ADD_CHILD_PARAM_HIERARCHY_CODE = "hierarchyCode";

  public static final String GEO_OBJECT_GET_UIDS      = "geoobject/get-uids";
  public static final String GEO_OBJECT_GET_UIDS_PARAM_AMOUNT = "amount";
  
  /**
   * Geo Object Types
   */
  
  public static final String GEO_OBJECT_TYPE_GET_ALL  = "geoobjecttype/get-all";
  public static final String GEO_OBJECT_TYPE_GET_ALL_PARAM_TYPES  = "types";
  
  public static final String GEO_OBJECT_TYPE_CREATE   = "geoobjecttype/create";
  
  public static final String GEO_OBJECT_TYPE_UPDATE   = "geoobjecttype/update";
  
  public static final String GEO_OBJECT_TYPE_DELETE   = "geoobjecttype/delete";
  
  /**
   * Hierarchy Types
   */
  
  public static final String HIERARCHY_TYPE_GET_ALL   = "hierarchytype/get-all";
  
  public static final String HIERARCHY_TYPE_CREATE   = "hierarchytype/create";
  
  public static final String HIERARCHY_TYPE_UPDATE   = "hierarchytype/update";
  
  public static final String HIERARCHY_TYPE_DELETE   = "hierarchytype/delete";
  
  public static final String HIERARCHY_TYPE_ADD      = "hierarchytype/add";
  
  public static final String HIERARCHY_TYPE_REMOVE   = "hierarchytype/remove";
  
  /**
   * Miscellaneous
   */
  
  public static final String EXECUTE_ACTIONS         = "executeActions";
  public static final String EXECUTE_ACTIONS_PARAM_ACTIONS = "actions";
}
