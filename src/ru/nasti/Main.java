package ru.nasti;

import ru.nasti.demoexam.ui.TableClientForm;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Main {
    public static SimpleDateFormat formatB = new SimpleDateFormat("dd.MM.yyyy");
    public static SimpleDateFormat formatR = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeAllFonts(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 12));

        new TableClientForm();

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/exam",
                "root",
                "1234");
    }

    public static void changeAllFonts(Font font) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
