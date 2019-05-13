package java_server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;

class ConfigValidator {
	private Map<String, String> keyToRegex = null;
	
	public ConfigValidator(Map<String, String> regexMap) throws ValidationException {
		this.keyToRegex = regexMap;
	}
	
	public ConfigValidator(String[] configKeys, String[] regex) throws ValidationException {
		this.keyToRegex = convertToMap(configKeys, regex);
	}

	public void validateProperty(Properties property) throws ValidationException {
		if(keyToRegex == null || keyToRegex.isEmpty()) {
			throw new ValidationException("keyToRegex Map was null or empty", 3);
		}
		for (Entry<String, String> entry : this.keyToRegex.entrySet()) {
			Pattern configPattern = Pattern.compile(entry.getValue());
			String toValidate = property.getProperty(entry.getKey());
			Matcher mat = configPattern.matcher(toValidate);
			mat.find();
			if (!(mat.group(0).equals(toValidate))) {
				throw new ValidationException("Validation unssuccesful for key" + toValidate, 1);
			}
		}
	}

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
