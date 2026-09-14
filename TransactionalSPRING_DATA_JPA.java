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


  
