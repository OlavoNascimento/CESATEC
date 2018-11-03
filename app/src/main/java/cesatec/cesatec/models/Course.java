package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private static final String TAG = "Course";
    
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

    private final int id;
    private final String name;
    private final int firstSubCourseId;
    private final int secondSubCourseId;

    public Course(int id, String name, int firstSubCourseId, int secondSubCourseId) {
        this.id = id;
        this.name = name;
        this.firstSubCourseId = firstSubCourseId;
        this.secondSubCourseId = secondSubCourseId;
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
        this.firstSubCourseId = in.readInt();
        this.secondSubCourseId = in.readInt();
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
        out.writeInt(firstSubCourseId);
        out.writeInt(secondSubCourseId);
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

    public int getFirstSubCourseId() {
        return firstSubCourseId;
    }

    public int getSecondSubCourseId() {
        return secondSubCourseId;
    }

    @Override
    public String toString() {
        return name;
    }
}
