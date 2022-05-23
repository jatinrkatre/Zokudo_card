package com.cards.zokudo.dto.response;

import lombok.Data;

@Data
public class CustomerDetailsResponseDTO {
    private String success;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String preferredName;
    private String countryCode;
    private String accountType;
    private long customerId;
    private String customerHashId;
    private String dateOfBirth;
    private long programId;
    private double balanceLimit;
    private double monthlyLimit;
    private double yearlyLimit;
    private String programPlan;
    private String agentHashId;
    private String distributorHashId;
}
