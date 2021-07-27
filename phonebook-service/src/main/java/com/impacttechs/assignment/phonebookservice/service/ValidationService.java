package com.impacttechs.assignment.phonebookservice.service;

import com.impacttechs.assignment.phonebookservice.abstraction.IValidationService;
import com.impacttechs.assignment.phonebookservice.exception.PhonebookServiceException;
import com.impacttechs.assignment.phonebookservice.model.PhoneBookRequest;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.impacttechs.assignment.phonebookservice.constants.ExceptionConstants.INVALID_DATA;
import static com.impacttechs.assignment.phonebookservice.utils.Utility.validateNumber;
import static io.micrometer.core.instrument.util.StringUtils.isBlank;
import static java.util.Objects.isNull;

@Service
public class ValidationService implements IValidationService {

    @Override
    public void validateCallerNumber(String callerNumber) throws PhonebookServiceException {
       validateObject(!validateNumber(callerNumber));
    }


    @Override
    public void validate(List<PhoneBookRequest> jsonRequest) throws PhonebookServiceException {
        validateObject(isNull(jsonRequest));
        validateEachPhoneBookRecord(jsonRequest);
    }

    private void validateEachPhoneBookRecord(List<PhoneBookRequest> jsonRequest) {
        for(PhoneBookRequest request : jsonRequest) {
            validateCallerNumber(request.getCallerNumber());
            validateObject(isBlank(request.getEmailId()));
        }
    }

    private void validateObject(boolean aNull) throws PhonebookServiceException {
        if (aNull) throw new PhonebookServiceException(INVALID_DATA);
    }
}
