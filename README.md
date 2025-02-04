# Offline license key service

Web service wrapping the [offline license key library](https://github.com/Radiokot/offline-license-key)

⚒ Work in progress. Please, come back later.

Java version: 11

## Environment variables

### Bare minimum, issuance API

| Name                     | Meaning                                                                                                                              |                                                                          
|:-------------------------|:-------------------------------------------------------------------------------------------------------------------------------------|
| `ISSUER_NAME`            | Name of the license keys issuer                                                                                                      |                                                  |
| `ISSUER_PRIVATE_KEY`     | Path to a file containing PKCS#8 encoded issuer's private key in PEM text format                                                     |
| `ISSUER_PUBLIC_KEY`      | Path to a file containing X509 encoded issuer's public key in PEM text format                                                        |
| `KEY_RENEWAL_TIMEOUT`    | Optional, key renewal timeout in ISO-8601 format. For example, `PT6H` means 1 renewal in 6 hours per subject                         |
| `KEY_RENEWAL_TIMEOUT_DB` | Optional, JBDC connection string for a database to store renewal timeouts between restarts. For example, `jdbc:sqlite:D:/timeout.db` |

See `test_private_key.pem` and `test_public_key.pem` as the issuer keys format example.

### Web store based on BTCPay Server

| Name                       | Meaning                                                                                                                                |                                                                          
|:---------------------------|:---------------------------------------------------------------------------------------------------------------------------------------|
| `GREENFIELD_BASE_URL`      | Absolute base URL of BTCPay Greenfield API                                                                                             |
| `GREENFIELD_AUTHORIZATION` | BTCPay Greenfield authorization value (`Token XXXXX`), invoices permission required                                                    |
| `BTCPAY_STORE_ID`          | BTCPay store ID                                                                                                                        |
| `BTCPAY_INVOICE_BASE_URL`  | Absolute base URL of the public BTCPay invoice endpoint                                                                                |
| `BTCPAY_WEBHOOK_SECRET`    | Optional, secret for set up BTCPay webhook for invoice creation and settlement                                                         |
| `PUBLIC_BASE_URL`          | Absolute base URL of the service, for redirects and notifications                                                                      |
| `KEY_ACTIVATION_URI`       | Optional, a URI which opens the app for key activation, for the successful purchase page. <br/> The key is passed as `key` query param |

### Notifications for the store

| Name                                | Meaning                                                                                                |                                                                          
|:------------------------------------|:-------------------------------------------------------------------------------------------------------|
| `MAILJET_ORDER_NOTIFICATIONS`       | Declare to use Mailjet for order notifications. <br/>If declared, the following variables must be set. |                                                  |
| `MAILJET_SENDER_EMAIL`              | Verified sender email address                                                                          |                                                  |
| `MAILJET_SENDER_EMAIL`              | Human-friendly name of the sender                                                                      |                                                  |
| `MAILJET_API_KEY`                   | Primary account API key                                                                                |                                                  |
| `MAILJET_API_SECRET_KEY`            | Primary account API secret key                                                                         |                                                  |
| `MAILJET_PENDING_ORDER_TEMPLATE_ID` | ID of the pending order transactional email template                                                   |                                                  |
| `MAILJET_PAID_ORDER_TEMPLATE_ID`    | ID of the paid order transactional email template                                                      |                                                  |

### Captcha for the store

| Name                              | Meaning                                                                                                                |                                                                          
|:----------------------------------|:-----------------------------------------------------------------------------------------------------------------------|
| `CLOUDFLARE_TURNSTILE`            | Declare to use Cloudflare Turnstile as captcha on the buy page. <br/>If declared, the following variables must be set. |                                                  |
| `CLOUDFLARE_TURNSTILE_SITE_KEY`   | The Site Key value of the configured Turnstile                                                                         |
| `CLOUDFLARE_TURNSTILE_SECRET_KEY` | The Secret Key value of the configured Turnstile                                                                       |
