package view;

import business.UserManager;
import core.Helper;
import dao.UserDao;
import entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends Layout {
    private JPanel container;
    private JPanel panel_top;
    private JPanel panel_bottom;
    private JLabel lbl_page_title;
    private JLabel lbl_username;
    private JTextField fld_username;
    private JButton btn_login;
    private JButton btn_exit;
    private JPasswordField fld_password;
    private JFormattedTextField fld_password3;
    private UserDao userDao;
    private UserManager userManager;


    public LoginView() {
        userDao = new UserDao();
        userManager = new UserManager();
        add(container);
        pageArt(500, 500, "Tourism Agency Management");
        setTheme("Nimbus");


        btn_login.addActionListener(e -> {


            if (Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_password)) {

                Helper.getMessage("Fields are not empty!", "Error");
            } else {

                User user = userManager.login(fld_username.getText(), fld_password.getText());
                if (user == null) {

                    Helper.getMessage("Username or password is wrong!", "Error");
                } else {

                    MenuView menuView = new MenuView(user);
                    dispose();
                }
            }
        });


        btn_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
