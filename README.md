
# Transaction Management in Spring Boot

## Overview

In Spring Boot, transaction management is an essential part of ensuring that operations on the database are completed successfully and consistently. Transactions allow you to group multiple operations into a single unit, which can be committed or rolled back as a whole, ensuring data integrity and consistency.

Spring provides both programmatic and declarative transaction management. Declarative transaction management is preferred because it separates transaction logic from business logic, making the code cleaner and easier to maintain.

## Transaction Management Types

1. **Declarative Transaction Management**
   - This is the most commonly used approach.
   - Spring provides declarative transaction management through annotations like `@Transactional`.
   - It allows you to configure transactions using annotations, which can be applied to methods or classes.
   
2. **Programmatic Transaction Management**
   - It involves explicitly starting and committing or rolling back a transaction using `PlatformTransactionManager`.
   - This approach is more flexible but harder to manage and typically not recommended for most cases.

## `@Transactional` Annotation

Spring Boot provides the `@Transactional` annotation for declarative transaction management. This annotation can be applied to methods or classes to define a transaction scope.

### Common Attributes of `@Transactional`
- **propagation**: Defines how transactions are propagated in the case of method calls.
  - `REQUIRED`: If a transaction exists, use it; otherwise, create a new one.
  - `REQUIRES_NEW`: Always create a new transaction, suspending the existing one if present.
  - `SUPPORTS`: If there is an existing transaction, participate in it; otherwise, execute without a transaction.
  - `MANDATORY`: Requires an existing transaction.
  - `NEVER`: Never execute within a transaction.
  - `NOT_SUPPORTED`: Execute without a transaction, suspending the existing one if present.
  - `NESTED`: Executes within a nested transaction if a current transaction exists.

- **isolation**: Defines the isolation level of the transaction, i.e., how it interacts with other concurrent transactions.
  - `READ_COMMITTED`, `READ_UNCOMMITTED`, `REPEATABLE_READ`, `SERIALIZABLE`.
  
- **timeout**: Specifies the maximum time in seconds for a transaction to be active.

- **rollbackFor**: Specifies the exceptions that trigger a rollback. For example:
  ```java
  @Transactional(rollbackFor = Exception.class)
  ```

- **noRollbackFor**: Specifies the exceptions that should not trigger a rollback.

### Example of `@Transactional` Usage

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyService {

    @Transactional
    public void performTransaction() {
        // Code that should run within a transaction.
    }
}
```

### Transaction Example with Propagation and Rollback

```java
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
public void myMethod() {
    // Code that needs to be executed in a new transaction
}
```

## Programmatic Transaction Management

In some cases, you might need to manage transactions programmatically. This approach provides more control, but it's less common in Spring Boot applications.

Here’s an example using `PlatformTransactionManager`:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class MyService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void performTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            // Business logic goes here

            transactionManager.commit(status);  // Commit the transaction
        } catch (RuntimeException e) {
            transactionManager.rollback(status);  // Rollback the transaction in case of exception
            throw e;  // Rethrow the exception to allow it to propagate
        }
    }
}
```

## Exception Handling and Rollback

- By default, Spring only rolls back on unchecked exceptions (`RuntimeException` and its subclasses).
- You can specify which exceptions trigger a rollback using the `rollbackFor` and `noRollbackFor` attributes of the `@Transactional` annotation.

Example:
```java
@Transactional(rollbackFor = { SQLException.class, MyCustomException.class })
public void myMethod() {
    // Code that needs to be transactional
}
```

## Testing Transactions in Spring Boot

Spring Boot provides an easy way to test transactions with `@Transactional` in the test methods. The framework automatically rolls back the transaction after the test method completes, ensuring that tests do not affect the database.

Example of a test class:
```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MyServiceTest {

    @Autowired
    private MyService myService;

    @Test
    @Transactional
    public void testTransaction() {
        // Perform some database operations here
        myService.performTransaction();
        
        // Assertions to verify the changes
    }
}
```

## Conclusion

- Spring Boot provides robust transaction management support through the `@Transactional` annotation, allowing you to declaratively handle transactions with minimal code.
- For more complex scenarios, you can manage transactions programmatically using `PlatformTransactionManager`.
- Spring’s transaction management ensures that your application maintains data consistency and integrity, even in the case of failures.
