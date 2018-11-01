package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a Student, implements parcelable so it can be send through
 * activities and saved on bundles
 */
public class Student implements Parcelable {
    private static final String TAG = "Student";

    /**
     * Generates a Student object from a parcelable
     */
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        /**
         * Instantiate a Student from a parcelable
         * @param in Parcelable used to create a Student
         * @return Student object
         */
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        /**
         * Create a Student array from a parcelable
         * @param size Size of the array
         * @return Student object array
         */
        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    private final String name;
    private final int ra;
    private final String avatarUrl;

    /**
     * Instantiate a Student object using parameters
     *
     * @param name Name of the student
     * @param ra Student enrollment id
     * @param imageUrl Byte array of the student image
     */
    public Student(String name, int ra, String imageUrl) {
        this.name = name;
        this.ra = ra;
        this.avatarUrl = imageUrl;
    }

    /**
     * Instantiate a Student object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable that contains the Student
     *           object information
     */
    private Student(Parcel in) {
        this.name = in.readString();
        this.ra = in.readInt();
        this.avatarUrl = in.readString();
    }

    /**
     * Create a Parcelable from a Student object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeInt(ra);
        out.writeString(avatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getRa() {
        return ra;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", ra=" + ra +
                ", imageUrl='" + avatarUrl + '\'' +
                '}';
    }
}