package cesatec.cesatec.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cesatec.cesatec.models.Student;

/**
 * Convert the response JSON to a Student object
 */
public class StudentDeserializer implements JsonDeserializer<Student> {
    private final static String TAG = "StudentDeserializer";

    @Override
    public Student deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObj = json.getAsJsonObject();
        final Short id = jsonObj.get("id").getAsShort();
        final String firstName = jsonObj.get("first_name").getAsString();
        final String lastName = jsonObj.get("last_name").getAsString();
        final String fullName = firstName + " " + lastName;
        final String avatarUrl = jsonObj.get("avatar").getAsString();
        return new Student(id, fullName, avatarUrl);
    }
}
