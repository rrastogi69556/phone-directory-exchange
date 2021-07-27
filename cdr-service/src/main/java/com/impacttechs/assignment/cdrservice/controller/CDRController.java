package com.impacttechs.assignment.cdrservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacttechs.assignment.cdrservice.abstraction.ICDRService;
import com.impacttechs.assignment.cdrservice.entity.CallDataRecord;
import com.impacttechs.assignment.cdrservice.exception.CdrServiceException;
import com.impacttechs.assignment.cdrservice.model.CDRReport;
import com.impacttechs.assignment.cdrservice.model.CallDataRecordWithTenant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.impacttechs.assignment.cdrservice.constants.ApiUrlConstants.*;
import static com.impacttechs.assignment.cdrservice.constants.GeneralConstants.TENANT_UUID;


@RestController
@RequestMapping(GET_CDR_URL)
@Api(value = "Call Data Records API Service of CRM. This API stores, returns, updates, deletes call data records")
public class CDRController {
    private static final String API_UPDATE_TAG = "CALL_DATA_RECORD_UPDATE_API";
    private static final String API_FETCH_TAG = "CALL_DATA_RECORD_FETCH_API";
    private static final String API_CREATE_TAG = "CALL_DATA_RECORD_CREATE_API";
    private static final String API_PUT_TAG = "CALL_DATA_RECORD_PUT_API";
    private static final String API_DELETE_TAG = "CALL_DATA_RECORD_DELETE_API";


    private final ICDRService cdrService;
    private final ObjectMapper mapper;

    @Autowired
    public CDRController(ICDRService service, ObjectMapper mapper) {
        this.cdrService = service;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Returns success/failed response as String", response = String.class, tags = {API_UPDATE_TAG}, produces = "Application/JSON", httpMethod = "POST")
    @PostMapping(POST_CDR_BY_PBX_WEBHOOK_TRIGGER_CRM_URL)
    public ResponseEntity<String> updateCdrRecordsFromWebhook() throws Exception {

        String content = cdrService.updateAllCDRRecords();
        return ResponseEntity.ok().body(mapper.writeValueAsString(content));
    }

    @ApiOperation(value = "Returns Call Data records as a List for a given customer", response = List.class, tags = {API_FETCH_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "Represents existing customer. Mandatory.", required = true, dataType = "java.lang.String", paramType = "header"),
    })
    @GetMapping(GET_CDR_BY_TENANT_UUID)
    public ResponseEntity<List<CallDataRecord>> getCallDataRecordsByTenantUuid(@RequestHeader(value = TENANT_UUID) String customerId) throws CdrServiceException {

        List<CallDataRecord> contents = cdrService.getAllCdrByTenantId(customerId);

        return ResponseEntity.ok().body(contents);
    }

    @ApiOperation(value = "Returns success/failure response for creating call data records in Directory", response = String.class, tags = {API_CREATE_TAG}, produces = "Application/JSON", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "Represents existing customer. Mandatory.", required = true, dataType = "java.lang.String", paramType = "header"),
            @ApiImplicitParam(name = "callDataRecords", value = "Represents List of call data records of a customer.", dataType = "java.util.List", paramType = "query"),
    })
    @PostMapping(POST_CDR_BY_TENANT_UUID)
    public ResponseEntity<String> createCallDataRecords(@RequestHeader(value = TENANT_UUID) String customerId,
                                                        @RequestBody(required = false)List<CallDataRecordWithTenant> callDataRecords) throws Exception {

        String content = cdrService.createCallDataRecords(customerId, callDataRecords);

        return ResponseEntity.ok().body(mapper.writeValueAsString(content));
    }


    @ApiOperation(value = "Returns success/failed response", response = String.class, tags = {API_PUT_TAG}, produces = "Application/JSON", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "Represents existing customer. Mandatory.", required = true, dataType = "java.lang.String", paramType = "header"),
            @ApiImplicitParam(name = "callDataRecord", value = "Represents call data record of a customer in json as an object.", dataType = "com.impacttechs.assignment.cdrservice.model.CallDataRecordWithTenant", paramType = "query"),
    })
    @PutMapping(PUT_CDR_BY_TENANT_UUID)
    public ResponseEntity<String> storePhoneBookRecords(@RequestHeader(value = TENANT_UUID) String customerId,
                                                        @RequestBody(required = false)CallDataRecordWithTenant callDataRecords) throws Exception {


        String content =  cdrService.insertCallDataRecord(customerId, callDataRecords);

        return ResponseEntity.ok().body(mapper.writeValueAsString(content));
    }

    @ApiOperation(value = "Returns success/failed response", response = String.class, tags = {API_DELETE_TAG}, produces = "Application/JSON", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant-uuid", value = "Represents existing customer. Mandatory.", required = true, dataType = "java.lang.String", paramType = "header"),
            @ApiImplicitParam(name = "recording", value = "Represents call recording of a customer.", required = true, dataType = "java.util.String", paramType = "path"),
    })
    @DeleteMapping(DELETE_CDR_BY_TENANT_UUID)
    public ResponseEntity<String> deletePhoneBookRecords(@RequestHeader(value = TENANT_UUID) String customerId,
                                                         @RequestParam(value = "recording") String recording) throws Exception {


        String content = cdrService.deleteCallDataRecord(customerId, recording);

        return ResponseEntity.ok().body(mapper.writeValueAsString(content));
    }

    @ApiOperation(value = "Returns List of call data records as a response", response = String.class, tags = {API_FETCH_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant-uuid", value = "Represents existing customer. Mandatory.", required = true, dataType = "java.lang.String", paramType = "header"),
    })
    @GetMapping(GET_CDR_REPORT_BY_TENANT_UUID)
    public ResponseEntity<String> getCdrReportByTenant(@RequestHeader(value = "tenant-uuid") String customerId) throws CdrServiceException, JsonProcessingException {
        CDRReport content = cdrService.generateReportByTenant(customerId);

        return ResponseEntity.ok().body(mapper.writeValueAsString(content));
    }

    @ApiOperation(value = "Returns List of reports of call data records of each tenant as a response", response = List.class, tags = {API_FETCH_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @GetMapping(GET_CDR_REPORTS_FOR_ALL_TENANTS)
    public ResponseEntity<List<CDRReport>> getCdrReports() throws CdrServiceException, JsonProcessingException {
        List<CDRReport> content = cdrService.generateReports();

        return ResponseEntity.ok().body(content);
    }
}
