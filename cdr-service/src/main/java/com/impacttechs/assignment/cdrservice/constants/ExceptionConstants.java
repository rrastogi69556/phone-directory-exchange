package com.impacttechs.assignment.cdrservice.constants;

public final class ExceptionConstants {
    private ExceptionConstants(){}

    public static final String INVALID_DATA = "Unprocessable entity. Client data error!";
    public static final String ERROR_OCCURRED_UPDATING_CDR_RECORD = "Error occurred while updating CDR record!";
    public static final String ERROR_OCCURRED_FETCHING_CDR_RECORD = "Error occurred while fetching CDR record!";
    public static final String ERROR_OCCURRED_DELETING_CDR_RECORD = "Error occurred while deleting CDR record!";
    public static final String CALL_DATA_RECORD_NOT_FOUND = "Call Data records not found!";
    public static final String ERROR_RESPONSE_NULL = "No Response from Api";
    public static final String ERROR_NOT_OK_RESPONSE = "Response code %s received. Not a 2xx response type";
    public static final String ERROR_NOTFOUND_VALID_RESPONSE = "No Tenant-UUID found in pbx api response";
}
