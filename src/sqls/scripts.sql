CREATE TABLE account (
    account_id INT PRIMARY KEY,
    account_name VARCHAR(50),
    balance NUMBER
);

-- Sample data for account table
INSERT INTO account (account_id, account_name, balance) VALUES (1, 'Account 1', 1000.0);
INSERT INTO account (account_id, account_name, balance) VALUES (2, 'Account 2', 500.0);

---
CREATE TABLE transaction (
    transaction_id INT PRIMARY KEY,
    account_id_from INT,
    account_id_to INT,
    amount NUMBER,
    FOREIGN KEY (account_id_from) REFERENCES account(account_id),
    FOREIGN KEY (account_id_to) REFERENCES account(account_id)
);

-- Sample data for transaction table
INSERT INTO transaction (transaction_id, account_id_from, account_id_to, amount) VALUES (1, 1, 2, 100.0);

----
create table customer(customer_ID  int, customer_name varchar(50), mobileNumber int, homeAddress varchar(200));