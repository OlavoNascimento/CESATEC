package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Enrollment implements Parcelable {
    private static final String TAG = "Enrollment";

    public static final Creator<Enrollment> CREATOR = new Creator<Enrollment>() {
        /**
         * Instantiate a Enrollment from a parcelable
         * @param in Parcelable to be converted to Enrollment
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

    private final int id;
    private final String group;
    private final String course;
    private final Student student;
    private final Authorization[] authorizations;

    /**
     * Instantiate a Student object using parameters
     *
     * @param id             Id of the enrollment
     * @param group          Group to which the enrollment belongs
     * @param course         Course of the student
     * @param student        Student associated with the enrollment
     * @param authorizations All authorizations related to the enrollment
     */
    public Enrollment(int id, String group, String course, Student student,
                      Authorization[] authorizations) {
        this.id = id;
        this.group = group;
        this.course = course;
        this.student = student;
        this.authorizations = authorizations;
    }

    /**
     * Instantiate a Enrollment object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable to be used on the creation the Enrollment
     */
    private Enrollment(Parcel in) {
        this.id = in.readInt();
        this.group = in.readString();
        this.course = in.readString();
        this.student = in.readParcelable(Student.class.getClassLoader());
        this.authorizations = in.createTypedArray(Authorization.CREATOR);
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
        out.writeString(course);
        out.writeParcelable(student, 0);
        out.writeTypedArray(authorizations, 0);
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

    public String getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }

    public Authorization[] getAuthorizations() {
        return authorizations;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", course='" + course + '\'' +
                ", student=" + student +
                ", authorizations=" + Arrays.toString(authorizations) +
                '}';
    }
}
