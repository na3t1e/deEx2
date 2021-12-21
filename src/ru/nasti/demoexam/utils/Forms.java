package ru.nasti.demoexam.utils;

import javax.swing.*;
import java.awt.*;

public class Forms {
    public static void errors(Component parent, String text) {
        JOptionPane.showMessageDialog(parent, text, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    public static void infos(Component parent, String text) {
        JOptionPane.showMessageDialog(parent, text, "Внимание", JOptionPane.INFORMATION_MESSAGE);
    }
}
