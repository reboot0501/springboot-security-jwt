jwt.io/#debugger-io
[Header]
{
"alg": "HS256",
"typ": "JWT"
}
[PAYLOAD]
{
"sub": "1234567890",
"name": "John Doe",
"iat": 1516239022
}
[Encoded]
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

String rawSignature = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                    +"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
                    +"Signature Key(현프로젝트:cos)";

[JWT]
BASE64(Header).
BASE64(PAYLOAD).
BASE64(HS256암호화(rawSignature))


