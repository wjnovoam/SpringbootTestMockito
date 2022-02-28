package com.wjnovoam.springboot.app.services;

import com.wjnovoam.springboot.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    List<Cuenta> findAll();

    Cuenta findById(Long id);

    Cuenta save(Cuenta cuenta);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId) throws Exception;

    void transferir(Long numCuentaOrige, Long numCuentaDestino, BigDecimal monto,Long bancoId);
}
