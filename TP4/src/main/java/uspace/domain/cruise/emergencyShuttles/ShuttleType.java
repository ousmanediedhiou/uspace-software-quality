package uspace.domain.cruise.emergencyShuttles;

import uspace.domain.cruise.emergencyShuttles.exceptions.InvalidShuttleTypeException;

public enum ShuttleType {
    STANDARD_SHUTTLE("standard_shuttle"),
    RESCUE_SHIP("rescue_ship"),
    SENIOR_SHUTTLE("senior_shuttle");

    private final String type;

    ShuttleType(String type) {
        this.type = type;
    }

    public static ShuttleType fromString(String type) {
        for (ShuttleType shuttleType : ShuttleType.values()) {
            if (shuttleType.type.equalsIgnoreCase(type)) {
                return shuttleType;
            }
        }

        throw new InvalidShuttleTypeException();
    }

}
