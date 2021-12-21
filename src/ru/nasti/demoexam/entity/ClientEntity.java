package ru.nasti.demoexam.entity;

import lombok.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

@Data
public class ClientEntity {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthday;
    private Date registrationDate;
    private String email;
    private String phone;
    private String genderCode;
    private String photoPath;
    private ImageIcon icon;

    public ClientEntity(int id, String firstName, String lastName, String patronymic, Date birthday, Date registrationDate, String email, String phone, String genderCode, String photoPath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.email = email;
        this.phone = phone;
        this.genderCode = genderCode;
        this.photoPath = photoPath;
        try {
            this.icon = new ImageIcon(
                    ImageIO.read(ClientEntity.class.getClassLoader().getResource("cat.jpg"))
                            .getScaledInstance(50, 50, Image.SCALE_DEFAULT)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClientEntity(String firstName, String lastName, String patronymic, Date birthday, Date registrationDate, String email, String phone, String genderCode, String photoPath) {
        this(-1, firstName, lastName, patronymic, birthday, registrationDate, email, phone, genderCode, photoPath);
    }
}
