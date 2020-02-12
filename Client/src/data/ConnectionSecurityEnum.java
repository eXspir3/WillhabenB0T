/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 * Enum for Email Connection security type
 * 
 * @author Frank Weber
 */
public enum ConnectionSecurityEnum {
	NONE, STARTTLS, SSLTLS;

	private final static String[] text = { "None", "STARTTLS", "SSL/TLS" };

	@Override
	public String toString() {
		return text[ordinal()];
	}
}
