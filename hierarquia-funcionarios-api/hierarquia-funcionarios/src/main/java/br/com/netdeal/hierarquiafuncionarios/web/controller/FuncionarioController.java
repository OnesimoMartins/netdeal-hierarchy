package br.com.netdeal.hierarquiafuncionarios.web.controller;

import br.com.netdeal.hierarquiafuncionarios.model.Funcionario;
import br.com.netdeal.hierarquiafuncionarios.model.Senha;
import br.com.netdeal.hierarquiafuncionarios.service.FuncionarioService;
import br.com.netdeal.hierarquiafuncionarios.web.dto.input.FuncionarioInput;
import br.com.netdeal.hierarquiafuncionarios.web.dto.output.FuncionarioOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("funcionarios")
@RestController
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping
    public List<FuncionarioOutput> funcionarios(){
        return funcionarioService.getFuncionarios().stream()
                .map(FuncionarioOutput::fromFuncionario).toList();
    }

    @GetMapping("gerentes")
    public List<FuncionarioOutput> funcionariosGerentes(Pageable pageable){
        return funcionarioService.getGerentes().stream()
                .map(FuncionarioOutput::fromFuncionario).toList();
    }

    @GetMapping("{id}/subordinados")
    public List<FuncionarioOutput> getSubordinados(@PathVariable Long id) {
        return funcionarioService.getSubordinados(id).stream()
                .map(FuncionarioOutput::fromFuncionario).toList();
    }

    @PostMapping
    public FuncionarioOutput novoFuncionario(@RequestBody @Validated
                                           FuncionarioInput funcionarioInput) {
        return FuncionarioOutput.fromFuncionario(
                funcionarioService.novoFuncionario(funcionarioInput.toFuncionario())
        );
    }

}
