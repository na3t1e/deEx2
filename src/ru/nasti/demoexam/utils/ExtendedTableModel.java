package ru.nasti.demoexam.utils;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public abstract class ExtendedTableModel<T> extends AbstractTableModel {
    private Class<T> cls;
    private String[] columnNames;

    private List<T> allRows = new ArrayList<>();
    private List<T> filteredRows;

    private Predicate<T>[] filters = new Predicate[]{null, null, null, null, null, null};
    private Comparator<T> sorter;

    public ExtendedTableModel(Class<T> cls, String[] columnNames) {
        this.cls = cls;
        this.columnNames = columnNames;
    }

    public void updateFilteredRows() {
        filteredRows = new ArrayList<>(allRows);
        for (Predicate<T> filter : filters) {
            if (filter != null) {
                filteredRows.removeIf(row -> !filter.test(row));
            }
        }
        if (sorter != null) {
            Collections.sort(filteredRows, sorter);
        }
        fireTableDataChanged();
        onUpdateRowsEvent();
    }

    public void onUpdateRowsEvent() {
    }

    @Override
    public int getRowCount() {
        return filteredRows.size();
    }

    @Override
    public int getColumnCount() {
        return cls.getDeclaredFields().length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Field field = cls.getDeclaredFields()[columnIndex];
            field.setAccessible(true);
            return field.get(this.filteredRows.get(rowIndex));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public List<T> getAllRows() {
        return allRows;
    }

    public void setAllRows(List<T> allRows) {
        this.allRows = allRows;
        this.updateFilteredRows();
    }

    public List<T> getFilteredRows() {
        return filteredRows;
    }

    public void setFilteredRows(List<T> filteredRows) {
        this.filteredRows = filteredRows;
    }

    public Comparator<T> getSorter() {
        return sorter;
    }

    public void setSorter(Comparator<T> sorter) {
        this.sorter = sorter;
        this.updateFilteredRows();
    }

    public Predicate<T>[] getFilters() {
        return filters;
    }

    public void setFilters(Predicate<T>[] filters) {
        this.filters = filters;
    }

    public abstract void onUpdateRowEvents();
}
