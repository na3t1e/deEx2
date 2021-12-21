package ru.nasti.demoexam.ui;

import ru.nasti.Main;
import ru.nasti.demoexam.entity.ClientEntity;
import ru.nasti.demoexam.manager.ClientEntityManager;
import ru.nasti.demoexam.utils.BaseForm;
import ru.nasti.demoexam.utils.Forms;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;

public class CreateClientForm extends BaseForm {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField patronymicField;
    private JTextField birthdayField;
    private JTextField registrationField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox genderBox;
    private JTextField photoField;
    private JButton сreateButton;
    private JPanel mainPanel;
    private JButton backButton;

    public CreateClientForm() {
        super(500, 400);
        setContentPane(mainPanel);
        initBoxes();
        initButtons();
        setVisible(true);
    }

    private void initBoxes() {
        genderBox.addItem("ж");
        genderBox.addItem("м");
    }

    private void initButtons() {
        сreateButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            if (firstName.isEmpty() || firstName.length() > 50) {
                Forms.errors(this, "Имя не введено или оно слишком длинное");
                return;
            }
            String lastName = lastNameField.getText();
            if (lastName.isEmpty() || lastName.length() > 50) {
                Forms.errors(this, "Фамилия не введена или она слишком длинная");
                return;
            }
            String patronymic = patronymicField.getText();
            if (patronymic.isEmpty() || patronymic.length() > 50) {
                Forms.errors(this, "Отчество не введено или оно слишком длинное");
                return;
            }
            Date birthday = null;
            try {
                birthday = Main.formatB.parse(birthdayField.getText());
            } catch (Exception ex) {
                Forms.errors(this, "Дата рождения введена в неправильно, корректный формат: dd.MM.yyyy");
                return;
            }
            Date registration = null;
            try {
                registration = Main.formatR.parse(registrationField.getText());
            } catch (Exception ex) {
                Forms.errors(this, "Дата регистрации введена в неправильно, корректный формат: dd.MM.yyyy hh:mm");
                return;
            }
            String phone = phoneField.getText();
            if (phone.isEmpty() || phone.length() > 20) {
                Forms.errors(this, "Номер не введён или он слишком длинный");
                return;
            }


            ClientEntity client = new ClientEntity(firstName, lastName, patronymic, birthday, registration, emailField.getText(), phone, (String) genderBox.getSelectedItem(), photoField.getText());
            try {
                ClientEntityManager.insert(client);
            } catch (SQLException exception) {
                Forms.errors(this, "Ошибка сохранения данных");
                exception.printStackTrace();
                return;
            }

            Forms.infos(this, "Клиент успешно добавлен");
            dispose();
            new TableClientForm();

        });
        backButton.addActionListener(e -> {
            dispose();
            new TableClientForm();
        });
    }
}
