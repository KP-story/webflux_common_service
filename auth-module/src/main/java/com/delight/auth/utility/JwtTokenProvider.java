package com.delight.auth.utility;

import com.delight.gaia.auth.jwt.SubjectInfoJwtMapper;
import com.delight.gaia.auth.subject.SubjectInfo;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public class JwtTokenProvider {

    public static String generateHMACToken(String scope, String issuer, SubjectInfo info, String audience, byte[] secretKey, String kId, Date now, Date expiredTime) throws JOSEException {
        Builder payload = SubjectInfoJwtMapper.toClaims(info);
        payload.claim("scope", scope);
        var header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .keyID(kId)
                .build();
        payload.issuer(issuer)
                .audience(audience)
                .issueTime(now)
                .expirationTime(expiredTime);
        var signedJWT = new SignedJWT(header, payload.build());
        var signer = new MACSigner(secretKey);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }


}
