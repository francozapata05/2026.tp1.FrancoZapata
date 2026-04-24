package com.bibliotech.service;

import com.bibliotech.exception.DniDuplicadoException;
import com.bibliotech.exception.EmailInvalidoException;
import com.bibliotech.model.Socio;

public interface SocioService {
    void registrar(Socio socio) throws DniDuplicadoException, EmailInvalidoException;
}
