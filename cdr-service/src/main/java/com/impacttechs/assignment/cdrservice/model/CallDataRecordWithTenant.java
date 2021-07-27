package com.impacttechs.assignment.cdrservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import static com.impacttechs.assignment.cdrservice.constants.GeneralConstants.ISO_INSTANT_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallDataRecordWithTenant implements Serializable {
    private static final long serialVersionUID = 2456560126408394823L;

    private String uuid;
    @JsonProperty("domain_name")
    private String domainName;
    @JsonProperty("caller_name")
    private String callerName;
    @JsonProperty("caller_number")
    private long callerNumber;
    @JsonProperty("destination_number")
    private long destinationNumber;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("call_start")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date callStart;
    @JsonProperty("ring_start")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date ringStart;
    @JsonProperty("answer_start")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date answerStart;
    @JsonProperty("call_end")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date callEnd;
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("recording")
    private String recording;
    @JsonProperty("click_to_call")
    private boolean clickToCall;
    @JsonProperty("click_to_call_data")
    private String clickToCallData;
    @JsonProperty("action")
    private String action;



}
