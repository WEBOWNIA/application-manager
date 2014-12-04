package net.webownia.applicationmgr.shared.enums;

import java.util.EnumSet;

/**
 * Created by abarczewski on 2014-12-03.
 */
public enum ApplicationStage {
    CREATED,
    DELETED,
    VERIFIED,
    REJECTED,
    ACCEPTED,
    PUBLISHED;

    public static final EnumSet<ApplicationStage> stagesForChangeContent = EnumSet.of(CREATED, VERIFIED);
    public static final EnumSet<ApplicationStage> allStages = EnumSet.of(CREATED, DELETED, VERIFIED, REJECTED, ACCEPTED, PUBLISHED);

    public static ApplicationStage getByName(String name) {
        return ApplicationStage.valueOf(name);
    }
}
