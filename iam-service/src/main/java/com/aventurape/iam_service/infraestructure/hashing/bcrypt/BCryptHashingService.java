package com.aventurape.iam_service.infraestructure.hashing.bcrypt;

import com.aventurape.iam_service.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
