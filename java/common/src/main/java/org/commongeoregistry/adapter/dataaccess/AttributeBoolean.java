package org.commongeoregistry.adapter.dataaccess;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AttributeBoolean extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = -3802068636170892383L;
  
  private Boolean            value;

  public AttributeBoolean(String name)
  {
    super(name, AttributeBooleanType.TYPE);

    this.value = null;
  }

  @Override
  public void setValue(Object value)
  {
    this.setBoolean((Boolean) value);
  }

  public void setBoolean(Boolean value)
  {
    this.value = value;
  }

  @Override
  public Boolean getValue()
  {
    return this.value;
  }

  public JsonObject toJSON()
  {
    JsonObject obj = new JsonObject();
    obj.addProperty(this.getName(), this.getValue().toString());

    return obj;
  }

  @Override
  public void fromJSON(JsonElement jValue, RegistryAdapter registry)
  {
    this.setValue(Boolean.valueOf(jValue.getAsString()));
  }
  
}
