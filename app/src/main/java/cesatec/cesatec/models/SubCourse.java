package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SubCourse implements Parcelable {
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
    private static final String TAG = "SubCourse";
    private final int parentCourseId;
    private final String name;

    /**
     * Instantiate a SubCourse object using parameters
     *
     * @param id   Id of the parent course
     * @param name Name of the sub course
     */
    public SubCourse(int id, String name) {
        this.parentCourseId = id;
        this.name = name;
    }

    /**
     * Instantiate a SubCourse object from a parcelable
     * Used to move the class between activities
     *
     * @param in Parcelable that contains the SubCourse
     *           object information
     */
    private SubCourse(Parcel in) {
        this.parentCourseId = in.readInt();
        this.name = in.readString();
    }

    /**
     * Create a Parcelable from a SubCourse object
     *
     * @param out Parcelable to write the information
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(parentCourseId);
        out.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getParentCourseId() {
        return parentCourseId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SubCourse{" +
                "parentCourseId=" + parentCourseId +
                ", name='" + name + '\'' +
                '}';
    }
}
