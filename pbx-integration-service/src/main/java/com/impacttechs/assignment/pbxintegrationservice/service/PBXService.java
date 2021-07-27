package com.impacttechs.assignment.pbxintegrationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacttechs.assignment.pbxintegrationservice.abstraction.IPbxService;
import com.impacttechs.assignment.pbxintegrationservice.exception.PbxServiceException;
import com.impacttechs.assignment.pbxintegrationservice.model.PbxCDR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.impacttechs.assignment.pbxintegrationservice.constants.ApiUrlConstants.GET_WEBHOOK_URL;
import static com.impacttechs.assignment.pbxintegrationservice.constants.ApiUrlConstants.TRIGGER_WEBHOOK_URL;
import static com.impacttechs.assignment.pbxintegrationservice.constants.ExceptionConstants.ERROR_NOTFOUND_VALID_RESPONSE;
import static com.impacttechs.assignment.pbxintegrationservice.constants.GeneralConstants.*;
import static com.impacttechs.assignment.pbxintegrationservice.utils.Utility.throwErrorIfInvalidResponse;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PBXService implements IPbxService {

    private final RestTemplate externalRestTemplate;

    @Value("${impacttechs.crm.pbx.webhook.api.url:http://localhost:3030}")
    private String webhookTriggerUrl;

    private final ObjectMapper mapper;

    @Autowired
    public PBXService(RestTemplate externalRestTemplate, ObjectMapper mapper) {
        this.externalRestTemplate = externalRestTemplate;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<String> triggerAndFetch() throws PbxServiceException {
        ResponseEntity<String> responseEntity = externalRestTemplate.exchange(webhookTriggerUrl.concat(TRIGGER_WEBHOOK_URL), HttpMethod.GET, null, String.class);
        throwErrorIfInvalidResponse(responseEntity, log);
        log.info(SUCCESS_TRIGGER);
        return responseEntity;
    }

    @Override
    public PbxCDR fetchCallDataRecords() throws PbxServiceException, JsonProcessingException {
        ResponseEntity<String> fetchAllRecords = externalRestTemplate.exchange(webhookTriggerUrl.concat(GET_WEBHOOK_URL), HttpMethod.GET, null, String.class);
        log.debug("Response returned: [ {} ] ", fetchAllRecords.getBody());

        throwErrorIfInvalidResponse(fetchAllRecords, log);

        if (fetchAllRecords.getHeaders().containsKey(TENANT_UUID)
                && nonNull(fetchAllRecords.getHeaders().get(TENANT_UUID))) {
            PbxCDR cdr =  mapper.readValue(fetchAllRecords.getBody(), PbxCDR.class);
            String tenantUUID = fetchAllRecords.getHeaders().get(TENANT_UUID).get(0);
            cdr.setTenantUUID(tenantUUID);
            log.info(SUCCESS_RECORD_FETCHED);
            return cdr;
        }
        log.error(ERROR_NOTFOUND_VALID_RESPONSE);
        return null;
    }


}
