package Model;

public class User {
    int id;
    String username;
    String password;
    String countryISO;

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
