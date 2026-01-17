package Model.APIControl;

public class Platform {
    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String name;

    public Platform(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
