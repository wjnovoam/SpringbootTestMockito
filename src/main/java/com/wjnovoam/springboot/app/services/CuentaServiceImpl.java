package com.wjnovoam.springboot.app.services;

import com.wjnovoam.springboot.app.exceptions.BancoNoExisteException;
import com.wjnovoam.springboot.app.exceptions.CuentaNoExisteException;
import com.wjnovoam.springboot.app.models.Banco;
import com.wjnovoam.springboot.app.models.Cuenta;
import com.wjnovoam.springboot.app.repositories.BancoRepository;
import com.wjnovoam.springboot.app.repositories.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CuentaServiceImpl implements CuentaService{

    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow(()-> new BancoNoExisteException("El banco no xiste"));
        return banco.getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId){
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(Long numCuentaOrige, Long numCuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrige).orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));;
        cuentaOrigen.debito(monto);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino).orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));;
        cuentaDestino.credito(monto);
        cuentaRepository.save(cuentaDestino);

        Banco banco = bancoRepository.findById(bancoId).orElseThrow(()-> new BancoNoExisteException("El banco no xiste"));
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.save(banco);
    }
}
