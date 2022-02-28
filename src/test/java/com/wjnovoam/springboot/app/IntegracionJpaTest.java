package com.wjnovoam.springboot.app;

import com.wjnovoam.springboot.app.datos.Datos;
import com.wjnovoam.springboot.app.exceptions.BancoNoExisteException;
import com.wjnovoam.springboot.app.exceptions.CuentaNoExisteException;
import com.wjnovoam.springboot.app.models.Banco;
import com.wjnovoam.springboot.app.models.Cuenta;
import com.wjnovoam.springboot.app.repositories.BancoRepository;
import com.wjnovoam.springboot.app.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    BancoRepository bancoRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andres", cuenta.get().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Andres");
        assertTrue(cuenta.isPresent());
        assertEquals("Andres", cuenta.get().getPersona());
        assertEquals("1000.00", cuenta.get().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
        assertThrows(NoSuchElementException.class, ()-> {
            cuenta.orElseThrow(()-> new NoSuchElementException("La persona no existe"));
        });
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testFindByCuentaIdThrowExceptions() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(20L);

        assertThrows(CuentaNoExisteException.class, ()-> {
            cuenta.orElseThrow(()-> new CuentaNoExisteException("La cuenta no existe"));
        });
        assertFalse(cuenta.isPresent());

    }

    @Test
    void testFindByBancoIdThrowExceptions() {
         Optional<Banco> banco = bancoRepository.findById(20L);

        assertThrows(BancoNoExisteException.class, ()-> {
            banco.orElseThrow(()-> new BancoNoExisteException("El banco no xiste"));
        });
        assertFalse(banco.isPresent());

    }

    @Test
    void testSave() {
        //Given
        Cuenta cuentaPepe = new Cuenta(null, "pepe", new BigDecimal("3000"));
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);

        //when
        //Optional<Cuenta> cuenta = cuentaRepository.findByPersona("pepe").get();
        //Optional<Cuenta> cuenta = cuentaRepository.findById(cuentasave.getId()).get();

        //Then
        //assertTrue(cuenta.isPresent());
        assertEquals("pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testUpdate() {
        //Given
        Cuenta cuentaPepe = new Cuenta(null, "pepe", new BigDecimal("3000"));

        //when
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);

        //Then
        //assertTrue(cuenta.isPresent());
        assertEquals("pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());

        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        assertEquals("pepe", cuentaActualizada.getPersona());
        assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());

    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).get();
        assertEquals("Jhon", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, ()-> {
            cuentaRepository.findById(2L).orElseThrow(()-> new NoSuchElementException("La persona no existe"));
        });

        assertEquals(1, cuentaRepository.findAll().size());

    }
}
