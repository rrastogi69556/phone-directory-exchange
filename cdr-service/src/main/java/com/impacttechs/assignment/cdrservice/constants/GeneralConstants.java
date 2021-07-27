package com.impacttechs.assignment.cdrservice.constants;

public final class GeneralConstants {
    private GeneralConstants(){}

    public static final String ERROR_NEW_CDR_RECORD = "Unable to update new record probably due to error in fetching response from external api";
    public static final String NO_UPDATION = "No Updation. Probably no CDR/Tenant found to update!";
    public static final String SUCCESS_CDR_RECORD_UPDATED = "CDR Record updated!!!";
    public static final String SUCCESS_CDR_RECORD_DELETED = "CDR Record deleted!!!";
    public static final String TENANT_UUID = "tenant-uuid";
    public static final String ISO_INSTANT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final Long ZERO = 0L;
}
