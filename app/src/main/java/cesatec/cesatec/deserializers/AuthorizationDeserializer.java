package cesatec.cesatec.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cesatec.cesatec.ApiConstants;
import cesatec.cesatec.models.Authorization;

/**
 * Deserializer used to transform a JSON string into a Authorization object
 */
public class AuthorizationDeserializer implements JsonDeserializer<Authorization> {
    private final static String TAG = "AuthorizationDeserializer";

    @Override
    public Authorization deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObj = json.getAsJsonObject();

        // Date when the authorization starts to be valid
        final Date authorization_start = Date.valueOf(jsonObj.get(
                ApiConstants.AUTHORIZATIONS_FIELD_DATE_START).getAsString());

        // Date when the authorization stops being valid
        final Date authorization_end = Date.valueOf(jsonObj.get(
                ApiConstants.AUTHORIZATIONS_FIELD_DATE_END).getAsString());

        // Time range when the authorization can be used by the student
        final Time time_start = Time.valueOf(jsonObj.get(
                ApiConstants.AUTHORIZATIONS_FIELD_TIME_START).getAsString());
        final Time time_end = Time.valueOf(jsonObj.get(
                ApiConstants.AUTHORIZATIONS_FIELD_TIME_END).getAsString());

        // Day of the week when the authorization can be used by the student
        final int weekdayNumber = jsonObj.get(
                ApiConstants.AUTHORIZATIONS_FIELD_WEEKDAY).getAsInt();
        // Get the weekday name by using it's integer representation
        final String weekday = new SimpleDateFormat("E", Locale.US).format(weekdayNumber);

        // Get the type of the authorization
        final String authorization_type = jsonObj.get(
                ApiConstants.AUTHORIZATIONS_FIELD_REGISTER_TYPE).getAsJsonObject()
                .get(ApiConstants.REGISTRY_FIELD_TYPE_FIELD_DESCRIPTION).getAsString();

        // Returns a new Authorization object based on the JSON data
        return new Authorization(authorization_start, authorization_end,
                time_start, time_end, weekday, authorization_type);
    }
}
