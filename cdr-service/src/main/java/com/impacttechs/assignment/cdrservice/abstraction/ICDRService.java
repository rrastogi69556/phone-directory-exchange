package com.impacttechs.assignment.cdrservice.abstraction;

import com.impacttechs.assignment.cdrservice.entity.CallDataRecord;
import com.impacttechs.assignment.cdrservice.exception.CdrServiceException;
import com.impacttechs.assignment.cdrservice.model.CDRReport;
import com.impacttechs.assignment.cdrservice.model.CallDataRecordWithTenant;
import com.impacttechs.assignment.cdrservice.model.PbxCDR;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.singletonList;


public interface ICDRService {
    PbxCDR getAllRecordsFromPBXExchangeWebhook() throws CdrServiceException, IOException;

    String updateAllCDRRecords() throws CdrServiceException, IOException;

    List<CallDataRecord> getAllCdrByTenantId(String customerId) throws CdrServiceException;

    String createCallDataRecords(String customerId, List<CallDataRecordWithTenant> callDataRecords) throws CdrServiceException;

    String deleteCallDataRecord(String customerId, String recording) throws CdrServiceException;

    CDRReport generateReportByTenant(String customerId) throws CdrServiceException;

    List<CDRReport> generateReports() throws CdrServiceException;

    default String insertCallDataRecord(String customerId, CallDataRecordWithTenant callDataRecordWithTenants) throws CdrServiceException {
        return createCallDataRecords(customerId, singletonList(callDataRecordWithTenants));
    }
}
