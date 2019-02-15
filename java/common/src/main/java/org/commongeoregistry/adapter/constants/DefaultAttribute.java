/**
 * Copyright (c) 2019 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.constants;

import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

public enum DefaultAttribute 
{
  UID("uid",       "UID",       "The internal globally unique identifier ID", AttributeCharacterType.TYPE, true),

  CODE("code",     "Code",      "Human readable unique identified", AttributeCharacterType.TYPE, true),
  
  LOCALIZED_DISPLAY_LABEL("localizedDisplayLabel", "Localized Display Label",   "Localized locaiton ", AttributeCharacterType.TYPE, true),
  
  TYPE("type",     "Type",      "The type of the GeoObject", AttributeCharacterType.TYPE, true),
  
  STATUS("status", "Status",    "The status of the GeoObject", AttributeTermType.TYPE, true),
  
  SEQUENCE("sequence", "Sequence",    "The sequence number of the GeoObject that is incremented when the object is updated", AttributeIntegerType.TYPE, true),
 
  CREATE_DATE("createDate", "Date Created",    "The date the object was created", AttributeDateType.TYPE, true),
  
  LAST_UPDATE_DATE("lastUpdateDate", "Date Last Updated",    "The date the object was updated", AttributeDateType.TYPE, true);

  
  private String name;
  
  private String defaultLocalizedLabel;
  
  private String defaultLocalizedDescription;
  
  private String type;
  
  private boolean isDefault;

  private DefaultAttribute(String _name, String _defaultLocalizedLabel, String _defaultLocalizedDescription, String _type, boolean _isDefault)
  {
    this.name                        = _name;
    
    this.defaultLocalizedLabel       = _defaultLocalizedLabel;
    
    this.defaultLocalizedDescription = _defaultLocalizedDescription;
    
    this.type                        = _type;
    
    this.isDefault					 = _isDefault;
    
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDefaultLocalizedName()
  {
    return this.defaultLocalizedLabel;
  }
  
  public String getDefaultLocalizedDescription()
  {
    return this.defaultLocalizedDescription;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public boolean getIsDefault()
  {
	return this.isDefault;
  }
  
 public AttributeType createAttributeType()
 {
   return AttributeType.factory(this.getName(), this.getDefaultLocalizedName(), this.getDefaultLocalizedDescription(), this.getType());
 }
}
