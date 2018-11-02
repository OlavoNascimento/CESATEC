package cesatec.cesatec.deserializers;

import com.google.gson.JsonArray;
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

        // The two sub courses of this course
        final JsonArray subCourses = jsonObject.get(
                ApiConstants.CoursesResource.NESTED_SUB_COURSES).getAsJsonArray();

        final int firstSubCourse = subCourses.get(1).getAsJsonObject().get(
                ApiConstants.SubCoursesResource.FIELD_ID).getAsInt();

        final int secondSubCourse = subCourses.get(2).getAsJsonObject().get(
                ApiConstants.SubCoursesResource.FIELD_ID).getAsInt();

        // Returns a new Course object based on the JSON data
        return new Course(id, name, firstSubCourse, secondSubCourse);
    }
}
