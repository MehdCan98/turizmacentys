package view;

import business.HotelManager;
import business.PansionManager;
import business.RoomManager;
import business.SeasonManager;
import core.ComboItem;
import core.Helper;
import entity.Hotel;
import entity.Pansion;
import entity.Room;
import entity.Season;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomEditView extends Layout {

    private JPanel container;
    private JButton btn_save;
    private JButton btn_back;

    private JLabel lbl_hotel_name;
    private JLabel lbl_hotel_city;
    private JLabel lbl_hotel_region;
    private JLabel lbl_hotel_adress;
    private JLabel lbl_hotel_email;
    private JLabel lbl_hotel_phone;
    private JLabel lbl_hotel_rating;

    private JTextField fld_room_number;
    private JTextField fld_capacity;
    private JTextField fld_adult_price;
    private JLabel lbl_hotel_free_parkin;
    private JLabel lbl_hotel_free_wifi;
    private JLabel lbl_hotel_swimming_pool;
    private JLabel lbl_hotel_fitness_center;
    private JLabel lbl_hotel_concierge;
    private JLabel lbl_hotel_spa;
    private JLabel lbl_hotel_room_services;

    private JComboBox cmb_has_tv;
    private JComboBox cmb_has_bar;
    private JComboBox cmb_has_xbox;
    private JComboBox cmb_has_safebox;
    private JComboBox cmb_hotel_name;
    private JComboBox cmb_seasson;
    private JComboBox cmb_room_type;
    private JTextField fld_child_price;
    private JTextField fld_stock;
    private JTextField fld_beds_count;
    private JTextField fld_square_meters;
    private JComboBox cmb_has_projector;
    private JComboBox cmb_pansion_type;

    private RoomManager roomManager;
    private HotelManager hotelManager;
    private PansionManager pansionManager;
    private SeasonManager seasonManager;

    private DefaultTableModel model;

    private Room room;


    public RoomEditView(Room room) {
        this.room = room;

        hotelManager = new HotelManager();
        roomManager = new RoomManager();
        pansionManager = new PansionManager();
        seasonManager = new SeasonManager();

        add(container);

        pageArt(700, 500, "Edit View");
        setTheme("Nimbus");


        if (room.getRoom_id() != 0) {
            loadRoomDetails();
        }


        createComboBox();


        btn_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        cmb_hotel_name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cmb_seasson.removeAllItems();

                String selectedHotelName = String.valueOf(cmb_hotel_name.getSelectedItem());

                int selectedHotelId = hotelManager.getHotelIdByName(selectedHotelName);

                for (Season season : seasonManager.findByAll()) {
                    if (season.getSeason_hotel_id() == selectedHotelId) {
                        cmb_seasson.addItem(season.getSeasonName());
                    }
                }

                for (Hotel hotel : hotelManager.findByAll()) {
                    if (hotel.getHotel_id() == selectedHotelId) {
                        String[] pansionTypes = hotel.getHotel_pansion_type();
                        cmb_pansion_type.removeAllItems();
                        for (String type : pansionTypes) {
                            cmb_pansion_type.addItem(type);
                        }
                    }
                }
            }
        });


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdateHotel();
            }
        });
    }


    private void loadRoomDetails() {
        fld_adult_price.setText(String.valueOf(room.getRoom_adult_price()));
        fld_child_price.setText(String.valueOf(room.getRoom_child_price()));
        fld_beds_count.setText(String.valueOf(room.getRoom_bed_count()));
        fld_capacity.setText(String.valueOf(room.getRoom_capacity()));
        fld_stock.setText(String.valueOf(room.getRoom_stock_quantity()));
        fld_square_meters.setText(String.valueOf(room.getRoom_square_meters()));
        fld_room_number.setText(room.getRoom_number());
    }

    private void createComboBox() {

        cmb_has_bar.addItem(true);
        cmb_has_bar.addItem(false);

        cmb_has_projector.addItem(true);
        cmb_has_projector.addItem(false);

        cmb_has_safebox.addItem(true);
        cmb_has_safebox.addItem(false);

        cmb_has_tv.addItem(true);
        cmb_has_tv.addItem(false);

        cmb_has_xbox.addItem(true);
        cmb_has_xbox.addItem(false);


        for (Hotel hotel : hotelManager.findByAll()) {
            cmb_hotel_name.addItem(new ComboItem(hotel.getHotel_id(), hotel.getHotel_name(),hotel.getHotel_city()));
        }

        for (Pansion pansion : pansionManager.findByAll()) {
            cmb_pansion_type.addItem(pansion.getPansion_type().toString());
        }

        for (Room.Type type : Room.Type.values()) {
            cmb_room_type.addItem(type.toString());
        }
    }


    private void saveOrUpdateHotel() {

        if (Helper.isFieldListEmpty(new JTextField[]{fld_room_number, fld_stock, fld_capacity, fld_square_meters, fld_beds_count, fld_child_price, fld_adult_price})) {
            Helper.getMessage("Not Null", "Error");
        } else {

            if (room.getRoom_id() != 0) {
                updateRoom();
            } else {

                saveRoom();
            }
        }
    }

    private void updateRoom() {

        ComboItem items = (ComboItem) cmb_hotel_name.getSelectedItem();
        room.setRoom_hotel_id(items.getKey());

        room.setRoom_seasson_id(seasonManager.getSeasonIdBySeasonName(cmb_seasson.getSelectedItem().toString()));
        room.setRoomType(Room.Type.valueOf(cmb_room_type.getSelectedItem().toString()));
        room.setRoom_number(fld_room_number.getText());
        room.setRoom_capacity(Integer.parseInt(fld_capacity.getText()));
        room.setRoom_adult_price(Integer.parseInt(fld_adult_price.getText()));
        room.setRoom_child_price(Integer.parseInt(fld_child_price.getText()));
        room.setRoom_stock_quantity(Integer.parseInt(fld_stock.getText()));
        room.setRoom_bed_count(Integer.parseInt(fld_beds_count.getText()));
        room.setRoom_square_meters(Integer.parseInt(fld_square_meters.getText()));
        room.setRoom_has_tv((Boolean) cmb_has_tv.getSelectedItem());
        room.setRoom_has_mini_bar((Boolean) cmb_has_bar.getSelectedItem());
        room.setRoom_has_gaming_console((Boolean) cmb_has_xbox.getSelectedItem());
        room.setRoom_has_projector((Boolean) cmb_has_projector.getSelectedItem());
        room.setRoom_has_safe_box((Boolean) cmb_has_safebox.getSelectedItem());
        room.setRoom_pansion_type(cmb_pansion_type.getSelectedItem().toString());

        roomManager.update(room);


        Helper.getMessage("Update", "Information");// Seciton 24-25 : The user is given appropriate pop up messages for successful transactions. Appropriate error messages are given to the user for incorrect operations.
        btn_back.setText("Back");
    }


    private void saveRoom() {

        ComboItem items = (ComboItem) cmb_hotel_name.getSelectedItem();
        room.setRoom_hotel_id(items.getKey());
        room.setRoom_hotel_name(items.getValue());
        room.setRoom_hotel_city(items.getMoreValue());

        room.setRoomType(Room.Type.valueOf(cmb_room_type.getSelectedItem().toString()));
        room.setRoom_number(fld_room_number.getText());
        room.setRoom_capacity(Integer.parseInt(fld_capacity.getText()));
        room.setRoom_adult_price(Integer.parseInt(fld_adult_price.getText()));
        room.setRoom_child_price(Integer.parseInt(fld_child_price.getText()));
        room.setRoom_stock_quantity(Integer.parseInt(fld_stock.getText()));
        room.setRoom_bed_count(Integer.parseInt(fld_beds_count.getText()));
        room.setRoom_square_meters(Integer.parseInt(fld_square_meters.getText()));
        room.setRoom_has_tv((Boolean) cmb_has_tv.getSelectedItem());
        room.setRoom_has_mini_bar((Boolean) cmb_has_bar.getSelectedItem());
        room.setRoom_has_gaming_console((Boolean) cmb_has_xbox.getSelectedItem());
        room.setRoom_has_projector((Boolean) cmb_has_projector.getSelectedItem());
        room.setRoom_has_safe_box((Boolean) cmb_has_safebox.getSelectedItem());


        if (cmb_pansion_type.getSelectedItem() == null || cmb_seasson.getSelectedItem() == null) {

            Helper.getMessage("Pansion or Season type is empty, please check hotel edit page", "Information");// Seciton 24-25 : The user is given appropriate pop up messages for successful transactions. Appropriate error messages are given to the user for incorrect operations.
            btn_save.setVisible(false);
            btn_back.setText("Exit");

            btn_back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        } else {

            room.setRoom_seasson_id(seasonManager.getSeasonIdBySeasonName(cmb_seasson.getSelectedItem().toString()));
            room.setRoom_pansion_type(cmb_pansion_type.getSelectedItem().toString());
            room.setRoom_season_start(seasonManager.getById(seasonManager.getSeasonIdBySeasonName(cmb_seasson.getSelectedItem().toString())).getSeason_start_date());
            room.setRoom_season_end(seasonManager.getById(seasonManager.getSeasonIdBySeasonName(cmb_seasson.getSelectedItem().toString())).getSeason_end_date());
            room.setRoom_season_name(cmb_seasson.getSelectedItem().toString());

            roomManager.save(room);

            Helper.getMessage("Saved", "Information");// Seciton 24-25 : The user is given appropriate pop up messages for successful transactions. Appropriate error messages are given to the user for incorrect operations.
            btn_back.setText("Back");
        }
    }
}
