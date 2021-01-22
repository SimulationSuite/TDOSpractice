package org.tdos.tdospractice.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

public class JsonUtils {

    public static class BytesSerializer extends StdSerializer<byte[]> {

        private static final long serialVersionUID = -5510353102817291511L;

        public BytesSerializer() {
            super(byte[].class);
        }

        @Override
        public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(Hex.encodeHexString(value));
        }
    }

    public static class BytesDeserializer extends StdDeserializer<byte[]> {

        private static final long serialVersionUID = 1514703510863497028L;

        public BytesDeserializer() {
            super(byte[].class);
        }

        @Override
        public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            String encoded = node.asText();
            if (encoded == null || encoded.trim().isEmpty()) {
                return new byte[]{};
            }
            try {
                return Hex.decodeHex(encoded.toCharArray());
            } catch (Exception e) {
                throw new IOException("invalid hex encoding");
            }
        }
    }

    public byte[] encode(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            SimpleModule module = new SimpleModule();
            module.addSerializer(byte[].class, new BytesSerializer());
            mapper.registerModule(module);
            return mapper.writeValueAsBytes(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T decode(byte[] encoded, Class<T> valueType) {
        if (encoded == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(byte[].class, new BytesDeserializer());
            mapper.registerModule(module);
            mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
            return mapper.readValue(encoded, valueType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
