package com.impacttechs.assignment.phonebookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacttechs.assignment.phonebookservice.abstraction.IPhoneBookService;
import com.impacttechs.assignment.phonebookservice.abstraction.IValidationService;
import com.impacttechs.assignment.phonebookservice.exception.ApiResponse;
import com.impacttechs.assignment.phonebookservice.model.PhoneBookRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.impacttechs.assignment.phonebookservice.constants.ApiUrlConstants.*;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Api(value = "PhoneBook Directory DB Service. This API stores, returns, updates, deletes phonebook data records")
@RequestMapping(GET_PHONE_BOOK_URL)
public class PhoneBookController {
    private static final String API_FETCH_TAG = "PHONE_BOOK_DIRECTORY_FETCH_RECORDS";
    private static final String API_CREATE_TAG = "PHONE_BOOK_DIRECTORY_CREATE_RECORDS";
    private static final String API_DELETE_TAG = "PHONE_BOOK_DIRECTORY_DELETE_RECORDS";
    private static final String API_PUT_TAG = "PHONE_BOOK_DIRECTORY_UPDATE_RECORDS";

    private final IPhoneBookService phoneBookService;
    private final ObjectMapper mapper;
    private final IValidationService validationService;

    @Autowired
    public PhoneBookController(IPhoneBookService phoneBookService, ObjectMapper mapper, IValidationService validationService) {
        this.mapper = mapper;
        this.phoneBookService = phoneBookService;
        this.validationService = validationService;
    }

    @ApiOperation(value = "Returns Phone Book Directory records", response = String.class, tags = {API_FETCH_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "callerNumber", value = "Represents existing caller Number. Mandatory.", required = true, dataType = "java.lang.Long", paramType = "path"),
    })
    @GetMapping(GET_PHONE_BOOK_BY_CALLER_NUMBER)
    public ResponseEntity<String> getPhoneBookRecord(@PathVariable(value = "callerNumber") String callerNumber) throws Exception {

        validationService.validateCallerNumber(callerNumber);

        String callerName = phoneBookService.getNameInPhoneBookRecord(callerNumber);

        ApiResponse response = new ApiResponse(OK, callerName);

        return ResponseEntity.ok().body(mapper.writeValueAsString(response));
    }

    @ApiOperation(value = "Returns Phone Book Directory records", response = String.class, tags = {API_FETCH_TAG}, produces = "Application/JSON", httpMethod = "GET")
    @GetMapping(GENERIC_PATH)
    public ResponseEntity<List<PhoneBookRequest>> getAllPhoneBookRecords() throws Exception {

        List<PhoneBookRequest> response = (List<PhoneBookRequest>) phoneBookService.getAllPhoneBookRecords();


        return ResponseEntity.ok().body(response);
    }


    @ApiOperation(value = "Returns success/failed response", response = String.class, tags = {API_CREATE_TAG}, produces = "Application/JSON", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "callerNumber", value = "Represents existing caller Number. Mandatory.", required = true, dataType = "java.lang.String", paramType = "query"),
    })
    @PostMapping(GENERIC_PATH)
    public ResponseEntity<String> storePhoneBookRecords(@RequestBody(required = false) List<PhoneBookRequest> jsonRequest) throws Exception {

        validationService.validate(jsonRequest);

        String content =  phoneBookService.insertPhoneBookRecord(jsonRequest);

        ApiResponse response = new ApiResponse(OK, content);

        return ResponseEntity.ok().body(mapper.writeValueAsString(response));
    }

    @ApiOperation(value = "Returns success/failed response", response = String.class, tags = {API_DELETE_TAG}, produces = "Application/JSON", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonRequest", value = "Represents list of phone book records that needs to be deleted. Mandatory.", required = true, paramType = "query"),
    })
    @DeleteMapping(GENERIC_PATH)
    public ResponseEntity<String> deletePhoneBookRecords(@RequestBody(required = false) List<PhoneBookRequest> jsonRequest) throws Exception {

        validationService.validate(jsonRequest);

        String content = phoneBookService.deletePhoneBookRecord(jsonRequest);

        ApiResponse response = new ApiResponse(OK, content);

        return ResponseEntity.ok().body(mapper.writeValueAsString(response));
    }

    @ApiOperation(value = "Returns success/failed response", response = String.class, tags = {API_PUT_TAG}, produces = "Application/JSON", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonRequest", value = "Represents json representation of PhoneBook. CallerNumber and Email mandatory.", required = true, paramType = "query"),
    })
    @PutMapping(GENERIC_PATH)
    public ResponseEntity<String> putPhoneBookRecords(@RequestBody(required = false) PhoneBookRequest jsonRequest) throws Exception {

        validationService.validate(singletonList(jsonRequest));

        String content = phoneBookService.updatePhoneBookRecord(jsonRequest);

        ApiResponse response = new ApiResponse(OK, content);

        return ResponseEntity.ok().body(mapper.writeValueAsString(response));
    }


}
