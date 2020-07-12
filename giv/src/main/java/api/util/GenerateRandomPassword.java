package api.util;

import java.security.SecureRandom;

/**
 * Generates random password for the external API user.
 *
 * @author : Thamilarasi
 * @version : 1.0
 * @since : 2019-05-06
 */
public class GenerateRandomPassword {
	private static SecureRandom random = new SecureRandom();
	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC = "0123456789";
	private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
	private static final int len = 15;
	private static final String dic = ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL_CHARS;

	public static final String generatePassword() {
		String result = "";

		for (int i = 0; i < len; i++) {
			int index = random.nextInt(dic.length());
			result += dic.charAt(index);
		}
		return result;
	}
}