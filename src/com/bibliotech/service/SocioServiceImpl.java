package com.bibliotech.service;

import com.bibliotech.exception.DniDuplicadoException;
import com.bibliotech.exception.EmailInvalidoException;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.SocioRepository;

public class SocioServiceImpl implements SocioService {
    private final SocioRepository repositorio;

    public SocioServiceImpl(SocioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrar(Socio socio) throws DniDuplicadoException, EmailInvalidoException {
        if (repositorio.buscarPorId(socio.getDni()).isPresent()) {
            throw new DniDuplicadoException(socio.getDni());
        }
        if (!emailValido(socio.getEmail())) {
            throw new EmailInvalidoException(socio.getEmail());
        }
        repositorio.guardar(socio);
    }

    private boolean emailValido(String email) {
        int arroba = email.indexOf("@");
        return arroba > 0 && email.indexOf(".", arroba) > arroba + 1;
    }
}
