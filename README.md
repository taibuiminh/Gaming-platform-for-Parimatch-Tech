# Group Project

Table of Contents

* [1. Task description](#-task-description)
  - [1.1. Stack](#-stack)
* [2. Game integration service](#-game-integration-service)
  - [2.1. API & Processing](#-api-processing)
    + [2.1.1. Balance example](#-balance-example)
    + [2.1.2. Bet example](#-bet-example)
    + [2.1.3. Win example](#-win-example)
    + [2.1.4. Hash Validation](#-hash-validation)
    + [2.1.5. Game Flow Validation](#-game-flow-validation)
    + [2.1.6. How to process requests](#-how-to-process-requests)
  - [2.2. Error handling](#-error-handling)
* [3. Simple Wallet](#-simple-wallet)
  - [3.1. API & Processing](#-api-processing-2)
    + [3.1.1. Validation rules](#-validation-rules)
    + [3.1.2. Idempotency](#-idempotency)
  - [3.2. Database model sample](#-database-model-sample)
* [4. Important Notes From Mentors](#-important-notes-from-mentors)

Below you can find all the details on group project

## [1. Task description](#-task-description)

Your dev team is chosen to implement gaming platform for Parimatch Tech in short terms.

Parimatch Tech has game provider that is sending requests to our platform during player's gameplay.

Your main goal is to implement 2 parts of this platform:

* First part is an "Game Integration Service" system that knows how to parse incoming request from
different game providers, validate requests according to game logic, then send HTTP request to second part of platform.
* Second part is a "Simple Wallet" system that communicates with database to store transactions and
handle balances.

Your team should think about design and create optimal architecture of the platform,
split responsibilities of platform to each appropriate service, and organize your team work in the
best way, so that each team member will spend the same amount of time and effort on their part of the work.

### [1.1. Stack](#-stack)

You should use:

* Servlets to handle and properly route incoming http requests in both services
* JDBC to work with database
* Jackson or JAXB for serialization/deserialization

**You should not use**: Spring Boot

## [2. Game integration service](#-game-integration-service)

Game integration service is 3rd party integration service which main business goal is to:

* adapt proprietary integration API to internal wallet API
* provide request validation logic
* provide game flow validation logic

### [2.1. API & Processing](#-api-processing)

This application should have controller that handles 3 requests:

* getBalance
* bet
* win

#### [2.1.1. Balance example](#-balance-example)

request

```http
GET /game/balance?playerId=42 HTTP/1.1
request-hash-sign: [value-to-validate]
Host: localhost:8080
```

response

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 78

{"status":"success","data":{"playerId":"42","balance":10.00,"currency":"EUR"}}
```

#### [2.1.2. Bet example](#-bet-example)

request

```http
POST /game/bet HTTP/1.1
Content-Type: application/json;charset=UTF-8
request-hash-sign: [value-to-validate]
Content-Length: 125
Host: localhost:8080

{"playerId": "42", "amount": 20.00, "currency": "EUR", "transaction": {"gameId": "fruits", "roundId": "2", "txId": "4321"} }
```

response

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 148

{"status":"success","data":{"playerId":"42","originalTxId":"4321","walletTxId":"c4ca4238a0b923820dcc509a6f75849b","balance":80.00,"currency":"EUR"}}
```

#### [2.1.3. Win example](#-win-example)

request

```http
POST /game/win HTTP/1.1
Content-Type: application/json;charset=UTF-8
request-hash-sign: [value-to-validate]
Content-Length: 127
Host: localhost:8080

{"playerId": "42", "amount": 100.00, "currency": "EUR", "transaction": {"gameId": "fruits", "roundId": "2", "txId": "54321"} }
```

response

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 150

{"status":"success","data":{"playerId":"42","originalTxId":"54321","walletTxId":"c81e728d9d4c2f636f067f89cc14862c","balance":180.00,"currency":"EUR"}}
```

#### [2.1.4. Hash Validation](#-hash-validation)

All request come with header "request-hash-sign" and some value.
Your application should have property secretSalt which is a string and will be used for hash validation

Your application should validate each request in a following way(if secretSalt equals "secret"):

* deserialize incoming request to a bean
* sort bean property values by property names (for example if you have 2 properties-values foo=1, bar=2 - sort result would be bar=2, foo=1)
* concatenate sorted values (example result from above: 21)
* add secretSalt to the end (example result: 21secret)
* hash resulting string using MD5 algorithm and compare to "request-hash-sign" header
* return error if compared values are not equal

#### [2.1.5. Game Flow Validation](#-game-flow-validation)

* When win comes you should check, that there was previously bet for this round.
* You should reject wins without bet for this round.
* You should reject new bets, if round already contain bet.
* You should process any amount of wins to the round.
* You should check that Bet and Win have different transaction Id (corresponding logic you should meet during idempotence check)

Hint: you should store round data in memory so you could check correct sequence of bets and wins

#### [2.1.6. How to process requests](#-how-to-process-requests)

* For "getBalance" request you should send HTTP request to wallet and ask balance,
receive response from wallet, and reply to Provider with player's balance in required format.
* For "bet" request you should validate request according to "Game logic validation rules" (see below).
If validation passed, you should send HTTP request to wallet to process "debit" operation
(pass required fields so that wallet is able to provide idempotence check), receive response from wallet,
and reply to Provider with player's new balance (after transaction) in required format.
* For "win" request you should validate request according to "Game logic validation rules" (see below).
If validation passed, you should send HTTP request to wallet to process "credit" operation
(pass required fields so that wallet is able to provide idempotence check), receive response from wallet,
and reply to Provider with player's new balance (after transaction) in required format.
Do not forget to handle error cases (e.g. when wallet replies with error, when your service failed
to receive response from wallet, some game logic validation failed),
in these cases you should reply o provider with error details in required format.

### [2.2. Error handling](#-error-handling)

You should provide error handling according to provider's requirements.
It means that if you can't process request from provider due to any reason (validation failure, system errors, environment issues with some components, etc.), you should send response to provider with error details in required format.
For error responses provider expect HTTP status code 422 with the following body in JSON format:

```
{
  "status": "errors",
  "message": "Errors, common description",
  "errors": [
    {
      "code": "42",
      "message": "detailed error description"
    }
}
```

Description of each field:

| **status** | always has value "errors" |
| **message** | some generic description of error |
| **errors** | list of errors that happened during transaction processing (can be at least one) |
| **errors[0].code** | some known to provider error code (see list below) |
| **errors[0].message** | description of error related to this error code |

Provider can handle only following error codes:

| 801 - Unexpected error |
| 810 - Security key mismatch |
| 820 - Wrong player Id. |
| 821 - A player is blocked |
| 831 - Insufficient funds |
| 833 - Bet already settled |
| 840 - Bet with specified ID not found |

## [3. Simple Wallet](#-simple-wallet)

Simple wallet is a financial transactions processing service which integrates to database and applies
some games specific on top.

Since simple wallet is so-called internal service(in contrary to game integration which exposes public API) -
you will need to come up with your own API for transaction processing.

### [3.1. API & Processing](#-api-processing-2)

You should design your own API. Which will meet following requirements:

* there should be method for getting a player information along with his current balance
* there should be **_one or more_** methods to commit transactions to database

Simple wallet service should also validate requests.

#### [3.1.1. Validation rules](#-validation-rules)

* On each bet you should open round and store it somewhere.
* if balance is not enough to perform game transaction - you should reject it

#### [3.1.2. Idempotency](#-idempotency)

Idempotency wiki: [https://en.wikipedia.org/wiki/Idempotence](https://en.wikipedia.org/wiki/Idempotence)

Simple wallet should provide idempotency of transaction processing. In simple words it means that 2 transactions with a same parameters should not
affect player's balance twice.

Also Simple Wallet should respond to duplicate transaction with a balance of original transaction.

You should add idempotence validation logic:
If duplicate transaction comes, you should check that playerId, currency and amount are the same as in original transaction.

You should use incoming txId as a key for idempotency(consider it's globally unique).

### [3.2. Database model sample](#-database-model-sample)

Please mention that this is only proposed database model. The only **strict** requirements are:

* postgresql
* database should be normalized
* it should reflect business needs(which means that you have to store entities like player, balances, rounds and transactions)

## [4. Important Notes From Mentors](#-important-notes-from-mentors)

* Mentors will help you organize work and split responsibilities.
* You can ask questions if task is not clear or missing some significant details.
* if you have reviewed and analyzed this task, and you are afraid that you will not have enough time to implement all requirement, then you should split the whole task into smaller subtask, order them according to priority and start implementing the most valuable feature, so that you can implement platform that players can use for game-play, even if it lacks some functionality
* We don't welcome cheating and duplicating code from another teams, this may cause penalty and the whole team may get injured.

**_HAVE FUN AND MAY THE FORCE BE WITH YOU_**
