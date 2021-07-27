package com.impacttechs.assignment.pbxintegrationservice.constants;

public final class ExceptionConstants {
    private ExceptionConstants(){}

    public static final String ERROR_RESPONSE_NULL = "No Response from Api";
    public static final String ERROR_NOT_OK_RESPONSE = "Response code %s received. Not a 2xx response type";
    public static final String ERROR_NOTFOUND_VALID_RESPONSE = "No Tenant-UUID found in pbx api response";
}
