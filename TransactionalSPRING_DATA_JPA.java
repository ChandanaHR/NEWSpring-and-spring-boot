//Account entity
package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolder;

    private double balance;

    public Account() {
    }

    public Account(String accountHolder, double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}


Repository
package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository
        extends JpaRepository<Account, Long> {
}

Transfer request DTO
  We don't want the client to send an entire Account object.
  package com.example.demo.dto;

public class TransferRequest {

    private Long fromAccountId;

    private Long toAccountId;

    private double amount;

    public TransferRequest() {
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

// Service without Transaction
@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public void transferMoney(
            Long fromId,
            Long toId,
            double amount) {

        Account fromAccount =
                repository.findById(fromId)
                        .orElseThrow();

        Account toAccount =
                repository.findById(toId)
                        .orElseThrow();

        fromAccount.setBalance(
                fromAccount.getBalance() - amount
        );

        repository.save(fromAccount);

        // Something goes wrong here

        toAccount.setBalance(
                toAccount.getBalance() + amount
        );

        repository.save(toAccount);
    }
}

Service with Transaction
  @Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transferMoney(
            Long fromId,
            Long toId,
            double amount) {

        Account fromAccount =
                repository.findById(fromId)
                        .orElseThrow();

        Account toAccount =
                repository.findById(toId)
                        .orElseThrow();

        fromAccount.setBalance(
                fromAccount.getBalance() - amount
        );

        repository.save(fromAccount);

        toAccount.setBalance(
                toAccount.getBalance() + amount
        );

        repository.save(toAccount);
    }
}
with transaction
  @Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transferMoney(
            Long fromId,
            Long toId,
            double amount) {

        Account fromAccount =
                repository.findById(fromId)
                        .orElseThrow();

        Account toAccount =
                repository.findById(toId)
                        .orElseThrow();

        fromAccount.setBalance(
                fromAccount.getBalance() - amount
        );

        repository.save(fromAccount);

        toAccount.setBalance(
                toAccount.getBalance() + amount
        );

        repository.save(toAccount);
    }
}


// complete production level code
money-transfer
│
├── src/main/java/com/example/moneytransfer
│
├── controller
│   └── AccountController.java
│
├── dto
│   ├── AccountResponse.java
│   ├── CreateAccountRequest.java
│   ├── TransferRequest.java
│   └── TransferResponse.java
│
├── entity
│   └── Account.java
│
├── exception
│   ├── AccountNotFoundException.java
│   ├── InsufficientBalanceException.java
│   ├── GlobalExceptionHandler.java
│   └── InvalidTransferException.java
│
├── repository
│   └── AccountRepository.java
│
├── service
│   └── AccountService.java
│
└── MoneyTransferApplication.java
// Entity : Account.java
  package com.example.moneytransfer.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
    name = "accounts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_account_number",
            columnNames = "account_number"
        )
    }
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "account_number",
        nullable = false,
        unique = true,
        length = 20
    )
    private String accountNumber;

    @Column(
        name = "account_holder_name",
        nullable = false,
        length = 100
    )
    private String accountHolderName;

    @Column(
        nullable = false,
        precision = 19,
        scale = 2
    )
    private BigDecimal balance;

    public Account() {
    }

    public Account(
            String accountNumber,
            String accountHolderName,
            BigDecimal balance) {

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
// DTO: CreateAccountRequest
package com.example.moneytransfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreateAccountRequest {

    @NotBlank(message = "Account number is required")
    @Pattern(
        regexp = "\\d{10,20}",
        message = "Account number must contain 10 to 20 digits"
    )
    private String accountNumber;

    @NotBlank(message = "Account holder name is required")
    @Size(
        min = 3,
        max = 100,
        message = "Account holder name must be between 3 and 100 characters"
    )
    private String accountHolderName;

    @DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "Initial balance cannot be negative"
    )
    private BigDecimal balance = BigDecimal.ZERO;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

// DTO: TransferRequest
package com.example.moneytransfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class TransferRequest {

    @NotNull(message = "From account number is required")
    @Pattern(
        regexp = "\\d{10,20}",
        message = "From account number must contain 10 to 20 digits"
    )
    private String fromAccountNumber;

    @NotNull(message = "To account number is required")
    @Pattern(
        regexp = "\\d{10,20}",
        message = "To account number must contain 10 to 20 digits"
    )
    private String toAccountNumber;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(
        value = "0.01",
        message = "Transfer amount must be greater than zero"
    )
    private BigDecimal amount;

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

//DTO: AccountResponse DTO
 // We shouldn't return the complete Entity.
  package com.example.moneytransfer.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;

    public AccountResponse(
            Long id,
            String accountNumber,
            String accountHolderName,
            BigDecimal balance) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}

//DTO: TransferResponse DTO
  package com.example.moneytransfer.dto;

import java.math.BigDecimal;

public class TransferResponse {

    private String message;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

    public TransferResponse(
            String message,
            String fromAccount,
            String toAccount,
            BigDecimal amount) {

        this.message = message;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
//Repository
package com.example.moneytransfer.repository;

import com.example.moneytransfer.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(
            String accountNumber
    );

    boolean existsByAccountNumber(
            String accountNumber
    );
}

//Custom exceptions
AccountNotFoundException
package com.example.moneytransfer.exception;

public class AccountNotFoundException
        extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
InsufficientBalanceException
package com.example.moneytransfer.exception;

public class InsufficientBalanceException
        extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
InvalidTransferException
package com.example.moneytransfer.exception;

public class InvalidTransferException
        extends RuntimeException {

    public InvalidTransferException(String message) {
        super(message);
    }
}
//Controller code
package com.example.moneytransfer.controller;

import com.example.moneytransfer.dto.AccountResponse;
import com.example.moneytransfer.dto.CreateAccountRequest;
import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.dto.TransferResponse;
import com.example.moneytransfer.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(
            AccountService accountService) {

        this.accountService = accountService;
    }

    // ----------------------------------------
    // CREATE ACCOUNT
    // ----------------------------------------

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response =
                accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ----------------------------------------
    // GET ACCOUNT
    // ----------------------------------------

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountNumber) {

        AccountResponse response =
                accountService.getAccount(accountNumber);

        return ResponseEntity.ok(response);
    }

    // ----------------------------------------
    // GET ALL ACCOUNTS
    // ----------------------------------------

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {

        return ResponseEntity.ok(
                accountService.getAllAccounts()
        );
    }

    // ----------------------------------------
    // MONEY TRANSFER
    // ----------------------------------------

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferMoney(
            @Valid @RequestBody TransferRequest request) {

        TransferResponse response =
                accountService.transferMoney(request);

        return ResponseEntity.ok(response);
    }
}
//Service code
package com.example.moneytransfer.service;

import com.example.moneytransfer.dto.AccountResponse;
import com.example.moneytransfer.dto.CreateAccountRequest;
import com.example.moneytransfer.dto.TransferRequest;
import com.example.moneytransfer.dto.TransferResponse;
import com.example.moneytransfer.entity.Account;
import com.example.moneytransfer.exception.AccountNotFoundException;
import com.example.moneytransfer.exception.InsufficientBalanceException;
import com.example.moneytransfer.exception.InvalidTransferException;
import com.example.moneytransfer.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(
            AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    // ----------------------------------------
    // CREATE ACCOUNT
    // ----------------------------------------

    @Transactional
    public AccountResponse createAccount(
            CreateAccountRequest request) {

        if (accountRepository.existsByAccountNumber(
                request.getAccountNumber())) {

            throw new InvalidTransferException(
                    "Account number already exists"
            );
        }

        BigDecimal initialBalance =
                request.getBalance() == null
                        ? BigDecimal.ZERO
                        : request.getBalance();

        Account account = new Account(
                request.getAccountNumber(),
                request.getAccountHolderName(),
                initialBalance
        );

        Account savedAccount =
                accountRepository.save(account);

        return convertToResponse(savedAccount);
    }

    // ----------------------------------------
    // GET ACCOUNT
    // ----------------------------------------

    @Transactional(readOnly = true)
    public AccountResponse getAccount(
            String accountNumber) {

        Account account =
                findAccount(accountNumber);

        return convertToResponse(account);
    }

    // ----------------------------------------
    // GET ALL ACCOUNTS
    // ----------------------------------------

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ----------------------------------------
    // MONEY TRANSFER
    // ----------------------------------------

    @Transactional
    public TransferResponse transferMoney(
            TransferRequest request) {

        String fromAccountNumber =
                request.getFromAccountNumber();

        String toAccountNumber =
                request.getToAccountNumber();

        BigDecimal amount =
                request.getAmount();

        // 1. Same account validation
        if (fromAccountNumber.equals(toAccountNumber)) {

            throw new InvalidTransferException(
                    "Source and destination accounts cannot be the same"
            );
        }

        // 2. Amount validation
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidTransferException(
                    "Transfer amount must be greater than zero"
            );
        }

        // 3. Find sender
        Account fromAccount =
                findAccount(fromAccountNumber);

        // 4. Find receiver
        Account toAccount =
                findAccount(toAccountNumber);

        // 5. Check balance
        if (fromAccount.getBalance()
                .compareTo(amount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance in source account"
            );
        }

        // ------------------------------------
        // DEBIT
        // ------------------------------------

        fromAccount.setBalance(
                fromAccount.getBalance()
                        .subtract(amount)
        );

        // ------------------------------------
        // CREDIT
        // ------------------------------------

        toAccount.setBalance(
                toAccount.getBalance()
                        .add(amount)
        );

        // Save both
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new TransferResponse(
                "Money transferred successfully",
                fromAccount.getAccountNumber(),
                toAccount.getAccountNumber(),
                amount
        );
    }

    // ----------------------------------------
    // FIND ACCOUNT
    // ----------------------------------------

    private Account findAccount(
            String accountNumber) {

        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found: "
                                        + accountNumber
                        )
                );
    }

    // ----------------------------------------
    // ENTITY → DTO
    // ----------------------------------------

    private AccountResponse convertToResponse(
            Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountHolderName(),
                account.getBalance()
        );
    }
}
//GlobalExceptionHandler.class
package com.example.moneytransfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------------------------------
    // ACCOUNT NOT FOUND
    // ----------------------------------------

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
    handleAccountNotFound(
            AccountNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // ----------------------------------------
    // INSUFFICIENT BALANCE
    // ----------------------------------------

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>>
    handleInsufficientBalance(
            InsufficientBalanceException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    // ----------------------------------------
    // INVALID TRANSFER
    // ----------------------------------------

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<Map<String, Object>>
    handleInvalidTransfer(
            InvalidTransferException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    // ----------------------------------------
    // VALIDATION ERROR
    // ----------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response =
                new HashMap<>();

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status",
                HttpStatus.BAD_REQUEST.value());

        response.put("errors", errors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // ----------------------------------------
    // GENERIC EXCEPTION
    // ----------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleGenericException(Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
    }

    // ----------------------------------------
    // COMMON RESPONSE
    // ----------------------------------------

    private ResponseEntity<Map<String, Object>>
    buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                status.value()
        );

        response.put(
                "message",
                message
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
hOW To add data
    Create Account 1
POST http://localhost:8080/api/accounts

Body → raw → JSON:

{
    "accountNumber": "1000000001",
    "accountHolderName": "John",
    "balance": 10000.00
}
Create Account 2
POST http://localhost:8080/api/accounts

Body:

{
    "accountNumber": "1000000002",
    "accountHolderName": "David",
    "balance": 5000.00
}
Check All Accounts

Request:

GET http://localhost:8080/api/accounts

Transfer Money
Now:
John
₹10,000
transfers:
₹2,000
to:
David
₹5,000
Request:
POST http://localhost:8080/api/accounts/transfer
    Body:

{
    "fromAccountNumber": "1000000001",
    "toAccountNumber": "1000000002",
    "amount": 2000.00
}
Perfect:
Before:
John  = ₹10,000
David = ₹5,000

        ↓ ₹2,000
After:
John  = ₹8,000
David = ₹7,000
Total money remains:
₹15,000

    Test Validation — Negative Amount

Send:

{
    "fromAccountNumber": "1000000001",
    "toAccountNumber": "1000000002",
    "amount": -2000.00
}
you'll get:

{
    "timestamp": "...",
    "status": 400,
    "errors": {
        "amount": "Transfer amount must be greater than zero"
    }
}
Test Insufficient Balance
Suppose John has:
₹8,000
Try:
{
    "fromAccountNumber": "1000000001",
    "toAccountNumber": "1000000002",
    "amount": 10000.00
}
Service checks:

if (fromAccount.getBalance()
        .compareTo(amount) < 0) {
Test Account Not Found
Request:

{
    "fromAccountNumber": "9999999999",
    "toAccountNumber": "1000000002",
    "amount": 1000.00
}
Response:

{
    "timestamp": "...",
    "status": 404,
    "message": "Account not found: 9999999999"
}
    Test Same Account

Request:

{
    "fromAccountNumber": "1000000001",
    "toAccountNumber": "1000000001",
    "amount": 1000.00
}

Response:

{
    "timestamp": "...",
    "status": 400,
    "message": "Source and destination accounts cannot be the same"
}
    


  
