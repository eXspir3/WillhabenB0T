/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.data;

import java.util.ArrayList;

/**
 * Handles instances of SearchBot for the Client GUI
 * @author Frank Weber
 */
public class SearchBots {

    private final ArrayList<SearchBot> searchBots = new ArrayList<>();

    public int size() {
        return searchBots.size();
    }

    public void addNew(SearchBot searchBot)
            throws Exception {
        for (int i = 0; i < searchBots.size(); i++) {
            if (searchBots.get(i).getName().equals(searchBot.getName())) {
                throw new Exception("A searchbot with the name \"" + searchBot.getName() + "\" already exists!");
            }
        }
        searchBots.add(searchBot);
    }

    public SearchBot get(int index)
            throws Exception {
        if (searchBots.size() <= index) {
            throw new Exception("Searchbot with ID " + index + " not found!");
        }
        return searchBots.get(index);
    }

    public SearchBot get(String name)
            throws Exception {
        for (int i = 0; i < searchBots.size(); i++) {
            if (searchBots.get(i).getName().equals(name)) {
                return searchBots.get(i);
            }
        }
        throw new Exception("Searchbot with name " + name + " not found!");
    }

    public void remove(int index)
            throws Exception {
        if (searchBots.size() <= index) {
            throw new Exception("Searchbot with ID " + index + " not found!");
        }
        searchBots.remove(index);
    }

    public void set(int index, SearchBot searchbot)
            throws Exception {
        if (searchBots.size() <= index) {
            throw new Exception("Searchbot with ID " + index + " not found!");
        }
        searchBots.set(index, searchbot);
    }

}
