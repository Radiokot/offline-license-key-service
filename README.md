# Offline license key service
Web service wrapping the [offline license key library](https://github.com/Radiokot/offline-license-key) 

⚒ Work in progress. Please, come back later.

## Environment variables
|Name|Meaning|
|:-|:-|
|`ISSUER_NAME`|Name of the license keys issuer|
|`ISSUER_PRIVATE_KEY`|Path to a file containing PKCS#8 encoded issuer's private key in PEM text format|
|`ISSUER_PUBLIC_KEY`|Path to a file containing X509 encoded issuer's public key in PEM text format|

See `test_private_key.pem` and `test_public_key.pem` as format example. 