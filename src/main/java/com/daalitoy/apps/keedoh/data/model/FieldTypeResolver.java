package com.daalitoy.apps.keedoh.data.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

public class FieldTypeResolver extends TypeIdResolverBase {
  @Override
  public String idFromValue(Object o) {
    throw new RuntimeException("required for serialization only");
  }

  @Override
  public String idFromValueAndType(Object o, Class<?> aClass) {
    throw new RuntimeException("required for serialization only");
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) {
    switch (id) {
      case "FIXED":
        {
          return context.getTypeFactory().constructType(new TypeReference<FixedField>() {});
        }
      case "VARIABLE":
        {
          return context.getTypeFactory().constructType(new TypeReference<VariableField>() {});
        }
      case "TERMINATED":
        {
          return context.getTypeFactory().constructType(new TypeReference<TerminatedField>() {});
        }
      case "BITMAPPED":
        {
          return context.getTypeFactory().constructType(new TypeReference<BitmappedField>() {});
        }
      default:
        {
          throw new IllegalArgumentException(id + " not known");
        }
    }
  }

  @Override
  public JsonTypeInfo.Id getMechanism() {
    return JsonTypeInfo.Id.CUSTOM;
  }
}
