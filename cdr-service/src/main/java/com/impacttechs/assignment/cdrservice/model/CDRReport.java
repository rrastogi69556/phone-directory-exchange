package com.impacttechs.assignment.cdrservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.impacttechs.assignment.cdrservice.serializer.JsonDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import static com.impacttechs.assignment.cdrservice.constants.GeneralConstants.ISO_INSTANT_FORMAT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CDRReport implements Serializable {

    private static final long serialVersionUID = -1298360923977662682L;

    @JsonProperty("total_number_of_calls")
    private int totalNumberOfCalls;
    @JsonProperty("total_successful_call_in_minutes")
    private long totalSuccessfulCallInMinutes;
    @JsonProperty("total_incomplete_call_in_minutes")
    private long totalIncompleteCallInMinutes;
    @JsonProperty("total_unmatched_call_in_minutes")
    private long totalUnMatchedCallInMinutes;
    @JsonProperty("latest_sync_date")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date latestSyncDate;

    @Override
    public String toString() {
        return "CDRReport{" +
                "totalNumberOfCalls=" + totalNumberOfCalls +
                ", totalSuccessfulCallInMinutes=" + totalSuccessfulCallInMinutes +
                ", totalIncompleteCallInMinutes=" + totalIncompleteCallInMinutes +
                ", totalUnMatchedCallInMinutes=" + totalUnMatchedCallInMinutes +
                ", latestSyncDate=" + latestSyncDate +
                '}';
    }
}
