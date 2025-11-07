package com.daalitoy.apps.keedoh.data.fields.processor;

import com.daalitoy.apps.keedoh.data.common.FIELD_TYPE;
import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FieldProcessors {

  private static final Logger log = LogManager.getLogger(FieldProcessors.class);
  private static Map<FIELD_TYPE, FieldProcessor> fieldProcessorMap = Maps.newHashMap();

  static {
    fieldProcessorMap.put(FIELD_TYPE.FIXED, new FixedFieldProcessor());
    fieldProcessorMap.put(FIELD_TYPE.VARIABLE, new VariableFieldProcessor());
    fieldProcessorMap.put(FIELD_TYPE.BITMAPPED, new BitmappedFieldProcessor());
    fieldProcessorMap.put(FIELD_TYPE.TERMINATED, new TerminatedFieldProcessor());
  }

  public static FieldProcessor getProcessorClass(FIELD_TYPE fieldType) {
    return (fieldProcessorMap.get(fieldType));
  }
}
