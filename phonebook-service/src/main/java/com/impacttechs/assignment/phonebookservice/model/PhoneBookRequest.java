package com.impacttechs.assignment.phonebookservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneBookRequest implements Serializable {
    private static final long serialVersionUID = 5196515438651790602L;
    private String callerNumber;

    private String emailId;

    private String name;
}

