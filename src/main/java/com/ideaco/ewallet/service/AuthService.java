package com.ideaco.ewallet.service;

import com.ideaco.ewallet.dto.LoginDTO;
import com.ideaco.ewallet.exception.LoginException;
import com.ideaco.ewallet.model.BalanceModel;
import com.ideaco.ewallet.model.UserModel;
import com.ideaco.ewallet.repository.BalanceRepository;
import com.ideaco.ewallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    public LoginDTO login(String userPhone, String password) throws LoginException {
        Optional<UserModel> userModelOptional = userRepository.findByUserPhone(userPhone);
        if(userModelOptional.isEmpty()){
            //throw exception
            throw new LoginException("User not found");
        }

        UserModel userModel = userModelOptional.get();
        if(userModel.getUserPassword().equals(password)){
            int balance = 0;
            Optional<BalanceModel> balanceModelOptional = balanceRepository.findByUserId(userModel.getUserId());
            if(balanceModelOptional.isPresent()){
                balance = balanceModelOptional.get().getBalance();
            }

            return convertDTO(userModel, balance);
        }else {
            //throw exception
            throw new LoginException("login failed");
        }
    }

    private LoginDTO convertDTO(UserModel userModel, int balance){
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUserId(userModel.getUserId());
        loginDTO.setUserName(userModel.getUserName());
        loginDTO.setUserPhone(userModel.getUserPhone());
        loginDTO.setUserEmail(userModel.getUserEmail());
        loginDTO.setUserPicture(userModel.getUserPicture());
        loginDTO.setBalance(balance);

        return loginDTO;
    }
}
