package com.ahnis.servaia.user.util;

import java.util.Arrays;

/**
 * Utility class that provides helper methods for working with enums.
 * This class contains static methods that assist in converting a string to an enum value.
 * <p>
 * It is not intended to be instantiated, as it contains only static methods.
 * The constructor throws an {@link UnsupportedOperationException} to prevent instantiation.
 * </p>
 *
 * <p><b>Important Note:</b> The methods in this class expect the input value to be non-null and non-empty.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *    MyEnum myEnum = EnumUtils.fromString(MyEnum.class, "VALUE");
 * </pre>
 *
 * @see Enum
 */
public final class EnumUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException If an attempt is made to instantiate this class.
     */
    private EnumUtils() {
        throw new UnsupportedOperationException("Cannot initialise Utility class");
    }

    /**
     * Converts a string to the corresponding enum constant of the specified enum type.
     * <p>
     * This method will trim and convert the input string to uppercase before attempting the conversion.
     * If the string does not match any enum constant, an {@link IllegalArgumentException} will be thrown.
     * </p>
     *
     * @param <T> The enum type.
     * @param enumType The class object of the enum type.
     * @param value The string to convert.
     * @return The enum constant corresponding to the given string value.
     * @throws IllegalArgumentException If the input string is null, empty, or does not match any enum constant.
     */
    public static <T extends Enum<T>> T fromString(Class<T> enumType, String value) {
        //Simpler terms : for global enum exception handling while json parsing :)
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Input value cannot be null or empty.");
        }
        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            //Throwing Exception with better message
            throw new IllegalArgumentException(
                    "Invalid value for enum '" + enumType.getSimpleName() + "': '" + value + "'. Expected one of: " +
                            Arrays.toString(enumType.getEnumConstants())
            );
        }
    }
}
