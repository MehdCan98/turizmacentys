package view;

import business.UserManager;
import entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuView extends Layout {
    private JPanel container;
    private JLabel lbl_title;
    private JButton btn_user_management;
    private JButton btn_hotel_management;
    private JButton btn_reservations;
    private JButton btn_logout;
    private UserManager userManager;


    public MenuView(User user) {
        userManager = new UserManager();
        add(container);
        pageArt(500, 500, "Menu");


        if (!user.getPerm().equals(User.Perm.ADMIN)) {
            btn_user_management.setVisible(false);
        }


        btn_user_management.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UserView userView = new UserView(new User());
                dispose();
                userView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });


        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LoginView loginView = new LoginView();
                dispose();
            }
        });


        btn_hotel_management.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectId = user.getUser_id();
                HotelView hotelView = new HotelView(userManager.getById(selectId));
                dispose();
                hotelView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });


        btn_reservations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ReservationView reservationView = new ReservationView(user);
                dispose();
                reservationView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });
    }
}
