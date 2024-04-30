package view;

import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserEditView extends Layout {


    private JPanel container;
    private JPanel w_top;
    private JPanel w_bottom;
    private JTextField fld_user_name;
    private JTextField fld_password;
    private JComboBox cmb_perm;
    private JButton btn_save;
    private JButton btn_clear;
    private JButton btn_cancel;

    private UserManager userManager;


    public UserEditView(User user) {
        userManager = new UserManager();
        add(container);
        pageArt(500, 500, "Add User");


        if (user.getUser_id() != 0) {
            fld_user_name.setText(user.getUser_name());
            fld_password.setText(user.getUser_pass());
            cmb_perm.addItem(user.getPerm());
        }


        for (User.Perm perm : User.Perm.values()) {
            cmb_perm.addItem(perm.toString());
        }


        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Helper.isFieldEmpty(fld_user_name) && Helper.isFieldEmpty(fld_password) && Helper.isFieldEmpty((JTextField) cmb_perm.getSelectedItem())) {

                    Helper.getMessage("Not Null", "Error");

                } else {

                    if (user.getUser_id() != 0) {

                        user.setUser_name(fld_user_name.getText());
                        user.setUser_pass(fld_password.getText());
                        user.setPerm(User.Perm.valueOf(cmb_perm.getSelectedItem().toString()));

                        if(!userManager.update(user)){
                            Helper.getMessage("User Already Exist","Information");
                        }else{

                            userManager.update(user);

                            Helper.getMessage("Update a User", "Information");


                            btn_cancel.setText("Back");
                        }
                    } else {


                        user.setUser_name(fld_user_name.getText());
                        user.setUser_pass(fld_password.getText());
                        user.setPerm(User.Perm.valueOf(cmb_perm.getSelectedItem().toString()));

                        if(!userManager.save(user)){
                            Helper.getMessage("User Already Exist","Information");
                        }else{

                            userManager.save(user);

                            Helper.getMessage("Saved", "Information");// Seciton 24-25 : The user is given appropriate pop up messages for successful transactions. Appropriate error messages are given to the user for incorrect operations.


                            btn_cancel.setText("Back");
                        }

                    }
                }
            }
        });


        btn_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fld_user_name.setText("");
                fld_password.setText("");
                cmb_perm.setSelectedItem(null);
            }
        });
    }
}
