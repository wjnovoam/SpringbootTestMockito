package com.wjnovoam.springboot.app.services;

import com.wjnovoam.springboot.app.exceptions.BancoNoExisteException;
import com.wjnovoam.springboot.app.exceptions.CuentaNoExisteException;
import com.wjnovoam.springboot.app.models.Banco;
import com.wjnovoam.springboot.app.models.Cuenta;
import com.wjnovoam.springboot.app.repositories.BancoRepository;
import com.wjnovoam.springboot.app.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService{

    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true) //Que solo haga consultas
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cuentaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow(()-> new BancoNoExisteException("El banco no xiste"));
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId){
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));
        return cuenta.getSaldo();
    }

    @Override
    @Transactional //Que puede hacer get, delete, post, update
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
