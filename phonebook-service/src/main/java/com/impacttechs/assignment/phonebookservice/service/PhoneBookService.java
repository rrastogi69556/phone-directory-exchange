package com.impacttechs.assignment.phonebookservice.service;

import com.impacttechs.assignment.phonebookservice.abstraction.IPhoneBookService;
import com.impacttechs.assignment.phonebookservice.exception.PhonebookServiceException;
import com.impacttechs.assignment.phonebookservice.model.PhoneBookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.impacttechs.assignment.phonebookservice.constants.ExceptionConstants.ERROR_OCCURRED_PHONEBOOK_RECORD;
import static com.impacttechs.assignment.phonebookservice.constants.GeneralConstants.SUCCESS_PHONEBOOK_RECORD_UPDATED;
import static com.impacttechs.assignment.phonebookservice.utils.Utility.getContentsFromFileLocation;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@Slf4j
public class PhoneBookService implements IPhoneBookService {
    private final HashMap<String, PhoneBookRequest> phoneBookRecordsCache = new HashMap<>();
    private static final String PHONEBOOK_DIRECTORY_PATH = "static/pb_directory.csv";

    @PostConstruct
    public void init() {
        List<String[]> allPhoneRecords = getContentsFromFileLocation(PHONEBOOK_DIRECTORY_PATH, log);
        loadPhoneRecordsIntoCache(phoneBookRecordsCache, allPhoneRecords);
    }

    @Override
    public List<PhoneBookRequest> getAllPhoneBookRecords() throws PhonebookServiceException {
        return new ArrayList<>(phoneBookRecordsCache.values());
    }

    private void loadPhoneRecordsIntoCache(HashMap<String, PhoneBookRequest> phoneBookRecordsCache, List<String[]> allPhoneRecords) {
        if(nonNull(allPhoneRecords) && !allPhoneRecords.isEmpty())  {
            allPhoneRecords = allPhoneRecords.subList(1, allPhoneRecords.size());
            for(String[] line : allPhoneRecords) {
                PhoneBookRequest request = new PhoneBookRequest();
                request.setName(line[0]);
                request.setEmailId(line[1]);
                request.setCallerNumber(line[2]);
                phoneBookRecordsCache.put(line[2], request);
            }
        }
    }

    @Override
    public String getNameInPhoneBookRecord(String callerNumber) throws PhonebookServiceException {
        return getPhoneBookRecordsCache().containsKey(callerNumber) ? getPhoneBookRecordsCache().get(callerNumber).getName() : EMPTY;
    }

    @Override
    public String insertPhoneBookRecord(List<PhoneBookRequest> jsonRequest) throws PhonebookServiceException {
        try {
            for (PhoneBookRequest phoneRecord : jsonRequest) {
                getPhoneBookRecordsCache().put(phoneRecord.getCallerNumber(), phoneRecord);
            }
            return SUCCESS_PHONEBOOK_RECORD_UPDATED;
        }catch (Exception e) {
            throw new PhonebookServiceException(ERROR_OCCURRED_PHONEBOOK_RECORD , e);
        }

    }

    @Override
    public String deletePhoneBookRecord(List<PhoneBookRequest> jsonRequest) throws PhonebookServiceException {
        try {
            for (PhoneBookRequest phoneRecord : jsonRequest) {
                getPhoneBookRecordsCache().remove(phoneRecord.getCallerNumber());
            }
            return SUCCESS_PHONEBOOK_RECORD_UPDATED;
        }catch (Exception e) {
            throw new PhonebookServiceException(ERROR_OCCURRED_PHONEBOOK_RECORD, e);
        }
    }

    @Override
    public String updatePhoneBookRecord(PhoneBookRequest jsonRequest) throws PhonebookServiceException {
        try {
            if (nonNull(jsonRequest) && getPhoneBookRecordsCache().containsKey(jsonRequest.getCallerNumber())) {
                getPhoneBookRecordsCache().put(jsonRequest.getCallerNumber(), jsonRequest);
            }
            return SUCCESS_PHONEBOOK_RECORD_UPDATED;
        }catch (Exception e) {
            throw new PhonebookServiceException(ERROR_OCCURRED_PHONEBOOK_RECORD, e);
        }
    }

    public Map<String, PhoneBookRequest> getPhoneBookRecordsCache() {
        return phoneBookRecordsCache;
    }
}
