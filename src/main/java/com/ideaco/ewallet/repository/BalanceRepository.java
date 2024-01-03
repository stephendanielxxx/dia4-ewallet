package com.ideaco.ewallet.repository;

import com.ideaco.ewallet.model.BalanceModel;
import com.ideaco.ewallet.model.UserBalanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<BalanceModel, Integer> {
    Optional<BalanceModel> findByUserId(int userId);

    @Query(value = "SELECT balance.balance_id as balanceId, balance.user_id as userId, " +
            "balance.balance as balanceAmount, user.user_name as userName, user.user_phone as userPhone " +
            "FROM tab_balance balance " +
            "JOIN tab_user user ON balance.user_id = user.user_id " +
            "where user.user_id = :userId ", nativeQuery = true)
    Optional<UserBalanceModel> getUserBalance(@Param("userId") int userId);
}
