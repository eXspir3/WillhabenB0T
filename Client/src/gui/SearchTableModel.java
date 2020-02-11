/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.data.SearchBots;
import javax.swing.table.AbstractTableModel;

/**
 * Tablemodel for the searchbot table
 * @author Frank Weber
 */
public class SearchTableModel extends AbstractTableModel {

    private final SearchBots searchBots;

    public SearchTableModel(SearchBots searchBots) {
        this.searchBots=searchBots;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Searchbot";
            case 1:
                return "Search Interval";
            case 2:
                return "Emails";
            default:
                return "NameNotFound";
        }
    }

    @Override
    public int getRowCount() {
        return searchBots.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        try {
            switch (columnIndex) {
                case 0:
                    return searchBots.get(rowIndex).getName();
                case 1:
                    return searchBots.get(rowIndex).getInterval()+" sec";
                case 2:
                    return searchBots.get(rowIndex).getEmailsAsString();
                default:
                    return "Should not see me";
            }
            
        } catch (Exception e) {
            return "Could not read Botconfig!";
        }
    }

}
