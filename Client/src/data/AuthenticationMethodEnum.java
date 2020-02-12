/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 * Enum for Email Authentication methods
 * 
 * @author Frank Weber
 */
public enum AuthenticationMethodEnum {
	NONE, NORMAL;

	private final static String[] text = { "No Authentication", "Password, normal", };

	@Override
	public String toString() {
		return text[ordinal()];
	}
}
