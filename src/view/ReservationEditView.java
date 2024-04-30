package view;

import business.ReservationManager;
import business.RoomManager;
import business.SearchManager;
import core.Helper;
import entity.Hotel;
import entity.Reservation;
import entity.Room;
import entity.Season;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReservationEditView extends Layout {
    private JPanel container;
    private JTextField fld_fullname;
    private JTextField fld_id;
    private JTextField fld_mpno;
    private JTextField fld_email;
    private JTextField fld_check_in;
    private JTextField fld_chekc_out;
    private JTextField fld_child_count;
    private JTextField fld_adult_count;
    private JButton btn_search_save;
    private JButton btn_search_cancel;
    private JLabel lbl_fullname;
    private JLabel lbl_id;
    private JLabel lbl_mpno;
    private JLabel lbl_email;
    private JLabel lbl_check_in;
    private JLabel lbl_check_out;
    private JLabel lbl_child_count;
    private JLabel lbl_adult_count;
    private JLabel lbl_search_title;
    private JPanel w_bottom;
    private JPanel w_top;
    private JLabel lbl_total_price;
    private ReservationManager reservationManager;
    private Reservation reservation;
    private RoomManager roomManager;
    private SearchManager searchManager;
    private Room room;
    private Season season;
    private Hotel hotel;


    public ReservationEditView(Reservation reservation, Room room, Season season, Hotel hotel) {
        this.hotel = hotel;
        searchManager = new SearchManager();
        roomManager = new RoomManager();
        reservationManager = new ReservationManager();
        this.reservation = reservation;
        this.room = room;
        this.season = season;
        add(container);
        pageArt(640, 480, "Guest Information");

        fld_chekc_out.setText("yyyy-mm-dd");
        fld_check_in.setText("yyyy-mm-dd");


        if (reservation.getReservation_id() != 0) {
            loadReservation();
        }


        btn_search_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdateReservation();
            }
        });


        btn_search_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }


    public void loadReservation() {
        fld_chekc_out.setText(String.valueOf(reservation.getReservation_checkout_date()));
        fld_check_in.setText(String.valueOf(reservation.getReservation_checkin_date()));
        fld_adult_count.setText(String.valueOf(reservation.getReservation_adult_count()));
        fld_child_count.setText(String.valueOf(reservation.getReservation_child_count()));
        fld_email.setText(reservation.getReservation_guest_email());
        fld_fullname.setText(reservation.getReservation_guest_fullname());
        fld_id.setText(reservation.getReservation_guest_id());
        fld_mpno.setText(reservation.getReservation_guest_mpno());
        lbl_total_price.setText(String.valueOf(reservation.getReservation_totol_price() + "₺"));
    }


    private void saveOrUpdateReservation() {
        if (Helper.isFieldListEmpty(new JTextField[]{fld_mpno, fld_id, fld_fullname, fld_email, fld_check_in, fld_child_count, fld_chekc_out, fld_adult_count})) {
            Helper.getMessage("Not Null", "Error"); // Seciton 24-25 : The user is given appropriate pop up messages for successful transactions. Appropriate error messages are given to the user for incorrect operations.
        } else {
            if (reservation.getReservation_id() != 0) {
                updateReservation();
            } else {
                saveReservation();
            }
        }
    }


    private void updateReservation() {
        int stock = room.getRoom_stock_quantity();

        reservation.setReservation_guest_fullname(fld_fullname.getText());
        reservation.setReservation_guest_id(fld_id.getText());
        reservation.setReservation_guest_mpno(fld_mpno.getText());
        reservation.setReservation_guest_email(fld_email.getText());
        reservation.setReservation_checkin_date(LocalDate.parse(fld_check_in.getText()));
        reservation.setReservation_checkout_date(LocalDate.parse(fld_chekc_out.getText()));
        reservation.setReservation_child_count(Integer.parseInt(fld_child_count.getText()));
        reservation.setReservation_adult_count(Integer.parseInt(fld_adult_count.getText()));


        LocalDate in = reservation.getReservation_checkin_date();
        LocalDate out = reservation.getReservation_checkout_date();
        int adult = room.getRoom_adult_price() * reservation.getReservation_adult_count();
        int child = room.getRoom_child_price() * reservation.getReservation_child_count();
        long daysBetween = ChronoUnit.DAYS.between(in, out);
        double totalPrice = (adult + child) * daysBetween;


        LocalDate start = season.getSeason_start_date();
        LocalDate end = season.getSeason_end_date();

        int seasonStart = start.compareTo(in);
        int seasonEnd = end.compareTo(out);


        if (seasonStart > 0 || seasonEnd < 0) {
            Helper.getMessage("Exceed Season", "Information");
        } else {

            if (daysBetween < 0) {
                Helper.getMessage("Please enter correct date", "Information");
            } else if (daysBetween == 0) {

                if (stock > 0) {
                    room.setRoom_stock_quantity(stock);
                    daysBetween = 1;
                    totalPrice = (adult + child) * daysBetween;
                    reservation.setReservation_totol_price((int) totalPrice);
                    reservationManager.update(reservation);
                    Helper.getMessage("Saved", "Information");
                    lbl_total_price.setText(String.valueOf(totalPrice) + "₺");
                    btn_search_cancel.setText("Back");
                    roomManager.stockCheck(room);
                    roomManager.saveReservation(room);
                } else {

                    Helper.getMessage("Out of Room stock", "Information");
                }
            } else {

                if (stock > 0) {
                    room.setRoom_stock_quantity(stock);
                    reservation.setReservation_totol_price((int) totalPrice);
                    reservationManager.update(reservation);
                    Helper.getMessage("Saved", "Information");
                    btn_search_cancel.setText("Back");
                    lbl_total_price.setText(String.valueOf(totalPrice));
                    roomManager.stockCheck(room);
                } else {

                    Helper.getMessage("Out of Room stock", "Information");
                }
            }
        }
    }


    private void saveReservation() {
        int stock = room.getRoom_stock_quantity();
        String currentDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        reservation.setReservation_room_id(room.getRoom_id());
        reservation.setReservation_hotel_id(hotel.getHotel_id());
        reservation.setReservation_guest_fullname(fld_fullname.getText());
        reservation.setReservation_guest_id(fld_id.getText());
        reservation.setReservation_guest_mpno(fld_mpno.getText());
        reservation.setReservation_guest_email(fld_email.getText());
        reservation.setReservation_checkin_date(LocalDate.parse((fld_check_in.getText())));
        reservation.setReservation_checkout_date(LocalDate.parse(fld_chekc_out.getText()));
        reservation.setReservation_child_count(Integer.parseInt(fld_child_count.getText()));
        reservation.setReservation_adult_count(Integer.parseInt(fld_adult_count.getText()));
        reservation.setReservation_date(LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));


        LocalDate in = reservation.getReservation_checkin_date();
        LocalDate out = reservation.getReservation_checkout_date();
        int adult = room.getRoom_adult_price() * reservation.getReservation_adult_count();
        int child = room.getRoom_child_price() * reservation.getReservation_child_count();
        long daysBetween = ChronoUnit.DAYS.between(in, out);
        double totalPrice = (adult + child) * daysBetween;


        LocalDate start = season.getSeason_start_date();
        LocalDate end = season.getSeason_end_date();

        int seasonStart = start.compareTo(in);
        int seasonEnd = end.compareTo(out);


        if (seasonStart > 0 || seasonEnd < 0) {
            Helper.getMessage("Exceed Season", "Information");
        } else {

            if (daysBetween < 0) {
                Helper.getMessage("Please enter correct date", "Information");
            } else if (daysBetween == 0) {

                if (stock > 0) {
                    stock--;
                    room.setRoom_stock_quantity(stock);
                    daysBetween = 1;
                    totalPrice = (adult + child) * daysBetween;
                    reservation.setReservation_totol_price((int) totalPrice);
                    reservationManager.save(reservation);
                    Helper.getMessage("Saved", "Information");
                    lbl_total_price.setText(String.valueOf(totalPrice) + "₺");
                    btn_search_cancel.setText("Back");
                    roomManager.stockCheck(room);
                    roomManager.saveReservation(room);
                } else {

                    Helper.getMessage("Out of Room stock", "Information");
                }
            } else {

                if (stock > 0) {
                    stock--;
                    room.setRoom_stock_quantity(stock);
                    reservation.setReservation_totol_price((int) totalPrice);
                    reservationManager.save(reservation);
                    Helper.getMessage("Saved", "Information");
                    btn_search_cancel.setText("Back");
                    lbl_total_price.setText(String.valueOf(totalPrice));
                    roomManager.stockCheck(room);
                } else {

                    Helper.getMessage("Out of Room stock", "Information");
                }
            }
        }
    }
}
