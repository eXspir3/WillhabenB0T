/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 * 
 * @author Frank Weber
 */
public class SearchBot {

	private final String name, link;
	private final ArrayList<String> emails;
	private final int interval;

	public SearchBot(String name, String link, int interval, ArrayList<String> emails) {
		this.name = name;
		this.link = link;
		this.interval = interval;
		this.emails = emails;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	public int getInterval() {
		return interval;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public String getEmailsAsString() {
		StringBuilder string = new StringBuilder();
		for (String email : emails) {
			string.append(email + ",");
		}
		return string.toString();
	}
}
