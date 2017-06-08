package mahadevan.siva.gayathriapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siva on 2016-12-15.
 */

public class Recipe implements Parcelable {

    private int id;
    private String name;
    private String avatar;
    private String description;
    private String prepTime;

    public Recipe(int id, String name, String avatar, String description, String prepTime) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.description = description;
        this.prepTime = prepTime;
    }


    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        avatar = in.readString();
        description = in.readString();
        prepTime = in.readString();
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(description);
        dest.writeString(prepTime);
    }


    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


}
