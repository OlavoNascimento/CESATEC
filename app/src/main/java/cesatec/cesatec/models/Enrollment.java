package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Enrollment implements Parcelable {
    private static final String TAG = "Enrollment";

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

    private final int id;
    private final String group;
    private final SubCourse subCourse;
    private final Student student;
    private final Authorization[] authorizations;

    private boolean isSelected;

    /**
     * Instantiate a Student object using parameters
     *
     * @param id             Id of the enrollment
     * @param group          Group to which the enrollment belongs
     * @param subCourse      Sub course of the student
     * @param student        Student associated with the enrollment
     * @param authorizations All authorizations related to the enrollment
     */
    public Enrollment(int id, String group, SubCourse subCourse, Student student,
                      Authorization[] authorizations) {
        this.id = id;
        this.group = group;
        this.subCourse = subCourse;
        this.student = student;
        this.authorizations = authorizations;
        this.isSelected = false;
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
        this.subCourse = in.readParcelable(SubCourse.class.getClassLoader());
        this.student = in.readParcelable(Student.class.getClassLoader());
        this.authorizations = in.createTypedArray(Authorization.CREATOR);
        this.isSelected = in.readInt() == 1;
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
        out.writeParcelable(subCourse, 0);
        out.writeParcelable(student, 0);
        out.writeTypedArray(authorizations, 0);
        out.writeInt(isSelected ? 1 : 0);
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

    public SubCourse getSubCourse() {
        return subCourse;
    }

    public Student getStudent() {
        return student;
    }

    public Authorization[] getAuthorizations() {
        return authorizations;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", course='" + subCourse.toString() + '\'' +
                ", student=" + student +
                ", authorizations=" + Arrays.toString(authorizations) +
                ", isSelected=" + isSelected +
                '}';
    }
}
