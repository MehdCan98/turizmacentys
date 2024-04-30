package core;

import javax.swing.*;

public class Helper {


    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }


    public static void getMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }


    public static boolean isFieldListEmpty(JTextField[] fieldList) {
        for (JTextField field : fieldList) {
            if (isFieldEmpty(field)) return true;
        }
        return false;
    }
}
