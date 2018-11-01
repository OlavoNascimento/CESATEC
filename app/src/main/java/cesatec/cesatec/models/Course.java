package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    public static final Creator<Course> CREATOR = new Creator<Course>() {
        /**
         * Instantiate a Course from a parcelable
         * @param in Parcelable used to create a a Course
         * @return Course object
         */
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        /**
         * Create a Course array from a parcelable
         * @param size Size of the array
         * @return Course object array
         */
        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
    private static final String TAG = "Course";
    private final int id;
    private final String name;

    /**
     * Instantiate a Course object using parameters
     *
     * @param id   Id of the parent course
     * @param name Name of the sub course
     */
    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Instantiate a Course object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable that contains the Course
     *           object information
     */
    private Course(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    /**
     * Create a Parcelable from a Course object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
