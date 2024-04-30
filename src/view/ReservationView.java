package view;

import business.*;
import core.ComboItem;
import core.Helper;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class ReservationView extends Layout {
    // Components
    private JPanel container;
    private JTabbedPane tabbedPane1;
    private JTable tbl_search_list;
    private JButton btn_search_create_reservation;
    private JButton btn_search_back;
    private JTable tbl_reservation_list;
    private JButton btn_reservation_list_edit;
    private JButton btn_reservation_list_delete;
    private JButton btn_reservation_list_back;
    private JComboBox cmb_search_city;
    private JLabel lbl_search_city;
    private JLabel lbl_search_hotel_name;
    private JComboBox cmb_search_hotel_name;
    private JLabel lbl_search_check_in;
    private JButton btn_search;
    private JButton btn_list_all;
    private JComboBox cmb_in_year;
    private JComboBox cmb_in_month;
    private JComboBox cmb_in_day;
    private JComboBox cmb_out_year;
    private JComboBox cmb_out_month;
    private JComboBox cmb_out_day;
    private DefaultTableModel model;
    private ReservationManager reservationManager;
    private Reservation reservation;
    private Object[] info;
    // Dates
    String checkInDate;
    String checkOutDate;
    // Managers
    private HotelManager hotelManager;
    private RoomManager roomManager;
    private SearchManager searchManager;
    private SeasonManager seasonManager;
    private User user;


    public ReservationView(User user) {

        this.user = user;
        seasonManager = new SeasonManager();
        roomManager = new RoomManager();
        searchManager = new SearchManager();
        reservationManager = new ReservationManager();
        hotelManager = new HotelManager();


        add(container);
        pageArt(1500, 800, "Reservations");


        searchTableLoad();
        reservationTableLoad();
        filterCity();
        filterForCity();
        filterHotel();
        filterForHotel();
        addDate();


        tbl_search_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = tbl_search_list.rowAtPoint(e.getPoint());
                tbl_search_list.setRowSelectionInterval(selectRow, selectRow);
            }
        });



        btn_search_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MenuView menuView = new MenuView(user);
            }
        });


        btn_search_create_reservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tbl_search_list.getSelectedRow() < 0) {
                    Helper.getMessage("Please select a Hotel", "Error");
                } else {
                    int selectRoomId = (int) tbl_search_list.getValueAt(tbl_search_list.getSelectedRow(), 1);
                    int selectSeasonId = (int) tbl_search_list.getValueAt(tbl_search_list.getSelectedRow(), 0);
                    int selectHotelId = (int) tbl_search_list.getValueAt(tbl_search_list.getSelectedRow(), 2);
                    ReservationEditView reservationEditView = new ReservationEditView(new Reservation(), roomManager.getById(selectRoomId), seasonManager.getById(selectSeasonId), hotelManager.getById(selectHotelId));
                    reservationEditView.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            searchTableLoad();
                            reservationTableLoad();

                            for (Reservation reservation : reservationManager.findByAll()){
                                for (Room room : roomManager.findByAll()){
                                    if(reservation.getReservation_room_id() == room.getRoom_id()){
                                        room.setRoom_reservation_id(reservation.getReservation_id());
                                        roomManager.saveReservation(room);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        btn_reservation_list_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MenuView menuView = new MenuView(user);
            }
        });


        btn_reservation_list_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectId = (int) tbl_reservation_list.getValueAt(tbl_reservation_list.getSelectedRow(), 0);
                int selectRoom = (int) tbl_reservation_list.getValueAt(tbl_reservation_list.getSelectedRow(), 2);
                int stock = roomManager.getById(selectRoom).getRoom_stock_quantity();
                stock++;

                for (Room room : roomManager.findByAll()) {
                    if (selectRoom == room.getRoom_id()) {
                        room.setRoom_stock_quantity(stock);
                        roomManager.stockCheck(room);
                    }
                }
                reservationManager.delete(selectId);
                reservationTableLoad();
                searchTableLoad();
            }
        });


        btn_reservation_list_edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectReservationId = (int) tbl_reservation_list.getValueAt(tbl_reservation_list.getSelectedRow(), 0);
                int selectRoomId = (int) tbl_reservation_list.getValueAt(tbl_reservation_list.getSelectedRow(), 2);
                ReservationEditView reservationEditView = new ReservationEditView(
                        reservationManager.getById(selectReservationId),
                        roomManager.getById(selectRoomId),
                        seasonManager.getById(roomManager.getById(selectRoomId).getRoom_seasson_id()),
                        null);
                reservationEditView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        searchTableLoad();
                        reservationTableLoad();
                    }
                });
            }
        });
    }


    public void searchTableLoad() {

        model = (DefaultTableModel) tbl_search_list.getModel();
        model.setRowCount(0);


        Object[] columns = {"Season ID", "Room ID", "Hotel ID", "Hotel Name", "City", "Season Start Date", "Season End Date", "Pansion Type", "Stock", "Adult Price", "Child Price"};
        model.setColumnIdentifiers(columns);


        for (Room roomList : roomManager.findByAll()) {
            info = new Object[]{roomList.getRoom_seasson_id(),roomList.getRoom_id(),roomList.getRoom_hotel_id(),roomList.getRoom_hotel_name(),roomList.getRoom_hotel_city(),
            roomList.getRoom_season_start(),roomList.getRoom_season_end(),roomList.getRoom_pansion_type(),roomList.getRoom_stock_quantity(),roomList.getRoom_adult_price(),roomList.getRoom_child_price()};
            int stock = (int) info[8];
            if (stock != 0) {
                model.addRow(info);
            }
        }


        tbl_search_list.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbl_search_list.getColumnModel().getColumn(1).setPreferredWidth(180);
        tbl_search_list.getColumnModel().getColumn(2).setPreferredWidth(74);
        tbl_search_list.getColumnModel().getColumn(3).setPreferredWidth(74);
        tbl_search_list.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbl_search_list.getColumnModel().getColumn(5).setPreferredWidth(150);
        tbl_search_list.getColumnModel().getColumn(6).setPreferredWidth(90);
        tbl_search_list.getColumnModel().getColumn(7).setPreferredWidth(50);
    }


    public void reservationTableLoad() {
        model = (DefaultTableModel) tbl_reservation_list.getModel();
        model.setRowCount(0); // Clear table


        Object[] columns = {"Reservation ID", "Hotel Name", "Room ID", "Full Name", "ID No", "Phone Number", "Email", "Checkin Date", "Checkout Date", "Children", "Adults", "Total Price", "Reservation Date"};
        model.setColumnIdentifiers(columns);


        for (Reservation reservation : reservationManager.findByAll()) {
            info = new Object[]{reservation.getReservation_id(), hotelManager.getById(reservation.getReservation_hotel_id()).getHotel_name(), reservation.getReservation_room_id(), reservation.getReservation_guest_fullname(), reservation.getReservation_guest_id(),
                    reservation.getReservation_guest_mpno(), reservation.getReservation_guest_email(), reservation.getReservation_checkin_date(), reservation.getReservation_checkout_date(), reservation.getReservation_child_count(), reservation.getReservation_adult_count(),
                    reservation.getReservation_totol_price(), reservation.getReservation_date()};
            model.addRow(info);
        }


        tbl_search_list.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbl_search_list.getColumnModel().getColumn(1).setPreferredWidth(180);
        tbl_search_list.getColumnModel().getColumn(2).setPreferredWidth(74);
        tbl_search_list.getColumnModel().getColumn(3).setPreferredWidth(74);
        tbl_search_list.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbl_search_list.getColumnModel().getColumn(5).setPreferredWidth(150);
        tbl_search_list.getColumnModel().getColumn(6).setPreferredWidth(90);
        tbl_search_list.getColumnModel().getColumn(7).setPreferredWidth(50);
        tbl_search_list.getColumnModel().getColumn(8).setPreferredWidth(50);
    }


    public void filterHotel() {
        searchTableLoad();


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);

                ComboItem selectItem = (ComboItem) cmb_search_hotel_name.getSelectedItem();
                int hotelId = 0;
                if (selectItem != null) {
                    hotelId = selectItem.getKey();
                }
                int selectedInYear = cmb_in_year.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_in_year.getSelectedItem())) : 0;
                int selectedOutYear = cmb_out_year.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_out_year.getSelectedItem())) : 0;
                int selectedInMonth = cmb_in_month.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_in_month.getSelectedItem())) : 0;
                int selectedOutMonth = cmb_out_month.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_out_month.getSelectedItem())) : 0;

                int selectedInDay = cmb_in_day.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_in_day.getSelectedItem())) : 0;
                int selectedOutDay = cmb_out_day.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_out_day.getSelectedItem())) : 0;

                String selectedValueForInMonth = (selectedInMonth < 10) ? "0" + selectedInMonth : String.valueOf(selectedInMonth);
                String selectedValueForOutMonth = (selectedOutMonth < 10) ? "0" + selectedOutMonth : String.valueOf(selectedOutMonth);

                String selectedValueForInDay = (selectedInDay < 10) ? "0" + selectedInDay : String.valueOf(selectedInDay);
                String selectedValueForOutDay = (selectedOutDay < 10) ? "0" + selectedOutDay : String.valueOf(selectedOutDay);

                String selectedValueForCity = (cmb_search_city.getSelectedItem() != null) ? cmb_search_city.getSelectedItem().toString() : "NULL";
                String selectedValueForHotelName = (cmb_search_hotel_name.getSelectedItem() != null) ? cmb_search_hotel_name.getSelectedItem().toString() : "NULL";

                String selectedValueForInDateYear = (selectedInYear != 0) ? cmb_in_year.getSelectedItem().toString() : "0";
                String selectedValueForOutDateYear = (selectedOutYear != 0) ? cmb_out_year.getSelectedItem().toString() : "0";

                checkInDate = selectedValueForInDateYear + "-" + selectedValueForInMonth + "-" + selectedValueForInDay;
                checkOutDate = selectedValueForOutDateYear + "-" + selectedValueForOutMonth + "-" + selectedValueForOutDay;


                ArrayList<Room> hotelTableList = roomManager.filterTable(selectedValueForCity, selectedValueForHotelName,
                        selectedValueForInDateYear, selectedValueForInMonth, selectedValueForInDay,
                        selectedValueForOutDateYear, selectedValueForOutMonth, selectedValueForOutDay);
                for (Room roomList : hotelTableList) {
                    Object[] info = {roomList.getRoom_seasson_id(),roomList.getRoom_id(),roomList.getRoom_hotel_id(),roomList.getRoom_hotel_name(),roomList.getRoom_hotel_city(),
                            roomList.getRoom_season_start(),roomList.getRoom_season_end(),roomList.getRoom_pansion_type(),roomList.getRoom_stock_quantity(),roomList.getRoom_adult_price(),roomList.getRoom_child_price()};
                    model.addRow(info);
                }
            }
        });


        btn_list_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTableLoad(); // Reload search table
            }
        });

        tabbedPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                searchTableLoad();
            }
        });
    }


    public void filterForHotel() {
        cmb_search_hotel_name.removeAllItems();
        cmb_search_hotel_name.addItem(null);
        for (Hotel hotel : hotelManager.findByAll()) {
            cmb_search_hotel_name.addItem(new ComboItem(hotel.getHotel_id(), hotel.getHotel_name()));
        }
    }


    public void filterCity() {
        searchTableLoad();


        btn_list_all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTableLoad(); // Reload search table
                helperForDateQuery();
            }
        });
    }


    public void filterForCity() {
        cmb_search_city.removeAllItems();
        cmb_search_city.addItem(null);
        for (Hotel hotel : hotelManager.findByAll()) {
            cmb_search_city.addItem(new ComboItem(hotel.getHotel_id(), hotel.getHotel_city()));
        }
    }


    public void helperForDateQuery() {

        String inYear = String.valueOf(cmb_in_year.getSelectedItem());
        String outYear = String.valueOf(cmb_out_year.getSelectedItem());

        String inMonth = String.valueOf(cmb_in_month.getSelectedItem());
        String outMonth = String.valueOf(cmb_out_month.getSelectedItem());

        String inDay = String.valueOf(cmb_in_day.getSelectedItem());
        String outDay = String.valueOf(cmb_out_day.getSelectedItem());

        String inDate = inYear + "/" + inMonth + "/" + inDay;
        String outDate = outYear + "/" + outMonth + "/" + outDay;

    }


    public void addDate() {

        cmb_in_year.addItem(null);
        cmb_out_year.addItem(null);
        for (int year = 2024; year <= 2034; year++) {
            cmb_in_year.addItem(String.valueOf(year));
            cmb_out_year.addItem(String.valueOf(year));
        }


        int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        cmb_in_month.addItem(null);
        cmb_out_month.addItem(null);
        for (int i = 0; i < months.length; i++) {
            cmb_in_month.addItem(months[i]);
            cmb_out_month.addItem(months[i]);
        }


        updateDaysComboBox();

        cmb_in_year.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDaysComboBox();
            }
        });
        cmb_in_month.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDaysComboBox();
            }
        });
        cmb_out_year.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDaysComboBox();
            }
        });
        cmb_out_month.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDaysComboBox();
            }
        });
    }


    private void updateDaysComboBox() {
        int selectedInYear = cmb_in_year.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_in_year.getSelectedItem())) : 0;
        int selectedOutYear = cmb_out_year.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_out_year.getSelectedItem())) : 0;
        int selectedInMonth = cmb_in_month.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_in_month.getSelectedItem())) : 0;
        int selectedOutMonth = cmb_out_month.getSelectedItem() != null ? Integer.parseInt(String.valueOf(cmb_out_month.getSelectedItem())) : 0;


        int maxInDays = getMaxDays(selectedInYear, selectedInMonth);
        int maxOutDays = getMaxDays(selectedOutYear, selectedOutMonth);


        int selectedInDay = Math.min(cmb_in_day.getItemCount(), maxInDays);
        int selectedOutDay = Math.min(cmb_out_day.getItemCount(), maxOutDays);
        cmb_in_day.removeAllItems();
        cmb_in_day.addItem(null);
        for (int day = 1; day <= maxInDays; day++) {
            cmb_in_day.addItem(String.valueOf(day));
        }
        cmb_out_day.removeAllItems();
        cmb_out_day.addItem(null);
        for (int day = 1; day <= maxOutDays; day++) {
            cmb_out_day.addItem(String.valueOf(day));
        }


        cmb_in_day.setSelectedIndex(Math.min(selectedInDay - 1, maxInDays - 1));
        cmb_out_day.setSelectedIndex(Math.min(selectedOutDay - 1, maxOutDays - 1));
    }


    private int getMaxDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 0;
        }
    }
}
