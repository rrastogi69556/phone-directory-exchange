package com.impacttechs.assignment.pbxintegrationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.impacttechs.assignment.pbxintegrationservice.abstraction.IPbxService;
import com.impacttechs.assignment.pbxintegrationservice.exception.PbxServiceException;
import com.impacttechs.assignment.pbxintegrationservice.model.PbxCDR;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.impacttechs.assignment.pbxintegrationservice.constants.ApiUrlConstants.*;

@RestController
@RequestMapping(GET_PBX_URL)
@Api(value = "PBX API Service of CRM. This API interacts with external PBX clients")
public class PBXController {
    private static final String API_FETCH_TAG = "PBX_FETCH_API";
    private static final String API_CREATE_TAG = "PBX_TRIGGER_API";

    private final IPbxService pbxService;

    @Autowired
    public PBXController(IPbxService pbxService) {
        this.pbxService = pbxService;
    }

    @ApiOperation(value = "Returns status response for triggering call data records in PBX exchange", response = String.class, tags = {API_CREATE_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @GetMapping(TRIGGER_WEBHOOK_URL)
    public ResponseEntity<String> triggerAndFetchLatestRecords() throws PbxServiceException {

        return pbxService.triggerAndFetch();
    }

    @ApiOperation(value = "Returns list of call data records from PBX exchange", response = List.class, tags = {API_FETCH_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @GetMapping(GET_WEBHOOK_URL)
    public ResponseEntity<PbxCDR> fetchCallDataRecords() throws PbxServiceException, JsonProcessingException {

        return ResponseEntity.ok().body(pbxService.fetchCallDataRecords());
    }

}
