package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SubCourse implements Parcelable {
    private static final String TAG = "SubCourse";

    public static final Creator<SubCourse> CREATOR = new Creator<SubCourse>() {
        /**
         * Instantiate a SubCourse from a parcelable
         * @param in Parcelable used to create a SubCourse
         * @return SubCourse object
         */
        @Override
        public SubCourse createFromParcel(Parcel in) {
            return new SubCourse(in);
        }

        /**
         * Create a SubCourse array from a parcelable
         * @param size Size of the array
         * @return SubCourse object array
         */
        @Override
        public SubCourse[] newArray(int size) {
            return new SubCourse[size];
        }
    };

    private final int id;
    private final int parentCourseId;
    private final String name;
    private final ArrayList<Enrollment> enrollments;
    private final ArrayList<Authorization> authorizations;

    /**
     * Instantiate a SubCourse object using parameters
     *
     * @param parentCourseId   Id of the parent course
     * @param name Name of the sub course
     */
    public SubCourse(int id, int parentCourseId, String name,
                     ArrayList<Enrollment> enrollments,
                     ArrayList<Authorization> authorizations) {
        this.id = id;
        this.parentCourseId = parentCourseId;
        this.name = name;
        this.enrollments = enrollments;
        this.authorizations = authorizations;
    }

    /**
     * Instantiate a SubCourse object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable that contains the SubCourse
     *           object information
     */
    private SubCourse(Parcel in) {
        this.id = in.readInt();
        this.parentCourseId = in.readInt();
        this.name = in.readString();

        ArrayList<Enrollment> enrollments = new ArrayList<>();
        in.readList(enrollments, Enrollment.class.getClassLoader());
        this.enrollments = enrollments;

        ArrayList<Authorization> authorizations = new ArrayList<>();
        in.readList(authorizations, Authorization.class.getClassLoader());
        this.authorizations = authorizations;
    }

    /**
     * Create a Parcelable from a SubCourse object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(parentCourseId);
        out.writeString(name);
        out.writeList(enrollments);
        out.writeList(authorizations);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getParentCourseId() {
        return parentCourseId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Enrollment> getEnrollments() {
        return enrollments;
    }

    public ArrayList<Authorization> getAuthorizations() {
        return authorizations;
    }

    @Override
    public String toString() {
        return "SubCourse{" +
                "id=" + id +
                ", parentCourseId=" + parentCourseId +
                ", name='" + name + '\'' +
                ", enrollments=" + enrollments +
                ", authorizations=" + authorizations +
                '}';
    }
}
