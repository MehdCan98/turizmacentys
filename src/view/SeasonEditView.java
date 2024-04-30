package view;

import business.HotelManager;
import business.SeasonManager;
import core.ComboItem;
import core.Helper;
import entity.Hotel;
import entity.Season;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SeasonEditView extends Layout {

    private JPanel container;
    private JTextField fld_season_start_date;
    private JComboBox cmb_season_hotel_name;
    private JTextField fld_season_end_date;
    private JLabel lbl_season_hotel_name;
    private JLabel lbl_season_start_date;
    private JLabel lbl_season_end_date;
    private JButton btn_season_save;
    private JButton btn_season_back;
    private JComboBox cmb_season_name;

    private Season season;
    private SeasonManager seasonManager;
    private HotelManager hotelManager;


    public SeasonEditView(Season season) {
        this.season = season;
        seasonManager = new SeasonManager();
        hotelManager = new HotelManager();


        add(container);
        // Set page attributes
        pageArt(640, 480, "Season Edit View");
        setTheme("Nimbus");

        if (season.getSeason_hotel_id() != 0) {
            loadHotelDetails();
        }


        createComboBox();


        fld_season_start_date.setText("01/06/" + LocalDate.now().getYear());
        fld_season_end_date.setText("01/09/" + LocalDate.now().getYear());


        btn_season_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btn_season_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdateHotel();
            }
        });
    }


    private void loadHotelDetails() {
        fld_season_start_date.setText(String.valueOf(season.getSeason_start_date()));
        fld_season_end_date.setText(String.valueOf(season.getSeason_end_date()));
        cmb_season_name.setSelectedItem(season.getSeasonName());
        cmb_season_hotel_name.setSelectedItem(season.getHotelName());
    }


    private void createComboBox() {

        for (Hotel hotel : hotelManager.findByAll()) {
            cmb_season_hotel_name.addItem(new ComboItem(hotel.getHotel_id(), hotel.getHotel_name()));
        }

        for (Season.SeasonName seasonName : Season.SeasonName.values()) {
            cmb_season_name.addItem(seasonName.toString());
        }


        cmb_season_name.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cmb_season_name.getSelectedItem() == Season.SeasonName.SUMMER.toString()) {
                    fld_season_start_date.setText("01/06/" + LocalDate.now().getYear());
                    fld_season_end_date.setText("01/09/" + LocalDate.now().getYear());
                }
                if (cmb_season_name.getSelectedItem() == Season.SeasonName.WINTER.toString()) {
                    fld_season_start_date.setText("01/01/" + LocalDate.now().getYear());
                    fld_season_end_date.setText("31/05/" + LocalDate.now().getYear());
                }
                if (cmb_season_name.getSelectedItem() == Season.SeasonName.FULL.toString()) {
                    fld_season_start_date.setText("01/01/" + LocalDate.now().getYear());
                    fld_season_end_date.setText("31/12/" + LocalDate.now().getYear());
                }
            }
        });
    }


    private void saveOrUpdateHotel() {

        if (Helper.isFieldListEmpty(new JTextField[]{fld_season_start_date, fld_season_end_date})) {
            Helper.getMessage("Not Null", "Error");
        } else {

            if (season.getSeason_id() != 0) {
                updateSeason();
            } else {

                saveSeason();
            }
        }
    }


    private void updateSeason() {
        ComboItem hotelName = (ComboItem) cmb_season_hotel_name.getSelectedItem();
        season.setSeason_hotel_id(hotelName.getKey());
        season.setHotelName(hotelName.getValue());
        season.setSeasonName(Season.SeasonName.valueOf(cmb_season_name.getSelectedItem().toString()));

        season.setSeason_start_date(LocalDate.parse(fld_season_start_date.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        season.setSeason_end_date(LocalDate.parse(fld_season_end_date.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        seasonManager.update(season);

        Helper.getMessage("Update", "Information");
        btn_season_back.setText("Back");
    }


    private void saveSeason() {
        ComboItem hotelName = (ComboItem) cmb_season_hotel_name.getSelectedItem();
        season.setSeason_hotel_id(hotelName.getKey());
        season.setHotelName(hotelName.getValue());
        season.setSeasonName(Season.SeasonName.valueOf(cmb_season_name.getSelectedItem().toString()));

        season.setSeason_start_date(LocalDate.parse(fld_season_start_date.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        season.setSeason_end_date(LocalDate.parse(fld_season_end_date.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        seasonManager.save(season);
        Helper.getMessage("Saved", "Information");
        btn_season_back.setText("Back");
    }
}
