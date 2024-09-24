package br.com.netdeal.hierarquiafuncionarios.repository;

import br.com.netdeal.hierarquiafuncionarios.model.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findBySupervisorNull();

    List<Funcionario> findBySupervisorId(Long id);
}