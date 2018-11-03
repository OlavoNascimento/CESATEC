package cesatec.cesatec.deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cesatec.cesatec.constants.ApiConstants;
import cesatec.cesatec.models.Authorization;
import cesatec.cesatec.models.Enrollment;
import cesatec.cesatec.models.SubCourse;

public class SubCourseDeserializer implements JsonDeserializer<SubCourse> {
    private final static String TAG = "SubCourseDeserializer";

    @Override
    public SubCourse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        // Get the id of the parent course
        final int id = jsonObject.get(
                ApiConstants.SubCoursesResource.FIELD_ID).getAsInt();

        // Get the id of the parent course
        final int parentId = jsonObject.get(
                ApiConstants.SubCoursesResource.FIELD_PARENT_COURSE_ID).getAsInt();

        // Get the name of the sub course
        final String subCourseName = jsonObject.get(
                ApiConstants.SubCoursesResource.FIELD_NAME).getAsString();

        // Get the enrollments of the sub course
        final String enrollmentsJson = jsonObject.get(
                ApiConstants.SubCoursesResource.NESTED_ENROLLMENTS).toString();
        final ArrayList<Enrollment> enrollments = jsonToEnrollments(enrollmentsJson, subCourseName);

        // Deserialize the student authorizations into an Authorization object array
        final String authorizationJSON = jsonObject.get(
                ApiConstants.EnrollmentResource.NESTED_AUTHORIZATIONS).toString();
        final ArrayList<Authorization> authorizations = jsonToAuthorizations(authorizationJSON);

        // Returns a new SubCourse object based on the JSON data
        return new SubCourse(id, parentId, subCourseName, enrollments, authorizations);
    }

    private ArrayList<Enrollment> jsonToEnrollments(String enrollmentJSON,
                                                    String subCourseName) {
        // Type token of an ArrayList of SubCourse
        Type typeToken = new TypeToken<ArrayList<Enrollment>>() {
        }.getType();
        // Set the enrollment deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Enrollment.class, new EnrollmentDeserializer(subCourseName));
        Gson enrollmentsGSON = gsonBuilder.create();
        // Deserialize the enrollment JSON into an Enrollment object array
        return enrollmentsGSON.fromJson(enrollmentJSON, typeToken);
    }

    private ArrayList<Authorization> jsonToAuthorizations(String authorizationJSON) {
        // Type token of an ArrayList of SubCourse
        Type typeToken = new TypeToken<ArrayList<Authorization>>() {
        }.getType();
        // Set the authorization deserializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(
                Authorization.class, new AuthorizationDeserializer());
        Gson authorizationsGSON = gsonBuilder.create();
        // Deserialize the authorization JSON into an Authorization object array
        return authorizationsGSON.fromJson(authorizationJSON, typeToken);
    }
}
