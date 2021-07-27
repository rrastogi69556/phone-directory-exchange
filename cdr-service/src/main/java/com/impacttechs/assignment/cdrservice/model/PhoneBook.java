package com.impacttechs.assignment.cdrservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneBook implements Serializable {
    private static final long serialVersionUID = -2292758043738470710L;
    private long callerNumber;
    private String emailId;
    private String name;

    @Override
    public String toString() {
        return "PhoneBook{" +
                "callerNumber=" + callerNumber +
                ", emailId='" + emailId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
