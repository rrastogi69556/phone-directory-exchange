package com.impacttechs.assignment.cdrservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name =" multi_tenant", indexes = {@Index(name = "multi_tenant_index", columnList = "uuid")})
public class MultiTenant implements Serializable {

    private static final long serialVersionUID = 6560096126192188073L;

    public MultiTenant(String uuid) {
        this.uuid = uuid;
    }

    @Id
    @Column(name = "uuid")
    private String uuid;

   @Column(name = "call_data_record")
   @JsonIgnore
   @OneToMany(mappedBy = "multiTenant", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CallDataRecord> callDataRecords;

    @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "last_updated")
   private Date lastUpdated;


    @Override
    public String toString() {
        return "MultiTenant{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
