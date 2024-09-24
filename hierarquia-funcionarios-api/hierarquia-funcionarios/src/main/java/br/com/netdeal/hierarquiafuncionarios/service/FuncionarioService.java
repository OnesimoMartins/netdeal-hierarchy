package br.com.netdeal.hierarquiafuncionarios.service;

import br.com.netdeal.hierarquiafuncionarios.model.Funcionario;
import br.com.netdeal.hierarquiafuncionarios.repository.FuncionarioRepository;
import br.com.netdeal.hierarquiafuncionarios.service.senha.GeradorSenha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final GeradorSenha geradorSenha;
    private final FuncionarioRepository funcionarioRepository;

    public List<Funcionario> getFuncionarios(){
        return funcionarioRepository.findAll();
    }

    public List<Funcionario> getGerentes(){
        return funcionarioRepository.findBySupervisorNull();
    }

    public List<Funcionario> getSubordinados(Long id){
        return funcionarioRepository.findBySupervisorId(id);
    }

    public Funcionario novoFuncionario(Funcionario funcionario) {

       var score = geradorSenha
               .getSenhaScore(funcionario.getSenha().getSenha());

       funcionario.getSenha().setSenhaScore(score);
       var senhaEncriptada =geradorSenha.gerarEncriptacao(funcionario.getSenha().getSenha());
       funcionario.getSenha().setSenha(senhaEncriptada);
       funcionario.setVersion(0);

       if(Objects.nonNull(funcionario.getSupervisor())) {
           var supervisor = funcionarioRepository.getById(funcionario.getSupervisor().getId());
           supervisor.setIsSupervisor(true);
           funcionarioRepository.save(supervisor);
           funcionario.setSupervisor(supervisor);
       }
       funcionario = funcionarioRepository.save(funcionario);

        return funcionario;
    }
}
