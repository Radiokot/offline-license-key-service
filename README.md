# Offline license key service
Web service wrapping the [offline license key library](https://github.com/Radiokot/offline-license-key) 

⚒ Work in progress. Please, come back later.

## Environment variables
| Name                       | Meaning                                                                             |                                                                          
|:---------------------------|:------------------------------------------------------------------------------------|
| `ISSUER_NAME`              | Name of the license keys issuer                                                     |                                                  |
| `ISSUER_PRIVATE_KEY`       | Path to a file containing PKCS#8 encoded issuer's private key in PEM text format    |
| `ISSUER_PUBLIC_KEY`        | Path to a file containing X509 encoded issuer's public key in PEM text format       |
| `GREENFIELD_BASE_URL`      | Absolute base URL of BTCPay Greenfield API                                          |
| `GREENFIELD_AUTHORIZATION` | BTCPay Greenfield authorization value (`Token XXXXX`), invoices permission required |
| `BTCPAY_STORE_ID`          | BTCPay store ID                                                                     |
| `BTCPAY_INVOICE_BASE_URL`  | Absolute base URL of the public BTCPay invoice endpoint                             |
| `PUBLIC_BASE_URL`          | Absolute base URL of the service, for redirects and notifications                   |

### Notifications
| Name                                | Meaning                                                                                                |                                                                          
|:------------------------------------|:-------------------------------------------------------------------------------------------------------|
| `MAILJET_ORDER_NOTIFICATIONS`       | Declare to use Mailjet for order notifications. <br/>If declared, the following variables must be set. |                                                  |
| `MAILJET_SENDER_EMAIL`              | Verified sender email address                                                                          |                                                  |
| `MAILJET_SENDER_EMAIL`              | Human-friendly name of the sender                                                                      |                                                  |
| `MAILJET_API_KEY`                   | Primary account API key                                                                                |                                                  |
| `MAILJET_API_SECRET_KEY`            | Primary account API secret key                                                                         |                                                  |
| `MAILJET_PENDING_ORDER_TEMPLATE_ID` | ID of the pending order transactional email template                                                   |                                                  |


See `test_private_key.pem` and `test_public_key.pem` as format example. 
