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
         * @param in Parcelable to be converted to Student
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

    private String name;
    private int ra;
    private byte[] image;

    /**
     * Instantiate a Student object using parameters
     *
     * @param name Name of the student
     * @param ra Student enrollment id
     * @param image Byte array of the student image
     */
    public Student(String name, int ra, byte[] image) {
        this.name = name;
        this.ra = ra;
        this.image = image;
    }

    /**
     * Instantiate a Student object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable to be used on the creation the Student
     */
    private Student(Parcel in) {
        this.name = in.readString();
        this.ra = in.readInt();

        // Recreates the user image represent by a byte array
        int imageLength = in.readInt();
        this.image = new byte[imageLength];
        in.readByteArray(image);
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

        if (image != null) {
            // Write the student image length
            out.writeInt(image.length);
            // Write the student image represented using a byte array
            out.writeByteArray(image);
        }
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

    public byte[] getImage() {
        return image;
    }
}