package ru.nasti.demoexam.ui;

import ru.nasti.Main;
import ru.nasti.demoexam.entity.ClientEntity;
import ru.nasti.demoexam.manager.ClientEntityManager;
import ru.nasti.demoexam.utils.BaseForm;
import ru.nasti.demoexam.utils.Forms;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;

public class EditClientForm extends BaseForm {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField patronymicField;
    private JTextField birthdayField;
    private JTextField registrationField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox genderBox;
    private JTextField photoField;
    private JButton saveButton;
    private JPanel mainPanel;
    private JButton backButton;
    private JTextField idField;
    private JButton deleteButton;

    private ClientEntity client;

    public EditClientForm(ClientEntity client) {
        super(500, 400);
        setContentPane(mainPanel);
        this.client = client;
        initBoxes();
        initButtons();
        initFields();
        setVisible(true);

    }

    private void initFields() {
        idField.setEditable(false);
        idField.setText(String.valueOf(client.getId()));
        firstNameField.setText(client.getFirstName());
        lastNameField.setText(client.getLastName());
        patronymicField.setText(client.getPatronymic());
        birthdayField.setText(Main.formatB.format(client.getBirthday()));
        registrationField.setText(Main.formatR.format(client.getRegistrationDate()));
        emailField.setText(client.getEmail());
        phoneField.setText(client.getPhone());
    }

    private void initBoxes() {
        genderBox.addItem("ж"); // женский
        genderBox.addItem("м");
        genderBox.setSelectedItem(client.getGenderCode());
    }

    private void initButtons() {
        saveButton.addActionListener(e -> {
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

            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setPatronymic(patronymic);
            client.setBirthday(birthday);
            client.setRegistrationDate(registration);
            client.setEmail(emailField.getText());
            client.setPhone(phone);
            client.setGenderCode((String) genderBox.getSelectedItem());
            client.setPhotoPath(photoField.getText());

            try {
                ClientEntityManager.edit(client);
            } catch (SQLException exception) {
                Forms.errors(this, "Ошибка сохранения данных");
                exception.printStackTrace();
                return;
            }
            Forms.infos(this, "Данные успешно изменены");
            dispose();
            new TableClientForm();
        });
        backButton.addActionListener(e -> {
            dispose();
            new TableClientForm();
        });
        deleteButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Вы точно хотите удалить?", "Вопрос", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                try {
                    ClientEntityManager.delete(client.getId());
                    Forms.infos(this, "Данные успешно удалены");
                    dispose();
                    new TableClientForm();
                } catch (SQLException exception) {
                    Forms.errors(this, "Ошибка удаления данных");
                    exception.printStackTrace();
                }

        });
    }

}
