package cesatec.cesatec.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
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
        final JsonObject jsonObject = json.getAsJsonObject();

        // Date when the authorization starts to be valid
        final String authorizationStart = jsonObject.get(
                ApiConstants.AUTHORIZATIONS_FIELD_DATE_START).getAsString();

        // Date when the authorization stops being valid
        final String authorizationEnd = jsonObject.get(
                ApiConstants.AUTHORIZATIONS_FIELD_DATE_END).getAsString();

        // Start of the time range when the authorization can be used by the student
        final String timeStart = jsonObject.get(
                ApiConstants.AUTHORIZATIONS_FIELD_TIME_START).getAsString();

        // End of the time range when the authorization can be used by the student
        final JsonElement timeEndElement = jsonObject.get(ApiConstants.AUTHORIZATIONS_FIELD_TIME_END);
        String timeEnd;
        if (timeEndElement.isJsonNull()) {
            timeEnd = null;
        } else {
            timeEnd = timeEndElement.getAsString();
        }

        // Day of the week when the authorization is valid
        final JsonElement weekdayElement = jsonObject.get(
                ApiConstants.AUTHORIZATIONS_FIELD_WEEKDAY);
        String weekday;
        if (weekdayElement.isJsonNull()) {
            weekday = null;
        } else {
            // Integer representing a day
            final int weekdayNumber = weekdayElement.getAsInt();
            // Get the weekday name by using it's integer representation
            weekday = new SimpleDateFormat("E", Locale.US).format(weekdayNumber);
        }

        // Responsible that created the authorization
        final String responsible = jsonObject.get(
                ApiConstants.AUTHORIZATIONS_FIELD_RESPONSIBLE).getAsString();

        // Returns a new Authorization object based on the JSON data
        return new Authorization(authorizationStart, authorizationEnd,
                timeStart, timeEnd, weekday, responsible);
    }
}
