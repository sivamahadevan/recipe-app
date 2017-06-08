package mahadevan.siva.gayathriapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siva on 2016-12-14.
 */

public class Ingredient implements Parcelable {

    private int id;
    private String name;
    private String barcode;
    private int foodGroupId;
    private String avatar;

    public Ingredient(int id, String name, String barcode, int foodGroupId, String avatar) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.foodGroupId = foodGroupId;
        this.avatar = avatar;
    }

    private Ingredient(Parcel in) {
        id = in.readInt();
        name = in.readString();
        barcode = in.readString();
        foodGroupId = in.readInt();
        avatar = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getFoodGroupId() {
        return foodGroupId;
    }

    public void setFoodGroupId(int foodGroupId) {
        this.foodGroupId = foodGroupId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(barcode);
        dest.writeInt(foodGroupId);
        dest.writeString(avatar);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
