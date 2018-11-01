package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Authorization implements Parcelable {
    public static final String TAG = "Authorization";

    public static final Creator<Authorization> CREATOR = new Creator<Authorization>() {
        /**
         * Instantiate a Authorization from a parcelable
         * @param in Parcelable used to create a Authorization
         * @return Authorization object
         */
        @Override
        public Authorization createFromParcel(Parcel in) {
            return new Authorization(in);
        }

        /**
         * Create a Authorization array from a parcelable
         * @param size Size of the array
         * @return Authorization object array
         */
        @Override
        public Authorization[] newArray(int size) {
            return new Authorization[size];
        }
    };

    private String authorizationStart;
    private String authorizationEnd;
    private String timeStart;
    private String timeEnd;
    private String weekday;
    private String responsible;

    public Authorization(String authorizationStart, String authorizationEnd, String timeStart,
                         String timeEnd, String weekday, String responsible) {
        this.authorizationStart = authorizationStart;
        this.authorizationEnd = authorizationEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.weekday = weekday;
        this.responsible = responsible;
    }

    /**
     * Instantiate a Authorization object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable that contains the Authorization
     *           object information
     */
    private Authorization(Parcel in) {
        this.authorizationStart = in.readString();
        this.authorizationEnd = in.readString();
        this.timeStart = in.readString();
        this.timeEnd = in.readString();
        this.weekday = in.readString();
        this.responsible = in.readString();
    }

    /**
     * Create a Parcelable from an Authorization object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(authorizationStart);
        out.writeString(authorizationEnd);
        out.writeString(timeStart);
        out.writeString(timeEnd);
        out.writeString(weekday);
        out.writeString(responsible);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthorizationStart() {
        return authorizationStart;
    }

    public String getAuthorizationEnd() {
        return authorizationEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getResponsible() {
        return responsible;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "authorizationStart='" + authorizationStart + '\'' +
                ", authorizationEnd='" + authorizationEnd + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", weekday='" + weekday + '\'' +
                ", responsible='" + responsible + '\'' +
                '}';
    }
}
