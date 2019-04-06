package sample;

import javafx.scene.control.CheckBox;

public class Person {

   private String username, password, mejl;
   private int id;
   private CheckBox checkBox;

    public Person(String username, String password, String mejl, int id, CheckBox checkBox) {
        this.username = username;
        this.password = password;
        this.mejl = mejl;
        this.id = id;
        this.checkBox = checkBox;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMejl() {
        return mejl;
    }

    public void setMejl(String mejl) {
        this.mejl = mejl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}