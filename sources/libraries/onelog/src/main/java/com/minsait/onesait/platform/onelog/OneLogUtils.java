package com.minsait.onesait.platform.onelog;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * OneLog static methods.
 * 
 * @author erperez
 */
public final class OneLogUtils {

	private static final String UNKNOWN_HOSTNAME = "Unknown hostname";

	private OneLogUtils() {
		// Private constructor
	}

	/**
	 * Returns the computer host name.
	 * 
	 * @return Host name
	 */
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (final UnknownHostException exception) {
			return UNKNOWN_HOSTNAME;
		}
	}

	/**
	 * Returns a double value in US format #.###.
	 * 
	 * @param decimal Double value.
	 * @return String containing the double value.
	 */
	public static String formatToUSDecimalFormat(final double decimal) {
		final DecimalFormat decimalFormat = new DecimalFormat("#.###",
				DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		return decimalFormat.format(decimal);
	}

	/**
	 * Returns a TrustManager which always trust the certificates.
	 * @return {@code TrustManager}.
	 */
	public static TrustManager[] buildNoopTrustManagers() {
		return new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
			}

			public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
			}
		}, };
	}

}
