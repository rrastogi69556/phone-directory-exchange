package com.impacttechs.assignment.phonebookservice.abstraction;



import com.impacttechs.assignment.phonebookservice.exception.PhonebookServiceException;
import com.impacttechs.assignment.phonebookservice.model.PhoneBookRequest;

import java.util.List;

public interface IValidationService {
    void validateCallerNumber(String callerNumber) throws PhonebookServiceException;
    void validate(List<PhoneBookRequest> phoneBookRequest) throws PhonebookServiceException;
}
