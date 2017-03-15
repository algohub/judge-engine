package org.algohub.engine.type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TypeNodeSerializer extends StdSerializer<TypeNode> {
    public TypeNodeSerializer() {
        this(null);
    }

    public TypeNodeSerializer(Class<TypeNode> t) {
        super(t);
    }

    @Override
    public void serialize(
            TypeNode value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString(value.toString());
    }
}
