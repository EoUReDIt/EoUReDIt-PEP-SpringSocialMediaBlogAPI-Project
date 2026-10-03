package com.example.service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidRegistrationException;
import com.example.exception.LoginFailedException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * User Story 1 - Registration.
     * Order matters: blank username / short password -> 400 (InvalidRegistrationException),
     * duplicate username -> 409 (DuplicateUsernameException) - the Controller distinguishes these
     * with two separate catch blocks.
     */
    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank())
            throw new InvalidRegistrationException();

        if (account.getPassword() == null || account.getPassword().length() < 4)
            throw new InvalidRegistrationException();

        if (accountRepository.findByUsername(account.getUsername()).isPresent())
            throw new DuplicateUsernameException();

        return accountRepository.save(account);
    }

    /**
     * User Story 2 - Login.
     * Both "no such username" and "wrong password" collapse into the same LoginFailedException,
     * since the README maps both to a single 401 response.
     */
    public Account login(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername())
                .orElseThrow(LoginFailedException::new);

        if (!existingAccount.getPassword().equals(account.getPassword())) {
            throw new LoginFailedException();
        }

        return existingAccount;
    }
}