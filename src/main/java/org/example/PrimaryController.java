package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.entity.User;
import org.example.service.UserService;

public class PrimaryController {

    @FXML
    private ListView<String> userListView;

    private final UserService userService = new UserService();

    @FXML
    private void onLoadUsers() {
        userListView.getItems().clear();

        for (User user : userService.findAllUsers()) {
            userListView.getItems().add(user.getId() + " - " + user.getName());
        }
    }
}