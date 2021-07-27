package com.impacttechs.assignment.cdrservice.constants;

public final class ApiUrlConstants {
    private ApiUrlConstants() {}

    public static final String GET_CDR_URL = "/cdr";
    public static final String GET_CDR_BY_TENANT_UUID = "/get";
    public static final String POST_CDR_BY_TENANT_UUID = "/post";
    public static final String PUT_CDR_BY_TENANT_UUID = "/put";
    public static final String DELETE_CDR_BY_TENANT_UUID = "/delete";
    public static final String GET_CDR_REPORT_BY_TENANT_UUID = "/report";
    public static final String GET_CDR_REPORTS_FOR_ALL_TENANTS = "/reports";


    /* PBX WEBHOOK API URLs*/

    public static final String PBX_URL = "/pbx";
    public static final String TRIGGER_WEBHOOK_URL = "/trigger_webhooks";
    public static final String GET_WEBHOOK_URL = "/get_cdr";
    public static final String POST_CDR_BY_PBX_WEBHOOK_TRIGGER_CRM_URL = "/update_cdr";

    /* Phone Book Directory API URLs */
    public static final String GET_PHONE_BOOK_URL = "/phone_book";
    public static final String PHONE_BOOK_RECORD = "/records";

}
