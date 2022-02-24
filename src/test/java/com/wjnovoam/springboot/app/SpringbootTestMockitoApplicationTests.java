package com.wjnovoam.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.wjnovoam.springboot.app.datos.Datos;
import com.wjnovoam.springboot.app.exceptions.DineroInsuficienteException;
import com.wjnovoam.springboot.app.models.Banco;
import com.wjnovoam.springboot.app.models.Cuenta;
import com.wjnovoam.springboot.app.repositories.BancoRepository;
import com.wjnovoam.springboot.app.repositories.CuentaRepository;
import com.wjnovoam.springboot.app.services.CuentaService;
import com.wjnovoam.springboot.app.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestMockitoApplicationTests {

    @MockBean
    CuentaRepository cuentaRepository;
    @MockBean
    BancoRepository bancoRepository;

    @Autowired //Cuando lo utilizamos, es bueno tener el Implementacion
    CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        //cuentaRepository = mock(CuentaRepository.class);
        //bancoRepository = mock(BancoRepository.class);
        //cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);
    }

    @Test
    void contextLoads() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
        when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
        when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        cuentaService.transferir(1L, 2L, new BigDecimal("100"), 1L);

        saldoOrigen = cuentaService.revisarSaldo(1L);
        saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("900", saldoOrigen.toPlainString());
        assertEquals("2100", saldoDestino.toPlainString());

        int total =  cuentaService.revisarTotalTransferencias(1L);
        assertEquals(1, total);

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(3)).findById(2L);
        verify(cuentaRepository, times(2)).update(any(Cuenta.class));

        verify(bancoRepository, times(2)).findById(anyLong());
        verify(bancoRepository).update(any(Banco.class));

        verify(cuentaRepository, times(6)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();
    }


    @Test
    void contextLoads2() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
        when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
        when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        assertThrows(DineroInsuficienteException.class, ()-> {
            cuentaService.transferir(1L, 2L, new BigDecimal("1200"), 1L);
        });

        saldoOrigen = cuentaService.revisarSaldo(1L);
        saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        int total =  cuentaService.revisarTotalTransferencias(1L);
        assertEquals(0, total);

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(2)).findById(2L);
        verify(cuentaRepository, never()).update(any(Cuenta.class));

        verify(bancoRepository, times(1)).findById(1L);
        verify(bancoRepository, never()).update(any(Banco.class));

        verify(cuentaRepository, times(5)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();
    }

    @Test
    void contextLoad3() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

        Cuenta cuenta1 = cuentaService.findById(1L);
        Cuenta cuenta2 = cuentaService.findById(1L);


        assertSame(cuenta1, cuenta2);
        //assertTrue(cuenta1 == cuenta2);
        assertEquals("Andres", cuenta1.getPersona());
        assertEquals("Andres", cuenta2.getPersona());

        verify(cuentaRepository, times(2)).findById(1L);

    }


}
