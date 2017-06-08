package mahadevan.siva.gayathriapp.models;

/**
 * Created by siva on 2016-12-14.
 */

public class FoodGroup {

    private int id;
    private String name;
    private String avatar;

    public FoodGroup(int id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
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
}
