package view;

import business.HotelManager;
import business.PansionManager;
import business.SeasonManager;
import core.Helper;
import entity.Hotel;
import entity.Pansion;
import entity.Season;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HotelEditView extends Layout {
    private JPanel container;
    private JButton btn_save;
    private JButton btn_back;
    private JTextField fld_hotel_name;
    private JComboBox<Boolean> cmbx_hotel_freepark;
    private JComboBox<Boolean> cmbx_hotel_free_wifi;
    private JComboBox<Boolean> cmbx_hotel_swimming_pool;
    private JComboBox<Boolean> cmbx_hotel_fitness_center;
    private JComboBox<Boolean> cmbx_hotel_concierge;
    private JComboBox<Boolean> cmbx_hotel_spa;
    private JComboBox<Boolean> cmbx_hotel_room_services;
    private JTextField fld_hotel_city;
    private JTextField fld_hotel_region;
    private JTextField fld_hotel_adress;
    private JTextField fld_hotel_email;
    private JTextField fld_hotel_phone;
    private JComboBox<String> cmbx_hotel_rating;
    private JTable tbl_pansion_type;
    private JLabel lbl_hotel_name;
    private JLabel lbl_hotel_city;
    private JLabel lbl_hotel_region;
    private JLabel lbl_hotel_adress;
    private JLabel lbl_hotel_email;
    private JLabel lbl_hotel_phone;
    private JLabel lbl_hotel_rating;
    private JLabel lbl_hotel_free_parkin;
    private JLabel lbl_hotel_free_wifi;
    private JLabel lbl_hotel_swimming_pool;
    private JLabel lbl_hotel_fitness_center;
    private JLabel lbl_hotel_concierge;
    private JLabel lbl_hotel_spa;
    private JLabel lbl_hotel_room_services;
    private JFormattedTextField fld_start_seasson;
    private JFormattedTextField fld_end_seasson;
    private Hotel hotel = null;
    private HotelManager hotelManager;
    private DefaultTableModel model;
    private PansionManager pansionManager;
    private SeasonManager seasonManager;

    public HotelEditView(Hotel hotel) {
        seasonManager = new SeasonManager();
        hotelManager = new HotelManager();
        pansionManager = new PansionManager();
        this.hotel = hotel;
        add(container);
        pageArt(700, 1080, "Edit");

        loadPansionTypes();


        if (hotel.getHotel_id() != 0) {
            loadHotelDetails();
        }

        createComboBox();


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdateHotel();
            }
        });


        btn_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }


    private void loadPansionTypes() {
        model = (DefaultTableModel) tbl_pansion_type.getModel();
        model.setRowCount(0);
        model.addColumn("ID");
        model.addColumn("Pansion Type");


        for (Pansion pansion : pansionManager.findByAll()) {
            model.addRow(new Object[]{pansion.getPansion_id(), pansion.getPansion_type()});


            tbl_pansion_type.setSelectionModel(new DefaultListSelectionModel() {
                @Override
                public void setSelectionInterval(int index0, int index1) {
                    if (super.isSelectedIndex(index0)) {
                        super.removeSelectionInterval(index0, index1);
                    } else {
                        super.addSelectionInterval(index0, index1);
                    }
                }
            });
        }

        fld_start_seasson.setText("--/--/----");
        fld_end_seasson.setText("--/--/----");
    }


    private void loadHotelDetails() {
        fld_hotel_name.setText(hotel.getHotel_name());
        fld_hotel_city.setText(hotel.getHotel_city());
        fld_hotel_region.setText(hotel.getHotel_region());
        fld_hotel_adress.setText(hotel.getHotel_fulladres());
        fld_hotel_email.setText(hotel.getHotel_mail());
        fld_hotel_phone.setText(hotel.getHotel_phone());
        cmbx_hotel_rating.setSelectedItem(hotel.getHotel_star());
        cmbx_hotel_freepark.setSelectedItem(hotel.isHotel_free_parking());
        cmbx_hotel_swimming_pool.setSelectedItem(hotel.isHotel_swimming_pool());
        cmbx_hotel_fitness_center.setSelectedItem(hotel.isHotel_fitness_center());
        cmbx_hotel_concierge.setSelectedItem(hotel.isHotel_concierge());
        cmbx_hotel_spa.setSelectedItem(hotel.isHotel_spa());
        cmbx_hotel_room_services.setSelectedItem(hotel.isHotel_room_services());
        cmbx_hotel_free_wifi.setSelectedItem(hotel.isHotel_free_wifi());

        for (Season season : seasonManager.findByAll()) {
            fld_start_seasson.setText(String.valueOf(season.getSeason_start_date()));
            fld_end_seasson.setText(String.valueOf(season.getSeason_end_date()));
        }
    }


    private void createComboBox() {
        cmbx_hotel_rating.addItem("1");
        cmbx_hotel_rating.addItem("2");
        cmbx_hotel_rating.addItem("3");
        cmbx_hotel_rating.addItem("4");
        cmbx_hotel_rating.addItem("5");

        cmbx_hotel_freepark.addItem(true);
        cmbx_hotel_freepark.addItem(false);

        cmbx_hotel_swimming_pool.addItem(true);
        cmbx_hotel_swimming_pool.addItem(false);

        cmbx_hotel_fitness_center.addItem(true);
        cmbx_hotel_fitness_center.addItem(false);

        cmbx_hotel_concierge.addItem(true);
        cmbx_hotel_concierge.addItem(false);

        cmbx_hotel_spa.addItem(true);
        cmbx_hotel_spa.addItem(false);

        cmbx_hotel_room_services.addItem(true);
        cmbx_hotel_room_services.addItem(false);

        cmbx_hotel_free_wifi.addItem(true);
        cmbx_hotel_free_wifi.addItem(false);
    }


    private void saveOrUpdateHotel() {
        if (Helper.isFieldListEmpty(new JTextField[]{fld_hotel_name, fld_hotel_city, fld_hotel_region, fld_hotel_adress, fld_hotel_email, fld_hotel_phone})) {
            Helper.getMessage("Not Null", "Error");
        } else {
            if (hotel.getHotel_id() != 0) {
                updateHotel();
            } else {
                saveHotel();
            }
        }
    }


    private void updateHotel() {



        hotel.setHotel_name(fld_hotel_name.getText());
        hotel.setHotel_city(fld_hotel_city.getText());
        hotel.setHotel_region(fld_hotel_region.getText());
        hotel.setHotel_fulladres(fld_hotel_adress.getText());
        hotel.setHotel_mail(fld_hotel_email.getText());
        hotel.setHotel_phone(fld_hotel_phone.getText());
        hotel.setHotel_star(String.valueOf(cmbx_hotel_rating.getSelectedItem()));
        hotel.setHotel_free_parking((Boolean) cmbx_hotel_freepark.getSelectedItem());
        hotel.setHotel_free_wifi((Boolean) cmbx_hotel_free_wifi.getSelectedItem());
        hotel.setHotel_swimming_pool((Boolean) cmbx_hotel_swimming_pool.getSelectedItem());
        hotel.setHotel_fitness_center((Boolean) cmbx_hotel_fitness_center.getSelectedItem());
        hotel.setHotel_concierge((Boolean) cmbx_hotel_concierge.getSelectedItem());
        hotel.setHotel_spa((Boolean) cmbx_hotel_spa.getSelectedItem());
        hotel.setHotel_room_services((Boolean) cmbx_hotel_room_services.getSelectedItem());


        for (Season season : seasonManager.findByAll()) {
            fld_start_seasson.setText(String.valueOf(season.getSeason_start_date()));
            fld_end_seasson.setText(String.valueOf(season.getSeason_end_date()));
        }


        int[] selectedRows = tbl_pansion_type.getSelectedRows();
        String[] selectedPansionTypes = new String[selectedRows.length];

        for (int i = 0; i < selectedRows.length; i++) {
            selectedPansionTypes[i] = (String) tbl_pansion_type.getValueAt(selectedRows[i], 1);
        }


        hotel.setHotel_pansion_type(selectedPansionTypes);


        hotelManager.update(hotel);


        Helper.getMessage("Update", "Information");
        btn_back.setText("Back");
    }


    private void saveHotel() {




        hotel.setHotel_name(fld_hotel_name.getText());
        hotel.setHotel_city(fld_hotel_city.getText());
        hotel.setHotel_region(fld_hotel_region.getText());
        hotel.setHotel_fulladres(fld_hotel_adress.getText());
        hotel.setHotel_mail(fld_hotel_email.getText());
        hotel.setHotel_phone(fld_hotel_phone.getText());
        hotel.setHotel_star(String.valueOf(cmbx_hotel_rating.getSelectedItem()));
        hotel.setHotel_free_parking((Boolean) cmbx_hotel_freepark.getSelectedItem());
        hotel.setHotel_free_wifi((Boolean) cmbx_hotel_free_wifi.getSelectedItem());
        hotel.setHotel_swimming_pool((Boolean) cmbx_hotel_swimming_pool.getSelectedItem());
        hotel.setHotel_fitness_center((Boolean) cmbx_hotel_fitness_center.getSelectedItem());
        hotel.setHotel_concierge((Boolean) cmbx_hotel_concierge.getSelectedItem());
        hotel.setHotel_spa((Boolean) cmbx_hotel_spa.getSelectedItem());
        hotel.setHotel_room_services((Boolean) cmbx_hotel_room_services.getSelectedItem());


        for (Season season : seasonManager.findByAll()) {
            if (hotel.getHotel_id() == season.getSeason_hotel_id()) {
                fld_start_seasson.setText(String.valueOf(season.getSeason_start_date()));
                fld_end_seasson.setText(String.valueOf(season.getSeason_end_date()));
            }

            if (hotel.getHotel_id() == 0) {
                fld_start_seasson.setText("2024-01-01");
                fld_end_seasson.setText("2024-12-31");
            }
        }


        int[] selectedRows = tbl_pansion_type.getSelectedRows();
        String[] selectedPansionTypes = new String[selectedRows.length];

        for (int i = 0; i < selectedRows.length; i++) {
            selectedPansionTypes[i] = tbl_pansion_type.getValueAt(selectedRows[i], 1).toString();
        }


        hotel.setHotel_pansion_type(selectedPansionTypes);


        hotelManager.save(hotel);


        Helper.getMessage("Saved" + "\n" + "Please set a Seasson!", "Information");
        btn_back.setText("Back");
    }
}
