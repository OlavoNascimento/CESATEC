package cesatec.cesatec.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RegistryTypes implements Parcelable {
    public static final Creator<RegistryTypes> CREATOR = new Creator<RegistryTypes>() {
        /**
         * Instantiate a RegistryTypes from a parcelable
         * @param in Parcelable used to create a RegistryTypes
         * @return RegistryTypes object
         */
        @Override
        public RegistryTypes createFromParcel(Parcel in) {
            return new RegistryTypes(in);
        }

        /**
         * Create a RegistryTypes array from a parcelable
         * @param size Size of the array
         * @return RegistryTypes object array
         */
        @Override
        public RegistryTypes[] newArray(int size) {
            return new RegistryTypes[size];
        }
    };
    private static final String TAG = "RegistryTypes";
    // Id that represents the type of an exit registry
    private final int fieldTypeExitId;

    // Id that represents the type of an return registry
    private final int fieldTypeReturnId;

    public RegistryTypes(int fieldTypeExitId, int fieldTypeReturnId) {
        this.fieldTypeExitId = fieldTypeExitId;
        this.fieldTypeReturnId = fieldTypeReturnId;
    }

    private RegistryTypes(Parcel in) {
        this.fieldTypeExitId = in.readInt();
        this.fieldTypeReturnId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(fieldTypeExitId);
        out.writeInt(fieldTypeReturnId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getFieldTypeExitId() {
        return fieldTypeExitId;
    }

    public int getFieldTypeReturnId() {
        return fieldTypeReturnId;
    }
}
