package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A {@link GeoObjectType} represents the definition of a location type, such as state, county, province, district, village
 * household, or health facility.  A {@link GeoObjectType} specifies the geometry type stored and the feature attributes on
 * the {@link GeoObject}s that are instances of the {@link GeoObjectType}. {@link GeoObjectType} objects are used to define 
 * integrity constraints on the user 
 * 
 * @author nathan
 * @author rrowlands
 *
 */
public class GeoObjectType implements Serializable
{
  /**
   * 
   */
  private static final long          serialVersionUID               = 2857923921744440744L;

  
  public static final String         JSON_ATTRIBUTES                = "attributes";
  
  public static final String         JSON_CODE                      = "code";
  
  public static final String         JSON_LOCALIZED_LABEL           = "localizedLabel";
  
  public static final String         JSON_LOCALIZED_DESCRIPTION     = "localizedDescription";
  
  public static final String         JSON_GEOMETRY_TYPE             = "geometryType";
 
  public static final String         JSON_IS_LEAF                   = "isLeaf";
  
  /**
   * Unique but human readable identifier. It could be VILLAGE or HOUSEHOLD.
   */
  private String                     code;
  
  /**
   * Type of geometry used for instances of this {@link GeoObjectType}, such as point, line, polygon, etc.
   */
  private GeometryType               geometryType;
  

  /**
   * The localized label of this type, such as Village or Household. Used for display in the presentation tier.
   */
  private String                     localizedLabel;
  

  /**
   * The localized description of this type, used for display in the presentation tier.
   */
  private String                     localizedDescription;
  
  /**
   * Indicates whether the type that can only be added as a leaf to a hierarchy. Certain types, like households and structures, if added to a tree
   * would cause the tree to grow to a very large size. Rather, instances of these types in the back-end will reference parent nodes in the tre.
   */
  private Boolean                    isLeaf;
  

  /**
   * Collection of {@link AttributeType} metadata attributes.
   * 
   * key: {@code AttributeType#getName()}
   * 
   * value: {@code AttributeType}
   * 
   */
  private Map<String, AttributeType> attributeMap;

  /**
   * 
   * 
   * 
   * @param code unique identifier that his human readable.
   * @param geometryType type of geometry for the {@link GeoObjectType} such as point, line, etc.
   * @param localizedLabel localized label of the {@link GeoObjectType}.
   * @param localizedDescription localized description of the {@link GeoObjectType}.
   * @param isLeaf True if the type is a leaf, false otherwise.
   * @param registry {@link RegistryAdapter} from which this {@link GeoObjectType} is defined. 
   */
  public GeoObjectType(String code, GeometryType geometryType, String localizedLabel, String localizedDescription, Boolean isLeaf, RegistryAdapter registry)
  {
    this.init(code, geometryType, localizedLabel, localizedDescription, isLeaf);

    this.attributeMap = buildDefaultAttributes(registry);
  }

  /**
   * 
   * 
   * 
   * @param code unique identifier that his human readable.
   * @param geometryType type of geometry for the {@link GeoObjectType} such as point, line, etc.
   * @param localizedLabel localized label of the {@link GeoObjectType}.
   * @param localizedDescription localized description of the {@link GeoObjectType}.
   * @param isLeaf True if the type is a leaf, false otherwise.
   * @param attributeMap attribute map.
   */
  private GeoObjectType(String code, GeometryType geometryType, String localizedLabel, String localizedDescription, Boolean isLeaf, Map<String, AttributeType> attributeMap)
  {
    this.init(code, geometryType, localizedLabel, localizedDescription, isLeaf);

    this.attributeMap = attributeMap;
  }
  

  /**
   * Initializes member variables.
   * 
   * @param code
   * @param geometryType
   * @param localizedLabel
   * @param localizedDescription
   * @param isLeaf
   * 
   */
  private void init(String code, GeometryType geometryType, String localizedLabel,
      String localizedDescription, Boolean isLeaf)
  {
    this.code = code;
    this.localizedLabel = localizedLabel;
    this.localizedDescription = localizedDescription;
    
    this.geometryType = geometryType;
    
    this.isLeaf = isLeaf;
  }
  
  
  /**
   * Createss a new instance of the current object and copies the attributes from the given {@link GeoObject} into this object.
   *  
   * @param gotSource {@link GeoObject} with attributes to copy into this attribute.

   * @return this {@link GeoObject}
   */
  public GeoObjectType copy(GeoObjectType gotSource)
  {
    
    GeoObjectType newGeoObjt = new GeoObjectType(this.code, this.geometryType, this.localizedLabel, this.localizedDescription, this.isLeaf, this.attributeMap);
    
    newGeoObjt.code = gotSource.getCode();
    newGeoObjt.localizedLabel = gotSource.getLocalizedLabel();
    newGeoObjt.localizedDescription = gotSource.getLocalizedDescription();
    
    newGeoObjt.geometryType = gotSource.getGeometryType();
    
    newGeoObjt.isLeaf = gotSource.isLeaf();
    
    return newGeoObjt;
  }
  

  /**
   * Returns the code which is the human readable unique identifier.
   * 
   * @return Code value.
   */
  public String getCode()
  {
    return this.code;
  }

  /**
   * Returns the {@link GeometryType} supported for instances of this {@link GeoObjectType}.
   * 
   * @return {@link GeometryType}.
   */
  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }
  
  /**
   * Returns the localized label of this {@link GeoObjectType} used for the presentation layer.
   * 
   * @return Localized label of this {@link GeoObjectType}.
   */
  public String getLocalizedLabel()
  {
    return this.localizedLabel;
  }
  
  /**
   * Sets the localized display label of this {@link GeoObjectType}.
   * 
   * @param localizedLabel
   */
  public void setLocalizedLabel(String localizedLabel)
  {
    this.localizedLabel = localizedLabel;
  }

  /**
   * Returns true if the type is a leaf node, false otherwise.
   * 
   * @return true if the type is a leaf node, false otherwise.
   */
  public Boolean isLeaf()
  {
    return this.isLeaf;
  }

  /**
   * True if the type is a leaf node, false otherwise.
   * 
   * @param isLeaf True if the type is a leaf node, false otherwise.
   */
  public void isLeaf(Boolean isLeaf)
  {
    this.isLeaf = isLeaf;
  }
  
  /**
   * Returns the localized description of this {@link GeoObjectType} used for the presentation layer.
   * 
   * @return Localized description of this {@link GeoObjectType}.
   */
  public String getLocalizedDescription()
  {
    return this.localizedDescription;
  }


  /**
   * Sets the localized display label of this {@link GeoObjectType}.
   * 
   * @param localizedDescription
   */
  public void setLocalizedDescription(String localizedDescription)
  {
    this.localizedDescription = localizedDescription;
  }
  
  /**
   * Returns the {@link AttributeType} defined on this {@link GeoObjectType} with the given name.
   * 
   * @param name Name of the attribute {@code AttributeType#getName()}.
   * 
   * @pre Attribute with the given name is defined on this {@link GeoObjectType}.
   * 
   * @return Name of the attributes.
   */
  public Optional<AttributeType> getAttribute(String name)
  {
    return Optional.of(this.attributeMap.get(name));
  }
  
  /**
   * Adds the given {@link AttributeType} as an attribute defined on this {@link GeoObjectType}s.
   * 
   * @param attributeType {@link AttributeType} to add to this {@link GeoObjectType}.
   */
  public void addAttribute(AttributeType attributeType)
  {
    this.attributeMap.put(attributeType.getName(), attributeType);
  }
  
  /**
   * Returns the {@link AttributeType} objects of the attributes defined on this @link GeoObjectType}.
   * 
   * @return {@link AttributeType} objects of the attributes defined on this @link GeoObjectType}.
   */
  public Map<String, AttributeType> getAttributeMap()
  {
    return this.attributeMap;
  }
  
  /**
   * Defines the standard set of {@link AttributeType} defined on all{@link GeoObjectType}s.
   * 
   * @return the standard set of {@link AttributeType} defined on all{@link GeoObjectType}s.
   */
  private static Map<String, AttributeType> buildDefaultAttributes(RegistryAdapter registry)
  {
    Map<String, AttributeType> defaultAttributeMap = new ConcurrentHashMap<String, AttributeType>();
        
    AttributeCharacterType uid = (AttributeCharacterType)DefaultAttribute.UID.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.UID.getName(), uid);
    
    AttributeCharacterType code = (AttributeCharacterType)DefaultAttribute.CODE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.CODE.getName(), code);
    
    AttributeCharacterType displayLabel = (AttributeCharacterType)DefaultAttribute.LOCALIZED_DISPLAY_LABEL.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.LOCALIZED_DISPLAY_LABEL.getName(), displayLabel);
    
    AttributeCharacterType type = (AttributeCharacterType)DefaultAttribute.TYPE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.TYPE.getName(), type);
    
    AttributeIntegerType sequence = (AttributeIntegerType)DefaultAttribute.SEQUENCE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.SEQUENCE.getName(), sequence);
    
    AttributeDateType createdDate = (AttributeDateType)DefaultAttribute.CREATE_DATE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.CREATE_DATE.getName(), createdDate);
    
    AttributeDateType updatedDate = (AttributeDateType)DefaultAttribute.LAST_UPDATE_DATE.createAttributeType();
    defaultAttributeMap.put(DefaultAttribute.LAST_UPDATE_DATE.getName(), updatedDate);
    
    AttributeTermType status = (AttributeTermType)DefaultAttribute.STATUS.createAttributeType();
    Term rootStatusTerm = registry.getMetadataCache().getTerm(DefaultTerms.GeoObjectStatusTerm.ROOT.code).get();
    status.setRootTerm(rootStatusTerm);
    defaultAttributeMap.put(DefaultAttribute.STATUS.getName(), status);
    
    return defaultAttributeMap;
  }
  
  public static GeoObjectType[] fromJSONArray(String saJson, RegistryAdapter adapter)
  {
    JsonParser parser = new JsonParser();

    JsonArray jaGots = parser.parse(saJson).getAsJsonArray();
    GeoObjectType[] gots = new GeoObjectType[jaGots.size()];
    for (int i = 0; i < jaGots.size(); ++i)
    {
      GeoObjectType got = GeoObjectType.fromJSON(jaGots.get(i).toString(), adapter);
      gots[i] = got;
    }
    
    return gots;
  }
  
  /**
   * Creates a {@link GeoObjectType} from the given JSON string.
   * 
   * @param sJson JSON string that defines the {@link GeoObjectType}.
   * @param registry {@link RegistryAdapter} from which this {@link GeoObjectType} object comes. 
   * @return
   */
  public static GeoObjectType fromJSON(String sJson, RegistryAdapter registry)
  {
    JsonParser parser = new JsonParser();
    
    JsonObject oJson = parser.parse(sJson).getAsJsonObject();
    JsonArray oJsonAttrs = oJson.getAsJsonArray(JSON_ATTRIBUTES);
    
    String code = oJson.get(JSON_CODE).getAsString();
    String localizedLabel = oJson.get(JSON_LOCALIZED_LABEL).getAsString();
    String localizedDescription = oJson.get(JSON_LOCALIZED_DESCRIPTION).getAsString();
    GeometryType geometryType = GeometryType.valueOf(oJson.get(JSON_GEOMETRY_TYPE).getAsString());
    Boolean isLeaf = Boolean.valueOf(oJson.get(JSON_IS_LEAF).getAsString()); 
    
    Map<String, AttributeType> attributeMap = buildDefaultAttributes(registry);
    for (int i = 0; i < oJsonAttrs.size(); ++i)
    {
      JsonObject joAttr = oJsonAttrs.get(i).getAsJsonObject();
      String name = joAttr.get(AttributeType.JSON_NAME).getAsString();
      
      AttributeType attrType = AttributeType.factory(name, joAttr.get(AttributeType.JSON_LOCALIZED_LABEL).getAsString(), joAttr.get(AttributeType.JSON_LOCALIZED_DESCRIPTION).getAsString(), joAttr.get(AttributeType.JSON_TYPE).getAsString());
      attributeMap.put(name, attrType);
    }
    
    // TODO Need to validate that the default attributes are still defined.
    GeoObjectType geoObjType = new GeoObjectType(code, geometryType, localizedLabel, localizedDescription, isLeaf, attributeMap);
    
    return geoObjType;
  }
  
  /**
   * Return the JSON representation of this {@link GeoObjectType}.
   * 
   * @return JSON representation of this {@link GeoObjectType}.
   */
  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();
    
    json.addProperty(JSON_CODE, this.getCode());
    
    json.addProperty(JSON_LOCALIZED_LABEL, this.getLocalizedLabel());
    
    json.addProperty(JSON_LOCALIZED_DESCRIPTION, this.getLocalizedDescription());
    
    json.addProperty(JSON_GEOMETRY_TYPE, this.geometryType.name()); // TODO: PROPOSED but not yet approved. Required for fromJSON reconstruction.
    
    json.addProperty(JSON_IS_LEAF, this.isLeaf().toString()); 
    
    JsonArray attrs = new JsonArray();
    
    for (String key : this.attributeMap.keySet())
    {
      AttributeType attrType = this.attributeMap.get(key);
      
      attrs.add(attrType.toJSON());
    }
    
    json.add(JSON_ATTRIBUTES, attrs);
    
    return json;
  }
  
}
