package view;

import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class UserView extends Layout {

    private JPanel container;
    private JPanel w_top;
    private JPanel w_bottom;
    private JTable tbl_user_list;
    private JButton btn_prev_menu;
    private JButton btn_new;
    private JButton btn_saveUpdate;
    private JButton btn_delete;
    private JTextField fld_user_pass;
    private JTextField fld_user_name;
    private JTextField fld_password;
    private JComboBox cmbx_user_perm;
    private JButton btn_edit;
    private JComboBox cmb_search_perm;
    private JButton btn_search;
    private JButton btn_list_all;

    private DefaultTableModel model;

    private UserManager userManager;

    private User user;

    private JPopupMenu jPopupMenu;

    public UserView(User user) {
        this.user = user;
        userManager = new UserManager();
        add(container);
        pageArt(500, 500, "User Management");


        userTableLoad();


        cmb_search_perm.addItem(User.Perm.ADMIN);
        cmb_search_perm.addItem(User.Perm.EMPLOYEE);


        tbl_user_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = tbl_user_list.rowAtPoint(e.getPoint());
                tbl_user_list.setRowSelectionInterval(selectRow, selectRow);
            }
        });


        btn_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UserEditView add = new UserEditView(new User());

                add.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        userTableLoad();
                    }
                });
            }
        });


        btn_prev_menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectId = (int) tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0);

                if (JOptionPane.showConfirmDialog(null, "Delete user?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {// Seciton 24-25 : The user is given appropriate pop up messages for successful transactions. Appropriate error messages are given to the user for incorrect operations.

                    userManager.delete(selectId);

                    userTableLoad();
                } else {
                    Helper.getMessage("Canceled", "Information");
                }
            }
        });


        btn_edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tbl_user_list.getSelectedRow() <= 0) {

                    Helper.getMessage("Please select a user", "Error");
                } else {

                    int selectId = (int) tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0);

                    UserEditView editView = new UserEditView(userManager.getById(selectId));

                    editView.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            userTableLoad();
                        }
                    });
                }
            }
        });


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                User.Perm selectedRole = (User.Perm) cmb_search_perm.getSelectedItem();
                if (selectedRole != null) {

                    loadUserFilter(selectedRole);
                } else {

                    userTableLoad();
                }
            }
        });


        btn_list_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTableLoad();
            }
        });
    }


    public void loadUserFilter(User.Perm roleFilter) {

        model = (DefaultTableModel) tbl_user_list.getModel();

        tableMouseSelect(tbl_user_list);

        Object[] col = {"ID", "Name", "PASSWORD", "PERM"};
        model.setColumnIdentifiers(col);
        model.setRowCount(0);

        for (User i : userManager.findByAll()) {
            if (i.getPerm().equals(roleFilter)) {
                Object[] list = {i.getUser_id(), i.getUser_name(), i.getUser_pass(), i.getPerm()};
                model.addRow(list);
            }
        }

        setTableProperties();
    }


    public void setTableProperties() {

        tbl_user_list.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbl_user_list.getColumnModel().getColumn(2).setPreferredWidth(150);
        tbl_user_list.getColumnModel().getColumn(3).setPreferredWidth(100);

        tbl_user_list.setEnabled(false);
        tbl_user_list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_user_list.getTableHeader().setResizingAllowed(false);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);
    }


    public void userTableLoad() {

        model = (DefaultTableModel) tbl_user_list.getModel();
        model.setRowCount(0);
        Object[] columns = {"ID", "Name", "Password", "Role"};
        model.setColumnIdentifiers(columns);

        for (User userList : userManager.findByAll()) {
            Object[] info = {userList.getUser_id(), userList.getUser_name(), userList.getUser_pass(), userList.getPerm()};
            model.addRow(info);
        }

        setTableProperties();
    }
}
