package cesatec.cesatec.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.models.Student;

/**
 * Deserializer used to transform JSON string into a Student object
 */
public class StudentDeserializer implements JsonDeserializer<Student> {
    private final static String TAG = "StudentDeserializer";

    @Override
    public Student deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        // Get the student name
        final String name = jsonObject.get(ApiConstants.STUDENTS_FIELD_NAME).getAsString();

        // Get the student register number
        final int ra = jsonObject.get(ApiConstants.STUDENTS_FIELD_ENROLL_ID).getAsInt();

        // Get the student avatar url
        final JsonElement imageElement = jsonObject.get(ApiConstants.STUDENTS_FIELD_IMAGE);
        String imageUrl;
        if (imageElement.isJsonNull()) {
            imageUrl = null;
        } else {
            // Assigns the student image as a byte array if it's not null
            imageUrl = imageElement.getAsString();
        }

        // Returns a new Student object based on the JSON data
        return new Student(name, ra, imageUrl);
    }
}
