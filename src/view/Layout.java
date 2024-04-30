package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Layout extends JFrame {


    public Layout() {}


    public void pageArt(int width, int height, String title) {
        setSize(width, height);
        setTitle(title);
        setResizable(false);
        setVisible(true);

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2;
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void setTheme(String themeName) {

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals(themeName)) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void createTable(DefaultTableModel model, JTable table, Object[] columns, ArrayList<Object[]> rows) {
        model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(columns);
        table.getTableHeader().setReorderingAllowed(false);

        model.setRowCount(0);

        if (rows == null) {
            rows = new ArrayList<>();
        }


        for (Object[] row : rows) {
            model.addRow(row);
        }
    }


    public int getSelectedRow(JTable table, int columnIndex) {

        return (int) table.getValueAt(table.getSelectedRow(), columnIndex);
    }


    public void tableMouseSelect(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table.rowAtPoint(e.getPoint()); // Get the selected row
                table.setRowSelectionInterval(selectedRow, selectedRow); // Select the row
            }
        });
    }
}
