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
import cesatec.cesatec.models.SubCourse;

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
        final int id = jsonObject.get(ApiConstants.EnrollmentResource.FIELD_ID).getAsInt();

        // Get the enrollment group, which can be null
        final JsonElement groupElement = jsonObject.get(
                ApiConstants.EnrollmentResource.FIELD_STUDENT_GROUP);
        String group;
        if (groupElement.isJsonNull()) {
            // Use group default value if it's null
            group = ApiConstants.EnrollmentResource.DEFAULT_STUDENT_GROUP;
        } else {
            // Use the JSON value if it's not null
            group = groupElement.getAsString();
        }

        // Get the sub course of the student
        final String subCourseJson = jsonObject.get(ApiConstants.EnrollmentResource.NESTED_SUB_COURSE).
                getAsJsonObject().toString();
        final SubCourse subCourse = jsonToSubCourse(subCourseJson);

        // Deserialize the student information associated with this enrollment
        final String studentJSON = jsonObject.get(
                ApiConstants.EnrollmentResource.NESTED_STUDENT).toString();
        final Student student = jsonToStudent(studentJSON);

        // Deserialize the student authorizations into an Authorization object array
        final String authorizationJSON = jsonObject.get(
                ApiConstants.EnrollmentResource.NESTED_AUTHORIZATIONS).toString();
        final Authorization[] authorizations = jsonToAuthorizations(authorizationJSON);

        // Returns a new Enrollment object based on the JSON data
        return new Enrollment(id, group, subCourse, student, authorizations);
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
     * Returns a SubCourse object from a JSON string representation
     *
     * @param subCourseJSON JSON string to be converted to a SubCourse object
     * @return SubCourse class object
     */
    private SubCourse jsonToSubCourse(String subCourseJSON) {
        // Set the sub course deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SubCourse.class, new SubCourseDeserializer());
        Gson subCourseGSON = gsonBuilder.create();
        // Deserialize the student JSON into a SubCourse object
        return subCourseGSON.fromJson(subCourseJSON, SubCourse.class);
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
        Gson authorizationsGSON = gsonBuilder.create();
        // Deserialize the authorization JSON into an Authorization object array
        return authorizationsGSON.fromJson(authorizationJSON, Authorization[].class);
    }
}
