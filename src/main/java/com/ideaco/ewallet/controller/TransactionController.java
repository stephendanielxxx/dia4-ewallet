package com.ideaco.ewallet.controller;

import com.ideaco.ewallet.dto.TransferDTO;
import com.ideaco.ewallet.exception.BalanceNotAvailableException;
import com.ideaco.ewallet.exception.UserNotFoundException;
import com.ideaco.ewallet.response.TransferResponse;
import com.ideaco.ewallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestParam("user_id") int userId,
                                                     @RequestParam("transaction_amount") int transactionAmount,
                                                     @RequestParam("transaction_receiver") int transactionReceiver,
                                                     @RequestParam("transaction_type") String transactionType){
        TransferResponse transferResponse = new TransferResponse();
        try {
            TransferDTO transferDTO = transactionService.transfer(userId, transactionAmount, transactionReceiver, transactionType);
            transferResponse.setSuccess(true);
            transferResponse.setMessage("Successfully transfer balance");
            transferResponse.setErrorCode("");
            transferResponse.setData(transferDTO);

            return ResponseEntity.ok().body(transferResponse);
        } catch (UserNotFoundException e) {
            transferResponse.setSuccess(false);
            transferResponse.setMessage("Failed transfer balance");
            transferResponse.setErrorCode("100"); // receiver account not found

            return ResponseEntity.badRequest().body(transferResponse);
        } catch (BalanceNotAvailableException e) {
            transferResponse.setSuccess(false);
            transferResponse.setMessage("Balance not available/not enough balance");
            transferResponse.setErrorCode("101"); // receiver account not found

            return ResponseEntity.badRequest().body(transferResponse);
        }

    }
}
