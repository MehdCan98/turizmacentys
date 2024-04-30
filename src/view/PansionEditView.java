package view;

import business.PansionManager;
import core.Helper;
import entity.Pansion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PansionEditView extends Layout {
    private JPanel container;
    private JPanel w_top;
    private JPanel w_bottom;
    private JButton btn_back;
    private JTextField fld_pansion_type;
    private JButton btn_save;
    private Pansion pansion = null;
    private PansionManager pansionManager;


    public PansionEditView(Pansion pansion) {
        pansionManager = new PansionManager();
        this.pansion = pansion;
        add(container);
        pageArt(700, 500, "Edit");
        setTheme("Nimbus");


        if (pansion.getPansion_id() != 0) {
            loadPansion();
        }


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdatePansion();
            }
        });


        btn_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current view
            }
        });
    }


    private void savePansions() {
        pansion.setPansion_type(fld_pansion_type.getText());
        pansionManager.save(pansion);
        Helper.getMessage("Saved", "Information");
        btn_back.setText("Back");
    }


    private void saveOrUpdatePansion() {
        if (Helper.isFieldListEmpty(new JTextField[]{fld_pansion_type})) {
            Helper.getMessage("Not Null", "Error");
        } else {
            if (pansion.getPansion_id() != 0) {
                updatePansion();
            } else {
                savePansions();
            }
        }
    }


    private void updatePansion() {
        pansion.setPansion_type(fld_pansion_type.getText());
        pansionManager.update(pansion);
        Helper.getMessage("Update", "Information");
        btn_back.setText("Back");
    }


    private void loadPansion() {
        fld_pansion_type.setText(pansion.getPansion_type());
    }
}
