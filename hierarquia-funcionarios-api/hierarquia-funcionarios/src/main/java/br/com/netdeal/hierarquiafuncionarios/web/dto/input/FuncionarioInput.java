package br.com.netdeal.hierarquiafuncionarios.web.dto.input;

import br.com.netdeal.hierarquiafuncionarios.model.Funcionario;
import br.com.netdeal.hierarquiafuncionarios.model.Senha;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FuncionarioInput{

    @NotBlank
    private String nome;

    @NotBlank
    private String senha;

    private Long supervisorId;

    private Integer version;

    public Funcionario toFuncionario(){
        var funcionario = Funcionario.builder()
                .senha(Senha.builder().senha(senha).build())
                .nome(nome)
                .version(version)
                .build();

        if(supervisorId != null){
            funcionario.setSupervisor(Funcionario.builder().id(supervisorId).build());
        }
        return funcionario;
    }
}
