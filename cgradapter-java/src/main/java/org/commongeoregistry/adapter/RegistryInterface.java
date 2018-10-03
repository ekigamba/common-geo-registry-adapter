package org.commongeoregistry.adapter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.DefaultTerms;
import org.commongeoregistry.adapter.dataaccess.Attribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.MetadataCache;

public abstract class RegistryInterface implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5085432383838987882L;
  
  private MetadataCache metadataCache;
  
  public RegistryInterface()
  {
    this.metadataCache = new MetadataCache();
  }
  
  public MetadataCache getMetadataCache()
  {
    return this.metadataCache;
  }
  
  
  // TODO - Add support for a supplier provided exception.
  public GeoObject createGeoObject(String _geoObjectTypeCode, String geom)
  {
    GeoObjectType geoObjectType = this.getMetadataCache().getGeoObjectType(_geoObjectTypeCode).get();
    
    Map<String, AttributeType> attributeTypeMap = geoObjectType.getAttributeMap();
    
    Map<String, Attribute> attributeMap = new ConcurrentHashMap<String, Attribute>();
    
    for (AttributeType attributeType : attributeTypeMap.values())
    {
      Attribute attribute = Attribute.attributeFactory(attributeType);
      
      attributeMap.put(attribute.getName(), attribute);
    }
    
    GeoObject geoObject = new GeoObject(geoObjectType, geoObjectType.getGeometryType(), attributeMap, geom);
    
    // Set some default values
    geoObject.getAttribute(DefaultAttribute.TYPE.getName()).setValue(_geoObjectTypeCode);
    
    geoObject.getAttribute(DefaultAttribute.STATUS.getName()).setValue(DefaultTerms.GeoObjectStatusTerm.NEW.getTerm());
    
    return geoObject;
    
  }
}
