package com.impacttechs.assignment.phonebookservice.abstraction;

import com.impacttechs.assignment.phonebookservice.exception.PhonebookServiceException;
import com.impacttechs.assignment.phonebookservice.model.PhoneBookRequest;

import java.util.List;

public interface IPhoneBookService {
    String getNameInPhoneBookRecord(String callerNumber) throws PhonebookServiceException;

    String insertPhoneBookRecord(List<PhoneBookRequest> jsonRequest) throws PhonebookServiceException;

    String deletePhoneBookRecord(List<PhoneBookRequest> jsonRequest) throws PhonebookServiceException;

    String updatePhoneBookRecord(PhoneBookRequest jsonRequest) throws PhonebookServiceException;

    List<PhoneBookRequest> getAllPhoneBookRecords() throws PhonebookServiceException;
}
