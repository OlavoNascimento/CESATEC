package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;

public class Authorization implements Parcelable {

    public static final Creator<Authorization> CREATOR = new Creator<Authorization>() {
        /**
         * Instantiate a Authorization from a parcelable
         * @param in Parcelable to be converted to Authorization
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

    private Date authorization_start;
    private Date authorization_end;
    private Time time_start;
    private Time time_end;
    private String weekday;
    private String authorization_type;

    public Authorization(Date authorization_start, Date authorization_end, Time time_start,
                         Time time_end, String weekday, String authorization_type) {
        this.authorization_start = authorization_start;
        this.authorization_end = authorization_end;
        this.time_start = time_start;
        this.time_end = time_end;
        this.weekday = weekday;
        this.authorization_type = authorization_type;
    }

    /**
     * Instantiate a Authorization object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable to be used on the creation the Authorization
     */
    private Authorization(Parcel in) {
        this.authorization_start = Date.valueOf(in.readString());
        this.authorization_end = Date.valueOf(in.readString());
        this.time_start = Time.valueOf(in.readString());
        this.time_end = Time.valueOf(in.readString());
        this.weekday = in.readString();
        this.authorization_type = in.readString();
    }

    /**
     * Create a Parcelable from an Authorization object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(String.valueOf(authorization_start));
        out.writeString(String.valueOf(authorization_end));
        out.writeString(String.valueOf(time_start));
        out.writeString(String.valueOf(time_end));
        out.writeString(weekday);
        out.writeString(authorization_type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Date getAuthorization_start() {
        return authorization_start;
    }

    public Date getAuthorization_end() {
        return authorization_end;
    }

    public Time getTime_start() {
        return time_start;
    }

    public Time getTime_end() {
        return time_end;
    }

    public String getweekday() {
        return weekday;
    }

    public String getAuthorization_type() {
        return authorization_type;
    }
}
