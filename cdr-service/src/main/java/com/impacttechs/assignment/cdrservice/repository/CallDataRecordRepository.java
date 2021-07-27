package com.impacttechs.assignment.cdrservice.repository;

import com.impacttechs.assignment.cdrservice.entity.CallDataRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallDataRecordRepository extends CrudRepository<CallDataRecord, Long> {
}
