package org.whirlplatform.meta.shared.version;

import org.whirlplatform.meta.shared.Version;

/**
 * Helps to work with the Version objects
 * 
 * @author bedritckiy_mr
 *
 */
public class VersionUtil {
	public static final Version NULL_VERSION = Version.create(0, 0, Integer.MAX_VALUE);

	/**
	 * Creates and initialises new Version object
	 * 
	 * @param value
	 *            - the string representation of the version
	 * @return new instance of Version
	 */
	public static Version createVersion(final String value) {
		return (Version.isValidVersion(value)) ? Version.parseVersion(value) : Version.create(value);
	}

	/**
	 * Creates and initialises new Version object
	 * 
	 * @param version
	 * @return new instance of Version
	 */
	public static Version createVersion(final Version version) {
		return createVersion(version.toString());
	}

	/**
	 * Recognises and returns the version type of the version object
	 * 
	 * @param version
	 * @return the version type
	 */
	public static VersionType type(final Version version) {
		return (version.isBranch()) ? VersionType.BRANCH : VersionType.VERSION;
	}

	/**
	 * Gets the folder name of the version object
	 * 
	 * @param version
	 *            - given version of the application
	 * @return the folder name
	 */
	public static String typeFolderName(final Version version) {
		return type(version).folderName();
	}

	/**
	 * Builds url to the application
	 * 
	 * @param parent
	 *            - the parent url to the application
	 * @param version
	 *            - given version of the application
	 * @return url
	 */
	public static String buildUrl(final String parent, final Version version) {
		StringBuilder sb = new StringBuilder();
		if (parent != null) {
			if (!"".equals(parent)) {
				sb.append(parent);
				if (!parent.endsWith("/")) {
					sb.append("/");
				}
			}
		}
		if (version != null) {
			sb.append(VersionUtil.typeFolderName(version));
			sb.append("/").append(version.toString());
		}
		return sb.toString();
	}

	/**
	 * Builds version url
	 * 
	 * @param version
	 *            - given version of the application
	 * @return url
	 */
	public static String buildUrl(final Version version) {
		StringBuilder sb = new StringBuilder();
		if (version != null) {
			sb.append(VersionUtil.typeFolderName(version));
			sb.append("/").append(version.toString());
		}
		return sb.toString();
	}

	/**
	 * Сравнивает Version объекты по строковым значениям
	 * 
	 * @param version1
	 *            - первый Version объект
	 * @param version2
	 *            - второй Version объект
	 * @return true, строковые значения version1 and version2 равны или нулевые 
	 */
	public static boolean stringEquals(final Version version1, final Version version2) {
		if (version1 == null) {
			if(version2 == null) {
				return true;
			}
		} else if(version2 == null) {
			return false;
		}
		return version1.toString().toLowerCase().equals(version2.toString().toLowerCase());
	}

	/**
	 * Если version равно null, возвращает объект NULL_VERSION
	 */
	public static Version ensureNotNull(Version version) {
		return (version == null) ? NULL_VERSION : version;
	}

	/**
	 * Если version равно NULL_VERSION, возвращает null
	 */
	public static Version originalVersion(Version version) {
		if (version == null) {
			return null;
		}
		return stringEquals(version, NULL_VERSION) ? null : version;
	}
}
