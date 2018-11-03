package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class Enrollment implements Parcelable {
    public static final Creator<Enrollment> CREATOR = new Creator<Enrollment>() {
        /**
         * Instantiate a Enrollment from a parcelable
         * @param in Parcelable used to create a Enrollment
         * @return Enrollment object
         */
        @Override
        public Enrollment createFromParcel(Parcel in) {
            return new Enrollment(in);
        }

        /**
         * Create a Enrollment array from a parcelable
         * @param size Size of the array
         * @return Enrollment object array
         */
        @Override
        public Enrollment[] newArray(int size) {
            return new Enrollment[size];
        }
    };
    private static final String TAG = "Enrollment";
    private final int id;
    private final String group;
    private final String subCourseName;
    private final Student student;
    private final ArrayList<Authorization> authorizations;
    private boolean allowedToLeave;

    private boolean isSelected;

    /**
     * Instantiate a Student object using parameters
     *
     * @param id             Id of the enrollment
     * @param group          Group to which the enrollment belongs
     * @param subCourseName  Sub course of the student
     * @param student        Student associated with the enrollment
     * @param authorizations All authorizations related to the enrollment
     */
    public Enrollment(int id, String group, String subCourseName, Student student,
                      ArrayList<Authorization> authorizations) {
        this.id = id;
        this.group = group;
        this.subCourseName = subCourseName;
        this.student = student;
        this.authorizations = authorizations;
        this.isSelected = false;
        this.allowedToLeave = checkAuthorizationList();
    }

    /**
     * Instantiate a Enrollment object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable that contains the Enrollment
     *           object information
     */
    private Enrollment(Parcel in) {
        this.id = in.readInt();
        this.group = in.readString();
        this.subCourseName = in.readString();
        this.student = in.readParcelable(Student.class.getClassLoader());

        ArrayList<Authorization> authorizations = new ArrayList<>();
        in.readList(authorizations, Authorization.class.getClassLoader());
        this.authorizations = authorizations;

        this.isSelected = in.readInt() == 1;
        this.allowedToLeave = in.readInt() == 1;
    }

    private boolean checkAuthorizationList() {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
                "HH:mm:ss");

        LocalTime timeNow = LocalTime.now(zoneId);
        LocalDate dateNow = LocalDate.now(zoneId);

        int weekDayNow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (authorizations != null) {
            for (Authorization authorization : authorizations) {

                Integer authorizationWeekDay = authorization.getWeekday();

                LocalDate authorizationDateStart = LocalDate.parse(
                        authorization.getAuthorizationDateStart(), dateFormatter);

                LocalDate authorizationDateEnd = LocalDate.parse(
                        authorization.getAuthorizationDateEnd(), dateFormatter);


                LocalTime authorizationTimeStart = LocalTime.parse(
                        authorization.getTimeStart(), timeFormatter);

                LocalTime authorizationTimeEnd;
                if (authorization.getTimeEnd() != null) {
                    authorizationTimeEnd = LocalTime.parse(
                            authorization.getTimeEnd(), timeFormatter);
                } else {
                    authorizationTimeEnd = null;
                }

                boolean authorizationDateValid = checkAuthorizationDate(
                        weekDayNow, dateNow, authorizationWeekDay,
                        authorizationDateStart, authorizationDateEnd);

                boolean authorizationTimeValid = checkAuthorizationTime(
                        timeNow, authorizationTimeStart, authorizationTimeEnd);

                if (authorizationDateValid && authorizationTimeValid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAuthorizationDate(int weekDayNow,
                                           @NonNull LocalDate dateNow,
                                           int authorizationWeekDay,
                                           @NonNull LocalDate authorizationDateStart,
                                           @NonNull LocalDate authorizationDateEnd) {
        if (authorizationWeekDay > -1) {
            if (weekDayNow != authorizationWeekDay) {
                return false;
            }
        }
        return (dateNow.isAfter(authorizationDateStart) || dateNow.equals(authorizationDateStart) &&
                (dateNow.isBefore(authorizationDateEnd) || dateNow.equals(authorizationDateEnd)));
    }


    private boolean checkAuthorizationTime(@NonNull LocalTime timeNow,
                                           @NonNull LocalTime authorizationStart,
                                           @Nullable LocalTime authorizationEnd) {
        if (timeNow.equals(authorizationStart) || timeNow.isAfter(authorizationStart)) {
            if (authorizationEnd == null) {
                return true;
            }
            return timeNow.equals(authorizationEnd) || timeNow.isBefore(authorizationEnd);
        }
        return false;
    }

    /**
     * Create a Parcelable from an Enrollment object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(group);
        out.writeString(subCourseName);
        out.writeParcelable(student, 0);
        out.writeList(authorizations);
        out.writeInt(isSelected ? 1 : 0);
        out.writeInt(allowedToLeave ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getSubCourseName() {
        return subCourseName;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAllowedToLeave() {
        return allowedToLeave;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", subCourseName='" + subCourseName + '\'' +
                ", student=" + student +
                ", authorizations=" + authorizations +
                ", allowedToLeave=" + allowedToLeave +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
