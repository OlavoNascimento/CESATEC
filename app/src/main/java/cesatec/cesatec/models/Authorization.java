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

    private String authorizationDateStart;
    private String authorizationDateEnd;
    private String timeStart;
    private String timeEnd;
    private Integer weekday;
    private String responsible;

    public Authorization(String authorizationDateStart, String authorizationDateEnd, String timeStart,
                         String timeEnd, Integer weekday, String responsible) {
        this.authorizationDateStart = authorizationDateStart;
        this.authorizationDateEnd = authorizationDateEnd;
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
        this.authorizationDateStart = in.readString();
        this.authorizationDateEnd = in.readString();
        this.timeStart = in.readString();
        this.timeEnd = in.readString();
        this.weekday = in.readInt();
        this.responsible = in.readString();
    }

    /**
     * Create a Parcelable from an Authorization object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(authorizationDateStart);
        out.writeString(authorizationDateEnd);
        out.writeString(timeStart);
        out.writeString(timeEnd);
        out.writeInt(weekday);
        out.writeString(responsible);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthorizationDateStart() {
        return authorizationDateStart;
    }

    public String getAuthorizationDateEnd() {
        return authorizationDateEnd;
    }

    String getTimeStart() {
        return timeStart;
    }

    String getTimeEnd() {
        return timeEnd;
    }

    Integer getWeekday() {
        return weekday;
    }

    public String getResponsible() {
        return responsible;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "authorizationDateStart='" + authorizationDateStart + '\'' +
                ", authorizationDateEnd='" + authorizationDateEnd + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", weekday='" + weekday + '\'' +
                ", responsible='" + responsible + '\'' +
                '}';
    }
}
