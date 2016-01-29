package util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import models.manytomany.RecipeTags;

import java.io.IOException;
import java.util.List;

public class RecipeTagsSerializer extends JsonSerializer<List<RecipeTags>> {

    @Override
    public void serialize(List<RecipeTags> value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (RecipeTags tag : value) {
            jgen.writeObject(tag.tag);
        }
        jgen.writeEndArray();
    }
}
