package com.impacttechs.assignment.pbxintegrationservice.abstraction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.impacttechs.assignment.pbxintegrationservice.exception.PbxServiceException;
import com.impacttechs.assignment.pbxintegrationservice.model.PbxCDR;
import org.springframework.http.ResponseEntity;

public interface IPbxService {

    ResponseEntity<String> triggerAndFetch() throws PbxServiceException;

    PbxCDR fetchCallDataRecords() throws PbxServiceException, JsonProcessingException;
}
