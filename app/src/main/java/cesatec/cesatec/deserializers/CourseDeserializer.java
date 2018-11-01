package cesatec.cesatec.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.models.Course;

public class CourseDeserializer implements JsonDeserializer<Course> {
    @Override
    public Course deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        // Get the id of the course
        final int id = jsonObject.get(ApiConstants.CoursesResource.FIELD_ID).getAsInt();

        // Get the name of the course
        final String name = jsonObject.get(ApiConstants.CoursesResource.FIELD_NAME).getAsString();

        // Returns a new Course object based on the JSON data
        return new Course(id, name);
    }
}
