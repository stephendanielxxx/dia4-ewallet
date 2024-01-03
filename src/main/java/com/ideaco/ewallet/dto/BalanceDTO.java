package com.ideaco.ewallet.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BalanceDTO {
    private int balanceId;
    private int balance;
    private UserDTO user;
}
