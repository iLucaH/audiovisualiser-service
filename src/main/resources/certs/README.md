# Certification
Create your own base64 public and private keys and add them to your environment variables as:
* AV_PUBLIC_KEY
* AV_PRIVATE_KEY

### Asymmetric key generation and conversion to base64: 
* openssl genrsa -out keypair.pem 2048
* openssl rsa -in keypair.pem -pubout -out public.pem
* openssl pkcs8 -topk8 -nocrypt -in private.pem -outform DER | base64 -w 0 > private.b64
* openssl rsa -pubin -in public.pem -outform DER | base64 -w 0 > public.b64