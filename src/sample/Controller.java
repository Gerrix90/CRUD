package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    static Connection conn;
    static Statement stmt;
    static String url;
    private int lastSelectedID;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField mejlTextField;

    @FXML
    private TableColumn<Person, String> username;

    @FXML
    private TableColumn<Person, String> password;

    @FXML
    private TableColumn<Person, String> mejl;

    @FXML
    private TableColumn<Person, CheckBox> checkBox;

    @FXML
    private TableColumn<Person, Integer> id;

    @FXML
    private TableView<Person> tableView;

    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();
    private int indexOfLastSelectedRow;
    private int maxID;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

//        usernameTextField = new TextField();
//        passwordTextField = new TextField();
//        mejlTextField = new TextField();

//        for (int i = 0; i <10 ; i++) {
//
//            CheckBox checkBox = new CheckBox("" + i);
//            Person person = new Person("Person " , i, checkBox);
//            personObservableList.add(person);
//        }
//
//        tableView.setItems(personObservableList);
//        name.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
//        id.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
//        checkBox.setCellValueFactory(new PropertyValueFactory<Person, CheckBox>("checkBox"));
    }

//    @FXML
//    private void deleteButtonAction(ActionEvent actionEvent){
//
//        for (int i = 0; i < tableView.getItems().size(); i++) {
//            if (tableView.getItems().get(i).getCheckBox().isSelected())
//                tableView.getItems().remove(tableView.getItems().get(i));
//        }
//
//    }

    public void deleteButtonAction(javafx.event.ActionEvent actionEvent) {

        for (int i = 0; i < tableView.getItems().size(); i++) {
            if (tableView.getItems().get(i).getCheckBox().isSelected()){

                String idZaBrisanje = String.valueOf(tableView.getItems().get(i).getId());
                System.out.println(idZaBrisanje);
                try {
                    stmt = conn.createStatement();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //stmt.executeQuery("insert into `korisnici`(`username`,`password`,`mejl`) values ('b','b','b')");
                try {
                    boolean dd = stmt.execute("DELETE FROM `korisnici` WHERE `id`='" + idZaBrisanje + "';");
                    if (dd == false) showDialog(" Nema podatka sa tim id-em!");
                    else {
                        showDialog("uspesno izbrisan podatak sa id=" + idZaBrisanje);
                        tableView.getItems().remove(tableView.getItems().get(i));

                    }
                } catch (SQLException e1) {
                    System.out.println(" nema takvog id-a");
                    e1.printStackTrace();
                }
            }
        }
    }

    public void connectButtonAction(ActionEvent actionEvent) {

        String driverName = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driverName);
//            obavestenjaLabel.setText("ucitan drajver!");
            showDialog("ucitan drajver!");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String serverName = "localhost";
        String mydatabase = "crud";
        String url = "jdbc:mysql://" + serverName + ":3306/" + mydatabase;
        String username = "root";
        String password = "";
        Statement stmt;
        try {
            conn = DriverManager.getConnection(url, username, password);
//            obavestenjaLabel.setText("uspesna konekcija na bazu");
            showDialog("uspesna konekcija na bazu");
        } catch (SQLException e) {
//            obavestenjaLabel.setText("greska pri konekciji na bazu");
            showDialog("greska pri konekciji na bazu");
            e.printStackTrace();
        }
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Database connection status");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void readButtonAction(ActionEvent actionEvent) {

        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT * FROM `korisnici`");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ArrayList<String> sb = new ArrayList<String>();

        try {
            while (rs.next()) {
                sb.add(rs.getString("id"));
                sb.add(rs.getString("username"));
                sb.add(rs.getString("password"));
                sb.add(rs.getString("mejl"));
                //System.out.println(rs.getString("password"));


//                    CheckBox checkBoxForPerson = new CheckBox(rs.getString("id"));
                CheckBox checkBoxForPerson = new CheckBox();
                Person person = new Person(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("mejl"),
                        Integer.valueOf(rs.getString("id")),
                        checkBoxForPerson);

                int tempID = Integer.valueOf(rs.getString("id"));
                if (maxID < tempID)
                    maxID = tempID;

                personObservableList.add(person);

                tableView.setItems(personObservableList);
                username.setCellValueFactory(new PropertyValueFactory<Person, String>("username"));

                password.setCellValueFactory(new PropertyValueFactory<Person, String>("password"));
                mejl.setCellValueFactory(new PropertyValueFactory<Person, String>("mejl"));
                id.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
                checkBox.setCellValueFactory(new PropertyValueFactory<Person, CheckBox>("checkBox"));

                tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {

                        System.out.println(newSelection.getUsername());

                        usernameTextField.setText(newSelection.getUsername());
                        passwordTextField.setText(newSelection.getPassword());
                        mejlTextField.setText(newSelection.getMejl());
                        lastSelectedID = newSelection.getId();
                        indexOfLastSelectedRow = tableView.getSelectionModel().getSelectedIndex();

                    }
                });


            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void updateButtonAction(ActionEvent actionEvent) {

        //obavezna konekcija!!!
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //stmt.executeQuery("insert into `korisnici`(`username`,`password`,`mejl`) values ('b','b','b')");
        try {
            stmt.execute("UPDATE `korisnici` SET `username`="+"'"+usernameTextField.getText()+"'"+
                    ",`password`="+"'"+passwordTextField.getText()+"'"+",`mejl`="+
                    "'"+mejlTextField.getText()+"'"+" WHERE id="+"'"+lastSelectedID+"'");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(indexOfLastSelectedRow);
//        tableView.getItems().get(indexOfLastSelectedRow).setUsername(usernameTextField.getText());
//        tableView.getItems().get(indexOfLastSelectedRow).setPassword(passwordTextField.getText());
//        tableView.getItems().get(indexOfLastSelectedRow).setMejl(mejlTextField.getText());


        tableView.getItems().add(new Person(usernameTextField.getText(), passwordTextField.getText(), mejlTextField.getText(), lastSelectedID, new CheckBox()));
        tableView.getItems().remove(tableView.getItems().get(indexOfLastSelectedRow));
        showDialog("uspesno izmenjen podaci sa id="+lastSelectedID);
    }

    public void createButtonAction(ActionEvent actionEvent) {
        try {
            //obavezna konekcija!!!
            stmt = conn.createStatement();
            //stmt.executeQuery("insert into `korisnici`(`username`,`password`,`mejl`) values ('b','b','b')");
            stmt.execute("INSERT INTO `korisnici`("+
                    "`username`,`password`,`mejl`) VALUES ('"+
                    usernameTextField.getText()+"','"+passwordTextField.getText()+"','"+
                    mejlTextField.getText()+"')");

            tableView.getItems().add(new Person(usernameTextField.getText(), passwordTextField.getText(), mejlTextField.getText(), maxID+1, new CheckBox()));

            showDialog("uspesno dodat podatak");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showDialog("neuspesno dodat podatak"+e.toString());
            e.printStackTrace();
        }
    }
}
