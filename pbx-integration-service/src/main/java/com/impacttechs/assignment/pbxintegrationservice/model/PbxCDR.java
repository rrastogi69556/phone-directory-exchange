package com.impacttechs.assignment.pbxintegrationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PbxCDR implements Serializable {
    private static final long serialVersionUID = 7186488731073558744L;
    private long total;
    private List<CallDataRecordWithTenant> data;
    private String tenantUUID;
}
