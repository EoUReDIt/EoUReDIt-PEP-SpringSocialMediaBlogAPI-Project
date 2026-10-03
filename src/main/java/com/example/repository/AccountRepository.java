package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Extends JpaRepository so Spring Data generates save/findAll/findById/etc. at runtime.
 * - Account is the managed entity.
 * - Integer matches the type of Account.accountId.
 *
 * findByUsername is a derived query: Spring Data parses the method name ("findBy" + "Username") and
 * builds the SQL for us - no method body or @Query needed. Used by AccountService for both registration
 * (checking for a duplicate username) and login (looking up the account to check against).
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);

}