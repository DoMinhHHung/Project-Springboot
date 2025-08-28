package iuh.fit.se.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import iuh.fit.se.dto.request.AuthenticationRequest;
import iuh.fit.se.dto.response.AuthenticationResponse;
import iuh.fit.se.dto.response.IntrospectResponse;
import iuh.fit.se.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${sign_key}")
    private String SIGN_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            var user = userRepository.findByUsername(request.getUsername())
                    .or(() -> userRepository.findByEmail(request.getEmail()))
                    .or(() -> userRepository.findByPhoneNumber(request.getPhoneNumber()))
                    .orElse(null);

            if (user == null) {
                return AuthenticationResponse.builder().authenticated(false).token(null).build();
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()
                    && !request.getEmail().equals(user.getEmail())) {
                return AuthenticationResponse.builder().authenticated(false).token(null).build();
            }

            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()
                    && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
                return AuthenticationResponse.builder().authenticated(false).token(null).build();
            }

            if (request.getPassword() == null ||
                    !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return AuthenticationResponse.builder().authenticated(false).token(null).build();
            }

            var token = generateToken(user.getUsername());
            return AuthenticationResponse.builder().token(token).authenticated(true).build();

        } catch (Exception ex) {
            log.error("Authentication error", ex);
            return AuthenticationResponse.builder().authenticated(false).token(null).build();
        }
    }

    public IntrospectResponse introspect(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            boolean signatureValid = jwsObject.verify(new MACVerifier(SIGN_KEY.getBytes()));
            if (!signatureValid) {
                return IntrospectResponse.builder().valid(false).build();
            }

            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
            Date now = new Date();

            if (claims.getExpirationTime() != null && claims.getExpirationTime().before(now)) {
                return IntrospectResponse.builder().valid(false).build();
            }
            if (claims.getNotBeforeTime() != null && claims.getNotBeforeTime().after(now)) {
                return IntrospectResponse.builder().valid(false).build();
            }

            return IntrospectResponse.builder()
                    .valid(true)
                    .subject(claims.getSubject())
                    .issuer(claims.getIssuer())
                    .issuedAt(claims.getIssueTime())
                    .expiresAt(claims.getExpirationTime())
                    .build();
        } catch (Exception e) {
            log.warn("Token introspection failed", e);
            return IntrospectResponse.builder().valid(false).build();
        }
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Instant now = Instant.now();
        Instant expiry = now.plus(1, ChronoUnit.HOURS);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("ace.com")
                .audience("my-service")
                .jwtID(UUID.randomUUID().toString())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiry))
                .claim("customClaim", "Custom")
                .build();

        JWSObject jwsObject = new JWSObject(header, new Payload(claims.toJSONObject()));

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
