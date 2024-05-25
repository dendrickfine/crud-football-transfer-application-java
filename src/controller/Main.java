package controller;

import model.User;
import view.LoginForm;

public class Main {
    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        loginForm.showForm();
//        User user = loginForm.user;
    }
}
