package com.wjnovoam.springboot.app.repositories;

import com.wjnovoam.springboot.app.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    //List<Banco> findAll();

    //Banco findById(Long id);

    //void update(Banco banco);
}
