package com.uob.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * This class file defines all the generic re-usable methods across any application.
 * @author DeepikaGuduru
 * @version 1.0
 * @created April-13-2019 
 * 
 */

public class CommonUtil {

	Properties properties;

	public String getProperty(String value) {
		properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("src/test/resources/config.properties");
			properties.load(inputStream);
			return properties.getProperty(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "Given property is invalid.";
	}
	
	public byte[] getEncryptedMessage(String value) {
		byte[] encryptPassword = Base64.encodeBase64(value.getBytes());
		System.out.println("Encrypted Password: " + encryptPassword);
		return encryptPassword;
	}
	
	public String getDecryptedMessage(byte[] value) {
		byte[] valueDecoded= Base64.decodeBase64(value);
		return new String(valueDecoded);
	}

}
