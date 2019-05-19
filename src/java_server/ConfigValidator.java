package java_server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;

class ConfigValidator {
	private Map<String, String> keyToRegex = null;

	ConfigValidator(Map<String, String> regexMap) throws ValidationException {
		this.keyToRegex = regexMap;
	}

	ConfigValidator(String[] configKeys, String[] regex) throws ValidationException {
		this.keyToRegex = convertToMap(configKeys, regex);
	}

	ConfigValidator() {
	}

	/**
	 * Validate a String with a RegularExpressions Pattern
	 * 
	 * @param string       String to validate
	 * @param regexPattern Regular Expressions Pattern to use for validation
	 * @throws ValidationException
	 */
	void validateString(String string, String regexPattern) throws ValidationException {
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matString = pattern.matcher(string);
		boolean check = matString.find();
		if (!check || !matString.group(0).equals(string)) {
			throw new ValidationException(
					"Validation unsuccessful for String: \"" + string + "\" in Pattern: " + regexPattern, 1);
		}
	}

	/**
	 * Validates certain values in a property by using a regexmap which is mapping
	 * keys of property to the regular Expression the value of this key should be
	 * validated with.
	 * 
	 * @param property Property to be Validated
	 * @throws ValidationException
	 */
	void validateProperty(Properties property) throws ValidationException {
		if (keyToRegex == null || keyToRegex.isEmpty()) {
			throw new ValidationException("keyToRegex Map was null or empty", 3);
		}
		for (Entry<String, String> entry : this.keyToRegex.entrySet()) {
			Pattern configPattern = Pattern.compile(entry.getValue());
			String toValidate = property.getProperty(entry.getKey());
//			System.out.println(configPattern.toString() + " matches Type: -" + entry.getKey() + "- with String: " + toValidate);
			Matcher mat = configPattern.matcher(toValidate);
			boolean check = mat.find();
			if (!check || !(mat.group(0).equals(toValidate))) {
				throw new ValidationException(
						"Validation unsuccessful for String: \"" + toValidate + "\" in Key: " + entry.getKey(), 1);
			}
		}
	}

	/**
	 * Converts 2 String arrays to a map
	 * 
	 * @param configKeys Keys of Property where values are to be validated
	 * @param regex      Regular Expressions the values of keys in configKeys should
	 *                   be validated with
	 * 
	 * @return Map mapped configKeys[] as key --> regex[] as value
	 * @throws ValidationException
	 */
	private Map<String, String> convertToMap(String[] configKeys, String[] regex) throws ValidationException {
		Map<String, String> convertedMap = new HashMap<String, String>();
		if (configKeys.length == regex.length) {
			for (int i = 0; i < configKeys.length; i++) {
				convertedMap.put(configKeys[i], regex[i]);
			}
			return convertedMap;
		} else {
			throw new ValidationException(
					"ConfigValidator.convertToMap(): String Arrays for Configuration Keys and Configuration Regex did not match in size!",
					2);
		}
	}
}
