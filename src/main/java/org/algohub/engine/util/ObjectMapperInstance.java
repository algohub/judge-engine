package org.algohub.engine.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public final class ObjectMapperInstance {
  public static final ObjectMapper INSTANCE = new ObjectMapper();
  private ObjectMapperInstance() {}

  static {
    INSTANCE.registerModule(new Jdk8Module());
    INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }
}
