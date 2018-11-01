package cesatec.cesatec.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.models.SubCourse;

public class SubCourseDeserializer implements JsonDeserializer<SubCourse> {
    @Override
    public SubCourse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        // Get the id of the parent course
        final int id = jsonObject.get(
                ApiConstants.SubCoursesResource.FIELD_PARENT_COURSE_ID).getAsInt();

        // Get the name of the sub course
        final String name = jsonObject.get(
                ApiConstants.SubCoursesResource.FIELD_NAME).getAsString();

        // Returns a new SubCourse object based on the JSON data
        return new SubCourse(id, name);
    }
}
