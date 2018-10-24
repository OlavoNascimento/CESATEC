package cesatec.cesatec.deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.models.Authorization;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.models.Student;

/**
 * Deserializer used to transform JSON string into a Enrollment object
 */
public class EnrollmentDeserializer implements JsonDeserializer<Enrollment> {
    private final static String TAG = "EnrollmentDeserializer";

    @Override
    public Enrollment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        // Id of the enrollment
        final int id = jsonObject.get(ApiConstants.ENROLLMENTS_FIELD_ID).getAsInt();

        // Get the course associated with this enrollment
        final String course = jsonObject.get(ApiConstants.ENROLLMENTS_NESTED_COURSE_SUB_GROUP).
                getAsJsonObject().get(ApiConstants.COURSE_FIELD_NAME).getAsString();

        // Get the enrollment group, which can be null
        final JsonElement groupObject = jsonObject.get(ApiConstants.ENROLLMENTS_FIELD_STUDENT_GROUP);
        String group;
        if (groupObject.isJsonNull()) {
            // Use group default value if it's null
            group = ApiConstants.ENROLLMENTS_DEFAULT_STUDENT_GROUP;
        } else {
            // Use the JSON value if it's not null
            group = groupObject.getAsString();
        }

        // Deserialize the student information associated with this enrollment
        final String studentJSON = jsonObject.get(
                ApiConstants.ENROLLMENTS_NESTED_STUDENT).toString();
        final Student student = jsonToStudent(studentJSON);

        // Deserialize the student authorizations in an Authorization array
        final String authorizationJSON = jsonObject.get(
                ApiConstants.ENROLLMENTS_NESTED_AUTHORIZATIONS).toString();
        final Authorization[] authorizations = jsonToAuthorizations(authorizationJSON);

        // Returns a new Enrollment object based on the JSON data
        return new Enrollment(id, group, course, student, authorizations);
    }

    /**
     * Returns a Student object from a JSON string representation
     *
     * @param studentJSON JSON string to be converted to a Student object
     * @return Student class object
     */
    private Student jsonToStudent(String studentJSON) {
        // Set the student deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Student.class, new StudentDeserializer());
        Gson registrationsGSON = gsonBuilder.create();
        // Deserialize the student JSON into a Student object
        return registrationsGSON.fromJson(studentJSON, Student.class);
    }

    /**
     * Returns an Authorization object array from a JSON string representation
     *
     * @param authorizationJSON JSON string to be converted to a an Authorization array
     * @return Authorization object array
     */
    private Authorization[] jsonToAuthorizations(String authorizationJSON) {
        // Set the authorization deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Authorization.class, new AuthorizationDeserializer());
        Gson registrationsGSON = gsonBuilder.create();
        // Deserialize the authorization JSON into an Authorization object array
        return registrationsGSON.fromJson(authorizationJSON, Authorization[].class);
    }
}
