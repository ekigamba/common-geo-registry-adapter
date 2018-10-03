package org.commongeoregistry.adapter.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.*;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

public class GeoObject implements Serializable
{

  /**
   * 
   */
  private static final long      serialVersionUID = 7686140708200106783L;

  private GeoObjectType          geoObjectType;

  private GeometryType           geometryType;

  private Geometry               geometry;

  private Map<String, Attribute> attributeMap;

  public GeoObject(GeoObjectType _geoObjectType, GeometryType _geometryType,
      Map<String, Attribute> _attributeMap)
  {
    this.geoObjectType = _geoObjectType;

    this.geometryType = _geometryType;

    this.geometry = null;

    this.attributeMap = _attributeMap;
  }

  public GeoObjectType getTypeCode()
  {
    return this.geoObjectType;
  }

  public GeometryType getGeometryType()
  {
    return this.geometryType;
  }

  public Geometry getGeometry()
  {
    return this.geometry;
  }

  public void setGeometry(Geometry geometry)
  {
    this.geometry = geometry;
  }
  
  public void setWKTGeometry(String wkt)
  {
    Geometry wktObj = null;
    WKTReader wktReader = new WKTReader();
    try
    {
      wktObj = wktReader.read(wkt);
    }
    catch (ParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.setGeometry(wktObj);
  }

  public Object getValue(String attributeName)
  {
    return this.attributeMap.get(attributeName).getValue();
  }

  public Attribute getAttribute(String attributeName)
  {
    return this.attributeMap.get(attributeName);
  }

  /**
   * Return the JSON representation of this metadata
   * 
   * @return
   */
  public JsonObject toJSON()
  {
    JsonObject jsonObj = new JsonObject();

    // It's assumed that GeoObjects are simple features rather than
    // FeatureCollections.
    // Spec reference: https://tools.ietf.org/html/rfc7946#section-3.3
    jsonObj.addProperty("type", "Feature");

    GeoJSONWriter gw = new GeoJSONWriter();
    org.wololo.geojson.Geometry gJSON = gw.write(this.getGeometry());
    
    JsonParser parser = new JsonParser();
    JsonObject geomObj = parser.parse(gJSON.toString()).getAsJsonObject();
    
    jsonObj.add("geometry", geomObj);
    
    JsonObject attrs = new JsonObject();
    for (String key : this.attributeMap.keySet())
    {
      Attribute attr = this.attributeMap.get(key);
      
      if(attr instanceof AttributeTerm)
      {
        attrs.add(key, attr.toJSON());
      }
      else
      {
        
        System.out.println(attr.toJSON());
        
        // TODO: All these attributes are required by the CGR spec. Adding an
        // empty string is a temporary step for me to work on another area of 
        // the adapter. Ensure that Values are always present and handle 
        // NULLs as errors.
        if(attr.getValue() == null )
        {
          attrs.addProperty(key, ""); 
        }
        else
        {
          attrs.addProperty(key, attr.getValue().toString() );
        }
      }
      
    }

    jsonObj.add("properties", attrs);
    

    return jsonObj;
  }

  public void printAttributes()
  {
    for (Attribute attribute : attributeMap.values())
    {
      System.out.println(attribute.toString());
    }

    System.out.println("Geometry: " + this.geometry);
  }
}
