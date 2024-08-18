# Password Storage Security Demo

This Spring Boot application demonstrates the importance of secure password storage by showcasing both secure and insecure methods. It's designed as an educational tool for developers to understand the risks associated with improper password handling.

## Overview

The application provides a simple REST API with the following features:
- User signup (with secure or insecure password storage)
- Listing all users (including their password hashes)
- Switching between secure and insecure password storage modes

## Security Caveat

**Important:** The list-users functionality in this demo is intentionally insecure as it displays password hashes. In a real-world application, this would be a significant security risk. This is included purely for educational purposes to show the difference between secure and insecure hashing methods.

## Getting Started

### Prerequisites

- Docker

### Running the Application

1. Clone this repository:
   ```
   git clone https://github.com/yourusername/password-demo.git
   cd password-demo
   ```

2. Build the Docker image:
   ```
   docker build -t password-demo .
   ```

3. Run the container:
   ```
   docker run -p 8880:8880 password-demo
   ```

The application will be available at `http://localhost:8880`.

## API Endpoints

1. Signup: `POST /api/users/signup`
   ```json
   {
     "name": "John Doe",
     "email": "john@example.com",
     "password": "secretpassword"
   }
   ```

2. List Users: `GET /api/users`

3. Set Security Mode: `POST /api/users/mode`
   ```json
   {
     "secureMode": true
   }
   ```

## Password Security Implementation

### Secure Mode (BCrypt)

In secure mode, the application uses BCrypt, a strong adaptive hash function designed for password hashing:

```java
private String hashPasswordSecure(String password) {
    return bCryptPasswordEncoder.encode(password);
}
```

BCrypt automatically handles salt generation and incorporates the salt into the final hash. It's also intentionally slow, which helps protect against brute-force attacks.

### Insecure Mode (SHA-1)

In insecure mode, the application uses SHA-1, which is not suitable for password hashing:

```java
private String hashPasswordInsecure(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Failed to hash password", e);
    }
}
```

SHA-1 is a fast hash function, making it vulnerable to brute-force attacks. It also doesn't use a salt, which makes it susceptible to rainbow table attacks.

## Security Considerations

1. Never store passwords in plain text.
2. Use a strong, slow hash function designed for password hashing (e.g., BCrypt, Argon2, PBKDF2).
3. Always use salts when hashing passwords to protect against rainbow table attacks.
4. Never display or transmit password hashes, even to administrators.
5. Implement proper access controls and authentication mechanisms.
6. Use HTTPS to encrypt data in transit.

## Demonstrating Insecure Hashing with Crackstation

This demo uses Crackstation.net, an online password cracking service, to illustrate the vulnerability of weak hashing algorithms like SHA-1. We're using this service for educational purposes only, to demonstrate how quickly insecure hashes can be cracked compared to secure ones.

### Using Crackstation in the Demo

1. Obtain a SHA-1 hash from the list-users endpoint:
   ```
   curl http://localhost:8880/api/users
   ```
   Copy one of the SHA-1 hashes from the response.

2. Visit https://crackstation.net/ in your web browser.

3. Paste the SHA-1 hash into the input field and complete the CAPTCHA.

4. Click the "Crack Hashes" button to see if the hash can be cracked.

### Why SHA-1 is Vulnerable

SHA-1 is a cryptographic hash function that is no longer considered secure for password hashing. It's vulnerable because:

1. It's fast to compute, allowing attackers to make billions of guesses per second.
2. It doesn't use a salt, making it vulnerable to rainbow table attacks.
3. Collisions have been found, further weakening its security.

Crackstation and similar services can often crack SHA-1 hashes of common passwords in seconds, demonstrating why it's crucial to use secure hashing algorithms in real applications.

### The Importance of Secure Hashing

This demonstration highlights why it's critical to use secure, purpose-built password hashing functions like bcrypt in real-world applications. Unlike SHA-1, bcrypt:

1. Is intentionally slow, making large-scale guessing attacks impractical.
2. Incorporates a salt automatically, protecting against rainbow table attacks.
3. Is adaptive, allowing increases in iterations as hardware improves.

### Ethical Considerations

**Warning:** This demonstration is for educational purposes only. Attempting to crack passwords without authorization is illegal and unethical. Always obtain proper authorization before testing or demonstrating security vulnerabilities.

## Conclusion

This demo illustrates the stark difference between secure and insecure password storage methods. In real-world applications, always use secure methods like BCrypt, and never expose password hashes. Remember, security is an ongoing process, and it's crucial to stay updated with the latest best practices and potential vulnerabilities.
