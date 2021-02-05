package br.com.security.jwt.service;

import org.springframework.stereotype.Service;

@Service
public interface ILoginService<P, R> {

    R login(P param);
}
