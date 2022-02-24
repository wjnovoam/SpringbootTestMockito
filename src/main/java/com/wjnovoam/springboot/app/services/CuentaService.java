package com.wjnovoam.springboot.app.services;

import com.wjnovoam.springboot.app.models.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {
    Cuenta findById(Long id);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId) throws Exception;

    void transferir(Long numCuentaOrige, Long numCuentaDestino, BigDecimal monto,Long bancoId);
}
