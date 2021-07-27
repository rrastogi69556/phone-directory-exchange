package com.impacttechs.assignment.cdrservice.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.impacttechs.assignment.cdrservice.constants.GeneralConstants.ISO_INSTANT_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "call_data_record", indexes = {@Index(name = "call_data_record_index", columnList = "id,recording")})
public class CallDataRecord implements Serializable {
    private static final long serialVersionUID = -181024505938016959L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "multi_tenant_id", nullable = false, referencedColumnName = "uuid")
    private MultiTenant multiTenant;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "caller_name")
    private String callerName;

    @Column(name = "caller_number")
    private long callerNumber;

    @Column(name = "destination_number")
    private long destinationNumber;

    @Column(name = "direction")
    private String direction;

    @Column(name = "call_start")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date callStart;

    @Column(name = "ring_start")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date ringStart;

    @Column(name = "answer_start")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date answerStart;

    @Column(name = "call_end")
    @JsonFormat(pattern = ISO_INSTANT_FORMAT, shape = JsonFormat.Shape.STRING)
    private Date callEnd;

    @Column(name = "duration")
    private int duration;
    @Column(name = "recording")
    private String recording;

    @Column(name = "click_to_call")
    private boolean clickToCall;

    @Column(name = "click_to_call_data")
    private String clickToCallData;

    @Column(name = "action")
    private String action;

    @Override
    public String toString() {
        return "CallDataRecord{" +
                "id=" + id +
                ", multiTenant=" + multiTenant +
                ", domainName='" + domainName + '\'' +
                ", callerName='" + callerName + '\'' +
                ", callerNumber=" + callerNumber +
                ", destinationNumber=" + destinationNumber +
                ", direction='" + direction + '\'' +
                ", callStart=" + callStart +
                ", ringStart=" + ringStart +
                ", answerStart=" + answerStart +
                ", callEnd=" + callEnd +
                ", duration=" + duration +
                ", recording='" + recording + '\'' +
                ", clickToCall=" + clickToCall +
                ", clickToCallData='" + clickToCallData + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
