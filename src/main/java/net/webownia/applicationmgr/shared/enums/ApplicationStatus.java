package net.webownia.applicationmgr.shared.enums;

import java.util.EnumSet;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
public enum ApplicationStatus {
    CREATED,
    DELETED,
    VERIFIED,
    REJECTED,
    ACCEPTED,
    PUBLISHED;

    public static final EnumSet<ApplicationStatus> stagesForRejecting = EnumSet.of(VERIFIED, ACCEPTED);
    public static final EnumSet<ApplicationStatus> allStages = EnumSet.of(CREATED, DELETED, VERIFIED, REJECTED, ACCEPTED, PUBLISHED);

    public static ApplicationStatus getByName(String name) {
        return ApplicationStatus.valueOf(name);
    }
}
