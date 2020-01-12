# moneyTransfer
* To start project you need gradle
1.  execute ./gradlew clean test jar 
2.  after jar creation just start the app: java -jar MoneyTransfer-1.0-SNAPSHOT.jar 

* Endpoint documentation is available on http://localhost:8080/swagger

* To make transfer the following steps should be taken:
1. (Create user) POST /api/users/create with JSON body:
{
  "firstName": "string",
  "lastName": "string",
  "email": "string" //should satisfy pattern for email
}

2. (Create account with userId foreigh key) POST /api/accounts/create with JSON body:
{
  "userId": 0,
  "currency": "USD"
}

3. (Deposit money to created account) POST /api/accounts/deposit with JSON body:
{
  "recipientAccountId": 0,
  "amount": 0,
  "currency": "USD"
}

4. (Create another user and it's account) repeat steps 1-2
5. (Make transfer from fisrt account to second one) POST /api/accounts/transferwith JSON body:
{
  "senderAccountId": 0,
  "recipientAccountId": 0,
  "amount": 0,
  "currency": "USD",
  "transactionType": "INTERNAL"
}

** this is just for documentation, you can use swagger for testing
