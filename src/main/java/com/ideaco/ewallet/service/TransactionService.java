package com.ideaco.ewallet.service;

import com.ideaco.ewallet.dto.BalanceDTO;
import com.ideaco.ewallet.dto.TransferDTO;
import com.ideaco.ewallet.dto.UserDTO;
import com.ideaco.ewallet.exception.BalanceNotAvailableException;
import com.ideaco.ewallet.exception.UserNotFoundException;
import com.ideaco.ewallet.model.BalanceModel;
import com.ideaco.ewallet.model.TransactionModel;
import com.ideaco.ewallet.model.UserBalanceModel;
import com.ideaco.ewallet.model.UserModel;
import com.ideaco.ewallet.repository.BalanceRepository;
import com.ideaco.ewallet.repository.TransactionRepository;
import com.ideaco.ewallet.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    @Transactional
    public TransferDTO transfer(int userId, int transactionAmount, int transactionReceiver, String transactionType)
            throws UserNotFoundException, BalanceNotAvailableException {
        // validate if receiver is exist
        Optional<UserModel> userModelOptional = userRepository.findById(transactionReceiver);
        if(userModelOptional.isEmpty()){
            throw new UserNotFoundException("Receiver not exist");
        }

        // validate if balance is available
        Optional<BalanceModel> optionalBalanceModel = balanceRepository.findByUserId(userId);
        if(optionalBalanceModel.isEmpty() || optionalBalanceModel.get().getBalance() < transactionAmount){
            throw new BalanceNotAvailableException("Balance not available");
        }

        //create transaction model
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionAmount(transactionAmount);
        transactionModel.setTransactionSender(userId);
        transactionModel.setTransactionReceiver(transactionReceiver);
        transactionModel.setTransactionType(transactionType);
        transactionModel.setTransactionTime(LocalDateTime.now());
        transactionModel.setTransactionStatus("PENDING");

        transactionRepository.save(transactionModel);

        //add receiver balance
        Optional<BalanceModel> optBalanceReceiver = balanceRepository.findByUserId(transactionReceiver);
        int receiverInitialBalance = optBalanceReceiver.get().getBalance(); // saldo awal
        optBalanceReceiver.get().setBalance(receiverInitialBalance + transactionAmount); // saldo awal + transfer amount
        balanceRepository.save(optBalanceReceiver.get());

        //reduce sender balance
        int senderInitialBalance = optionalBalanceModel.get().getBalance(); // saldo awal
        optionalBalanceModel.get().setBalance(senderInitialBalance - transactionAmount); //saldo awal - transfer amount
        balanceRepository.save(optionalBalanceModel.get());

        transactionModel.setTransactionStatus("SUCCESSSUCCESS");
        TransactionModel transferModel = transactionRepository.save(transactionModel);

        return convertDTO(transferModel);

    }

    public BalanceDTO getUserBalance(int userId) throws UserNotFoundException{
        Optional<UserBalanceModel> userBalance = balanceRepository.getUserBalance(userId);
        if(userBalance.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return convertDTO(userBalance.get());
    }

    private BalanceDTO convertDTO(UserBalanceModel userBalanceModel){
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setBalanceId(userBalanceModel.getBalanceId());
        balanceDTO.setBalance(userBalanceModel.getBalanceAmount());

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userBalanceModel.getUserId());
        userDTO.setUserName(userBalanceModel.getUserName());
        userDTO.setUserPhone(userBalanceModel.getUserPhone());

        balanceDTO.setUser(userDTO);
        return balanceDTO;
    }

    private TransferDTO convertDTO(TransactionModel transactionModel){
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setTransactionId(transactionModel.getTransactionId());
        transferDTO.setTransactionSender(transactionModel.getTransactionSender());
        transferDTO.setTransactionReceiver(transactionModel.getTransactionReceiver());
        transferDTO.setTransactionAmount(transactionModel.getTransactionAmount());
        transferDTO.setTransactionTime(transactionModel.getTransactionTime());
        transferDTO.setTransactionType(transactionModel.getTransactionType());
        transferDTO.setTransactionStatus(transactionModel.getTransactionStatus());

        Optional<UserModel> senderModelOptional = userRepository.findById(transactionModel.getTransactionSender());
        transferDTO.setSenderName(senderModelOptional.get().getUserName());
        Optional<UserModel> receiverModelOptional = userRepository.findById(transactionModel.getTransactionReceiver());
        transferDTO.setReceiverName(receiverModelOptional.get().getUserName());
        return transferDTO;
    }
}
