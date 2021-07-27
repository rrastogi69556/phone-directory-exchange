package com.impacttechs.assignment.cdrservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacttechs.assignment.cdrservice.abstraction.ICDRService;
import com.impacttechs.assignment.cdrservice.entity.CallDataRecord;
import com.impacttechs.assignment.cdrservice.entity.MultiTenant;
import com.impacttechs.assignment.cdrservice.exception.CdrServiceException;
import com.impacttechs.assignment.cdrservice.helper.CDRDaoHelper;
import com.impacttechs.assignment.cdrservice.model.CDRReport;
import com.impacttechs.assignment.cdrservice.model.CallDataRecordWithTenant;
import com.impacttechs.assignment.cdrservice.model.PbxCDR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.impacttechs.assignment.cdrservice.constants.ApiUrlConstants.*;
import static com.impacttechs.assignment.cdrservice.constants.ExceptionConstants.*;
import static com.impacttechs.assignment.cdrservice.constants.GeneralConstants.*;
import static com.impacttechs.assignment.cdrservice.utils.DateUtils.convertDateToLocalDateTime;
import static com.impacttechs.assignment.cdrservice.utils.Utility.throwErrorIfInvalidResponse;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
@Slf4j
public class CDRService implements ICDRService {


    @Value("${impacttechs.crm.pbx.webhook.api.url:http://localhost:9901}")
    private String webhookTriggerUrl;

    @Value("${impacttechs.crm.pbx.phonebook.api.url}")
    private String phoneBookApiUrl;

    private final RestTemplate loadBalancedRestTemplate;
    private final CDRDaoHelper helper;
    private final ObjectMapper mapper;

    @Autowired
     public CDRService(@LoadBalanced RestTemplate loadBalancedRestTemplate, CDRDaoHelper helper, ObjectMapper mapper) {
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.helper = helper;
        this.mapper = mapper;
    }

    public PbxCDR getAllRecordsFromPBXExchangeWebhook() throws CdrServiceException, IOException {
        ResponseEntity<String> fetchAllRecords = fetchCallDataRecordsFromExternalPbxApi();
        PbxCDR cdr = mapper.readValue(fetchAllRecords.getBody(), PbxCDR.class);
        if (nonNull(cdr)  && nonNull(cdr.getTenantUUID())) {
            return cdr;
        }

        log.error(ERROR_NOTFOUND_VALID_RESPONSE);
        return null;
    }

    private ResponseEntity<String> fetchCallDataRecordsFromExternalPbxApi() throws CdrServiceException {
        ResponseEntity<String> fetchAllRecords = loadBalancedRestTemplate.exchange(webhookTriggerUrl.concat(PBX_URL).concat(GET_WEBHOOK_URL), HttpMethod.GET, null, String.class);
        log.debug("Response returned: [ {} ] ", fetchAllRecords.getBody());

        throwErrorIfInvalidResponse(fetchAllRecords, log);
        return fetchAllRecords;
    }

    public String updateAllCDRRecords() throws CdrServiceException, IOException {
        //1. trigger webhook of external api
        triggerPbxWebhookUrl();

        //2. get all the records from pbx
        PbxCDR cdrFromPbxExchange  = getAllRecordsFromPBXExchangeWebhook();

        if (isNull(cdrFromPbxExchange)) {
            return ERROR_NEW_CDR_RECORD;
        }

        //3. Update the caller name
        updateCallerNameInNewRecords(cdrFromPbxExchange);

        //4. filter only new ones or create all if none exists.once updated return the status.
        return updateNewRecordsInDB(cdrFromPbxExchange);
    }

    private void triggerPbxWebhookUrl() throws CdrServiceException {
        ResponseEntity<String> responseEntity = loadBalancedRestTemplate.exchange(webhookTriggerUrl.concat(PBX_URL).concat(TRIGGER_WEBHOOK_URL), HttpMethod.GET, null, String.class);
        throwErrorIfInvalidResponse(responseEntity, log);
    }

    private String updateNewRecordsInDB(PbxCDR cdrFromPbxExchange) {
        if( isNotEmpty(cdrFromPbxExchange.getData())) {
            helper.updateRecordsIfNotExist(cdrFromPbxExchange.getData(), cdrFromPbxExchange.getTenantUUID());
            return SUCCESS_CDR_RECORD_UPDATED;
        }
        return NO_UPDATION;
    }

    private void updateCallerNameInNewRecords(PbxCDR callDataRecordWithTenants) throws CdrServiceException, IOException {
        Map<String, String> phoneToNameMap = fetchPhoneBookRecordsFromPhoneBookServiceApi();
        List<CallDataRecordWithTenant> callDataRecordsFromPBX = callDataRecordWithTenants.getData();

        callDataRecordsFromPBX = callDataRecordsFromPBX.stream()
                .filter(cdr -> phoneToNameMap.containsKey(String.valueOf(cdr.getCallerNumber())))
                .map(cdr -> updateNameAndGetTenant(phoneToNameMap.get(String.valueOf(cdr.getCallerNumber())), cdr))
                .collect(Collectors.toList());

        callDataRecordWithTenants.setData(callDataRecordsFromPBX);
    }

    private Map<String, String> fetchPhoneBookRecordsFromPhoneBookServiceApi() throws IOException, CdrServiceException {
        ResponseEntity<String> phoneBookList = loadBalancedRestTemplate.exchange(phoneBookApiUrl.concat(GET_PHONE_BOOK_URL).concat(PHONE_BOOK_RECORD), HttpMethod.GET, null, String.class);
        log.debug("records are : {} ", phoneBookList);
        throwErrorIfInvalidResponse(phoneBookList, log);
        List<LinkedHashMap<String, String>> phoneBookRecords = (List<LinkedHashMap<String, String>>)mapper.readValue(phoneBookList.getBody(), List.class);
        Map<String, String> phoneToNameMap = new HashMap<>();
        phoneBookRecords.forEach(record -> phoneToNameMap.put(record.get("callerNumber"), record.get("name")));
        return phoneToNameMap;
    }

    private CallDataRecordWithTenant updateNameAndGetTenant(String phoneToName, CallDataRecordWithTenant tenant) {
        tenant.setCallerName(phoneToName);
        return tenant;
    }

    public List<CallDataRecord> getAllCdrByTenantId(String customerId) throws CdrServiceException {
        return helper.getAllCDRsByTenantId(customerId);
    }

    public String createCallDataRecords(String customerId, List<CallDataRecordWithTenant> callDataRecords) throws CdrServiceException {
        return helper.createCallDataRecords(customerId, callDataRecords);
    }

    public String deleteCallDataRecord(String customerId, String recording) throws CdrServiceException {
        return helper.deleteCallDataRecord(customerId,  recording) ;
    }

    public CDRReport generateReportByTenant(String customerId) throws CdrServiceException {
        Optional<MultiTenant> tenant = helper.findTenantIfPresent(customerId);
        if(tenant.isPresent()) {
            return generateCallReportForTenant(tenant.get());
        } else {
            throw new CdrServiceException(CALL_DATA_RECORD_NOT_FOUND);
        }
    }

    public List<CDRReport> generateReports() throws CdrServiceException {
        List<MultiTenant> tenants = helper.findAllTenants();
        List<CDRReport> reportsPerTenant = new LinkedList<>();
        for(MultiTenant tenant : tenants) {
            reportsPerTenant.add(generateCallReportForTenant(tenant));
        }
        return reportsPerTenant;
    }

    private CDRReport generateCallReportForTenant(MultiTenant tenant) throws CdrServiceException {
        try {
            List<CallDataRecord> recordList = tenant.getCallDataRecords();
            CDRReport report = new CDRReport();
            setTotalNumberOfCalls(recordList, report);
            setTotalSuccessfulCallsInMinutes(recordList, report);
            setTotalInCompleteCallsInMinutes(recordList, report);
            setTotalUnMatchedCallsInMinutes(recordList, report, tenant);
            setLastSyncDate(tenant, report);
            return report;

        } catch (Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_FETCHING_CDR_RECORD, e);
        }
    }

    private void setTotalSuccessfulCallsInMinutes(List<CallDataRecord> recordList, CDRReport report) {
        List<CallDataRecord> successfulCalls = recordList.stream().filter(record -> nonNull(record.getAnswerStart())).collect(toList());
        if(isNotEmpty(successfulCalls)) {
            long totalSuccessfulCalls = successfulCalls.stream().map(record -> (Duration.between(convertDateToLocalDateTime(record.getCallStart()), convertDateToLocalDateTime(record.getCallEnd()))).toMinutes()).reduce(ZERO, Long::sum);
            report.setTotalSuccessfulCallInMinutes(totalSuccessfulCalls);
        }
    }

    private void setTotalInCompleteCallsInMinutes(List<CallDataRecord> recordList, CDRReport report) {
        List<CallDataRecord> incompleteCalls = recordList.stream().filter(record -> isNull(record.getAnswerStart())).collect(toList());
        if(isNotEmpty(incompleteCalls)) {
            long totalInCompleteCalls = incompleteCalls.stream().map(record -> (Duration.between(convertDateToLocalDateTime(record.getCallStart()), convertDateToLocalDateTime(record.getCallEnd()))).toMinutes()).reduce(ZERO, Long::sum);
            report.setTotalIncompleteCallInMinutes(totalInCompleteCalls);
        }
    }

    private void setTotalNumberOfCalls(List<CallDataRecord> recordList, CDRReport report) {
        int totalNoOfCalls = recordList.size();
        report.setTotalNumberOfCalls(totalNoOfCalls);
    }

    private void setTotalUnMatchedCallsInMinutes(List<CallDataRecord> recordList, CDRReport report, MultiTenant tenant) throws IOException, CdrServiceException {
        PbxCDR recentCallsFromPbx = getAllRecordsFromPBXExchangeWebhook();
        List<CallDataRecord> convertedRecordsFromPbx = helper.populateCallDataRecordsEntity(tenant, recentCallsFromPbx.getData());
        boolean isRemoved = recordList.removeAll(convertedRecordsFromPbx);
        if(isRemoved) {
            long unMatchCalls = recordList.stream().map(record -> (Duration.between(convertDateToLocalDateTime(record.getCallStart()), convertDateToLocalDateTime(record.getCallEnd()))).toMinutes()).reduce(ZERO, Long::sum);
            report.setTotalUnMatchedCallInMinutes(unMatchCalls);
        }
    }

    private void setLastSyncDate(MultiTenant tenant, CDRReport report) {
        report.setLatestSyncDate(tenant.getLastUpdated());
    }
}
