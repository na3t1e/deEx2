package ru.nasti.demoexam.ui;

import ru.nasti.demoexam.entity.ClientEntity;
import ru.nasti.demoexam.manager.ClientEntityManager;
import ru.nasti.demoexam.utils.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

public class TableClientForm extends BaseForm {
    private JPanel mainPanel;
    private JTable table;
    private JButton createButton;
    private JComboBox genderComboBox;
    private JComboBox nameComboBox;
    private JButton resetButton;
    private JButton surnameButton;
    private JButton dateButton;
    private JButton helpButton;
    private JButton aboutButton;
    private JLabel rowCount;
    private JTextField searchTextField;

    private ExtendedTableModel<ClientEntity> model;

    private boolean surnameSort = false;
    private boolean dateSort = false;

    public TableClientForm() {
        super(800, 600);
        setContentPane(mainPanel);
        initTable();
        initButtons();
        initElements();
        setVisible(true);
    }

    private void initTable() {
        table.setRowHeight(50);
        table.getTableHeader().setReorderingAllowed(false);

        try {
            model = new ExtendedTableModel<>(ClientEntity.class, new String[]{"ID", "Имя", "Фамилия", "Отчество", "День рождения", "Дата регистрации", "Email", "Телефон", "Пол", "Фото", "Фото котика"}) {
                @Override
                public void onUpdateRowEvents() {
                    rowCount.setText("Показано записей " + model.getFilteredRows().size() + " / " + model.getAllRows().size());
                }
            };
            model.setAllRows(ClientEntityManager.select());
            table.setModel(model);
            if (model.getFilteredRows().size() == 0) {
                Forms.infos(this, "В базе данных не найдено записей");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Forms.errors(this, "Ошибка получения данных!");
        }
        TableColumn column = table.getColumn("Фото");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        model.getFilters()[0] = new Predicate<ClientEntity>() {
            @Override
            public boolean test(ClientEntity clientEntity) {
                String search = searchTextField.getText();
                if (search == null || search.isEmpty()) {
                    return true;
                }
                String names = clientEntity.getLastName() + " " + clientEntity.getFirstName() + " " + clientEntity.getPatronymic();
                return names.startsWith(search);
            }
        };

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        dispose();
                        new EditClientForm(model.getAllRows().get(row));
                    }
                }
            }

        });
        genderComboBox.addItem("Пол");
        genderComboBox.addItem("ж");
        genderComboBox.addItem("м");

        model.getFilters()[1] = new Predicate<ClientEntity>() {
            @Override
            public boolean test(ClientEntity clientEntity) {
                int selected = genderComboBox.getSelectedIndex();
                if (selected != 0) {
                    return clientEntity.getGenderCode().equals(genderComboBox.getSelectedItem());
                } else {
                    return true;
                }
            }
        };

        nameComboBox.addItem("Все имена");
        try {
            List<ClientEntity> list = ClientEntityManager.select();
            Set<String> names = new HashSet<>();
            for (ClientEntity c : list) {
                names.add(c.getFirstName());
            }
            for (String s : names) {
                nameComboBox.addItem(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.getFilters()[2] = new Predicate<ClientEntity>() {
            @Override
            public boolean test(ClientEntity clientEntity) {
                int selected = nameComboBox.getSelectedIndex();
                if(selected!=0){
                    return clientEntity.getFirstName().equals(nameComboBox.getSelectedItem());
                }else {return true;}
            }
        };
    }

    private void initElements() {
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.updateFilteredRows();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.updateFilteredRows();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                model.updateFilteredRows();
            }
        });


        nameComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    model.updateFilteredRows();
                }
            }
        });

        genderComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    model.updateFilteredRows();
                }
            }
        });
    }

    private void initButtons() {
        createButton.addActionListener(e -> {
            dispose();
            new CreateClientForm();
        });
        surnameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setSorter(new Comparator<ClientEntity>() {
                    @Override
                    public int compare(ClientEntity o1, ClientEntity o2) {
                        if (!surnameSort) {
                            return o1.getLastName().compareTo(o2.getLastName());
                        } else {
                            return o2.getLastName().compareTo(o1.getLastName());
                        }
                    }
                });
                surnameSort = !surnameSort;
                dateSort = false;
            }
        });

        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setSorter(new Comparator<ClientEntity>() {
                    @Override
                    public int compare(ClientEntity o1, ClientEntity o2) {
                        if (!dateSort) {
                            return o1.getBirthday().compareTo(o2.getBirthday());
                        } else {
                            return o2.getBirthday().compareTo(o1.getBirthday());
                        }
                    }
                });
                surnameSort = false;
                dateSort = !dateSort;
            }
        });

        resetButton.addActionListener(e -> {
            searchTextField.setText("");
            genderComboBox.setSelectedIndex(0);
            nameComboBox.setSelectedIndex(0);
            model.setSorter(null);
        });
        helpButton.addActionListener(e -> {
            Forms.infos(this, "Редактирование - двойное нажатие по строчке\nУдаление - в форме редактирования");
        });
        aboutButton.addActionListener(e -> {
            Forms.infos(this, "Для связи с разработчиком напишите на почту na3t1e@gmail.com");
        });
    }
}
