package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Represents a Student, implements parcelable so it can be send through
 * activities and saved on bundles
 */
public class Student implements Parcelable {
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

    private short id;
    private String name;
    private String avatarUrl;

    /**
     * Instantiate a Student object using parameters
     *
     * @param id        Id of the student
     * @param name      Name of the student
     * @param avatarUrl Url of the student avatar
     */
    public Student(short id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    /**
     * Instantiate a Student object from a parcelable
     *
     * @param in Parcelable to be converted to a Student
     */
    public Student(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);

        this.id = Short.parseShort(data[0]);
        this.name = data[1];
        this.avatarUrl = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Instructions on how to convert a Student to a parcelable
     *
     * @param parcel Parcelable to be write the information to
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(String.valueOf(id));
        parcel.writeString(name);
        parcel.writeString(avatarUrl);
    }

    public short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    @NonNull
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatarUrl=" + avatarUrl +
                '}';
    }
}