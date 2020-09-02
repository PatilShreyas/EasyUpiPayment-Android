# Getting Transaction Details

Once transaction is completed, `onTransactionCompleted()` gets invoked (as we seen in previous section). In that callback, you'll get instance of `TransactionDetails`. 

`TransactionDetails` instance includes details about previously completed transaction.
To get details, below method of TransactionDetails are useful:

<table>
  <tr>
    <th>Kotlin (Field)</th>
    <th>Java (Method)</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><code>transactionId</code></td>
    <td><code>getTransactionId()</code></td>
    <td>Returns Transaction ID</td>
  </tr>
  <tr>
    <td><code>responseCode</code></td>
    <td><code>getResponseCode()</code></td>
    <td>Returns UPI Response Code</td>
  </tr>
  <tr>
    <td><code>approvalRefNo</code></td>
    <td><code>getApprovalRefNo()</code></td>
    <td>Returns UPI Approval Reference Number (beneficiary)</td>
  </tr>
  <tr>
    <td><code>transactionStatus</code></td>
    <td><code>getTransactionStatus()</code></td>
    <td>Returns Status of transaction.<br>(SUBMITTED/SUCCESS/FAILURE)<br></td>
  </tr>
  <tr>
  <td><code>transactionRefId</code></td>
    <td><code>getTransactionRefId()</code></td>
    <td>Returns Transaction reference ID passed in input</td>
  </tr>
  <tr>
    <td><code>amount</code></td>
    <td><code>getAmount</code></td>
    <td>Returns Transaction amount</td>
  </tr>
</table>

---

In *TransactionStatus*, you'll get ENUM among `SUBMITTED`, `SUCCESS` and `FAILURE`. Their meaning is:

- `TransactionStatus.SUBMITTED` - Transaction is in ***PENDING*** state. Money might deducted from user's account but not deposited in payee's account.
- `TransactionStatus.SUCCESS` - Transaction is successful.
- `TransactionStatus.FAILED` - Transaction is failed.
