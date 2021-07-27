package com.impacttechs.assignment.cdrservice.helper;

import com.impacttechs.assignment.cdrservice.entity.CallDataRecord;
import com.impacttechs.assignment.cdrservice.entity.MultiTenant;
import com.impacttechs.assignment.cdrservice.exception.CdrServiceException;
import com.impacttechs.assignment.cdrservice.model.CallDataRecordWithTenant;
import com.impacttechs.assignment.cdrservice.repository.CallDataRecordRepository;
import com.impacttechs.assignment.cdrservice.repository.MultiTenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

import static com.impacttechs.assignment.cdrservice.constants.ExceptionConstants.*;
import static com.impacttechs.assignment.cdrservice.constants.GeneralConstants.*;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

/**
 * This class' objective is to transact with DB and maintain the updated status of the CDR activity.
 */
@Component
@Slf4j
public class CDRDaoHelper {

    private final MultiTenantRepository repository;
    private final CallDataRecordRepository callrepository;

    @Autowired
    public CDRDaoHelper(MultiTenantRepository repository, CallDataRecordRepository callrepository) {
        this.repository = repository;
        this.callrepository = callrepository;
    }

    public void updateRecordsIfNotExist(List<CallDataRecordWithTenant> cdrMapPerTenant, String tenantUuid) throws CdrServiceException {
        Optional<MultiTenant> tenant = repository.findById(tenantUuid);
        if( tenant.isPresent() && isNotEmpty(cdrMapPerTenant)) {
            updateRecords(tenant.get(), cdrMapPerTenant);
            tenant.get().setLastUpdated(new Date());
            return;
        }

        createEntryForTenant(cdrMapPerTenant, tenantUuid);

    }

    @Transactional
    protected void updateRecords(MultiTenant tenant, List<CallDataRecordWithTenant> cdrFromWebHook) throws CdrServiceException {
        List<String> uniqueRecordingIDFromDB = tenant.getCallDataRecords().stream().map(CallDataRecord::getRecording).collect(toList());
        List<String> uniqueRecordingIDFromWebHook = cdrFromWebHook.stream().map(CallDataRecordWithTenant::getRecording).collect(toList());
        boolean isRemoved = uniqueRecordingIDFromWebHook.removeAll(uniqueRecordingIDFromDB);
        if(isRemoved) {
            List<CallDataRecordWithTenant> newTenants = cdrFromWebHook.stream().filter(cdrWithTenant -> isContainedIn(cdrWithTenant.getRecording(), uniqueRecordingIDFromWebHook)).collect(toList());
            updateInDB(newTenants, tenant.getUuid());
        }

    }

    private boolean isContainedIn(String tenantRecording, List<String>  uniqueRecordingIDFromWebHook) {
        return uniqueRecordingIDFromWebHook.contains(tenantRecording);
    }

    private void updateInDB(List<CallDataRecordWithTenant> newTenants, String Uuid) throws CdrServiceException {
        try {
            Optional<MultiTenant> tenantToUpdate = repository.findById(Uuid);
            if (tenantToUpdate.isPresent()) {
                List<CallDataRecord> newCallRecords = populateCallDataRecordsEntity(tenantToUpdate.get(), newTenants);
                tenantToUpdate.get().getCallDataRecords().addAll(newCallRecords);
                repository.save(tenantToUpdate.get());
            }
        } catch (Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_UPDATING_CDR_RECORD);
        }
    }

    /**
     * This method maps the properties and behaviours of external api CDR contents with CRM DB CDR contents.
     * @param customer - tenant
     * @param newCdrs - call data records
     * @return new mapped CDRs
     */
    public List<CallDataRecord> populateCallDataRecordsEntity(MultiTenant customer, List<CallDataRecordWithTenant> newCdrs) {
        List<CallDataRecord> newCallRecords = new LinkedList<>();
        for(CallDataRecordWithTenant cdr : newCdrs) {
            CallDataRecord record = new CallDataRecord();
            record.setDomainName(cdr.getDomainName());
            record.setCallerName(cdr.getCallerName());
            record.setCallerNumber(cdr.getCallerNumber());
            record.setDestinationNumber(cdr.getDestinationNumber());
            record.setDirection(cdr.getDirection());
            record.setCallStart(cdr.getCallStart());
            record.setCallEnd(cdr.getCallEnd());
            record.setRingStart(cdr.getRingStart());
            record.setAnswerStart(cdr.getAnswerStart());
            record.setRecording(cdr.getRecording());
            record.setClickToCall(cdr.isClickToCall());
            record.setClickToCallData(cdr.getClickToCallData());
            record.setAction(cdr.getAction());
            record.setMultiTenant(customer);
            newCallRecords.add(record);
        }
        return newCallRecords;
    }

    private void createEntryForTenant(List<CallDataRecordWithTenant> callDataRecordWithTenants, String uuid)  throws CdrServiceException {

        try{
            MultiTenant newTenant = new MultiTenant(uuid);
            List<CallDataRecord> newCallRecords = populateCallDataRecordsEntity(newTenant, callDataRecordWithTenants);
            newTenant.setCallDataRecords(newCallRecords);
            newTenant.setLastUpdated(new Date());
            repository.save(newTenant);
        } catch (Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_UPDATING_CDR_RECORD);
        }
    }

    public List<CallDataRecord> getAllCDRsByTenantId(String customerId) throws CdrServiceException {
        try {
           Optional<MultiTenant> tenant = repository.findById(customerId);
           if(tenant.isPresent()) {
               return tenant.get().getCallDataRecords();
           }

        } catch(Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_FETCHING_CDR_RECORD, e);
        }
        return emptyList();
    }

    public String createCallDataRecords(String customerId, List<CallDataRecordWithTenant> callDataRecords) throws CdrServiceException {
        try {
            Optional<MultiTenant> tenant = repository.findById(customerId);
            if (tenant.isPresent()) {
                List<CallDataRecord> records = populateCallDataRecordsEntity(tenant.get(), callDataRecords);
                List<CallDataRecord> existingRecords = tenant.get().getCallDataRecords();
                if (isNotEmpty(existingRecords)) {
                    existingRecords.addAll(records);
                    repository.save(tenant.get());
                }
            } else {
                MultiTenant newTenant = new MultiTenant(customerId);
                newTenant.setCallDataRecords(populateCallDataRecordsEntity(newTenant, callDataRecords));
                repository.save(newTenant);
            }
            return SUCCESS_CDR_RECORD_UPDATED;
        } catch (Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_FETCHING_CDR_RECORD, e);
        }
    }

    public String deleteCallDataRecord(String customerId, String recording) throws CdrServiceException {
        try{
            Optional<MultiTenant> tenant = repository.findById(customerId);
            if(tenant.isPresent()) {
                List<CallDataRecord> callDataRecordList = tenant.get().getCallDataRecords();
                if(nonNull(callDataRecordList) && isNotEmpty(callDataRecordList)) {
                    Optional<CallDataRecord> recordsToRemove = callDataRecordList.stream().filter(record -> recording.equals(record.getRecording())).findFirst();
                    if(callDataRecordList.size() == 1 && recordsToRemove.isPresent()) {
                        repository.delete(tenant.get());
                    } else {
                        recordsToRemove.ifPresent(record -> removeCallDataEntryFromList(tenant.get(), callDataRecordList, record));
                    }
                    return SUCCESS_CDR_RECORD_DELETED;
                    }
                }
            return NO_UPDATION;
        } catch(Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_DELETING_CDR_RECORD, e);
        }
    }

    private void removeCallDataEntryFromList(MultiTenant tenant, List<CallDataRecord> callDataRecordList, CallDataRecord recordsToRemove) throws CdrServiceException {
        try {
            boolean isRemoved = callDataRecordList.remove(recordsToRemove);
            if (isRemoved) {
                tenant.setCallDataRecords(callDataRecordList);
                repository.saveAndFlush(tenant);
            }
        }catch(Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_DELETING_CDR_RECORD);
        }
    }

    public Optional<MultiTenant> findTenantIfPresent(String customerId) throws CdrServiceException {
        try {
            return repository.findById(customerId);
        } catch(Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_FETCHING_CDR_RECORD, e);
        }
    }

    public List<MultiTenant> findAllTenants() throws CdrServiceException {
        try {
            return repository.findAll();
        }catch(Exception e) {
            throw new CdrServiceException(ERROR_OCCURRED_FETCHING_CDR_RECORD, e);
        }
    }
}

