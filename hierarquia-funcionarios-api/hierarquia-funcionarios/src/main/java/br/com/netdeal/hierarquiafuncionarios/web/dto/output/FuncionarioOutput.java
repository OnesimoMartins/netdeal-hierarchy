package br.com.netdeal.hierarquiafuncionarios.web.dto.output;

import br.com.netdeal.hierarquiafuncionarios.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioOutput {

    private Long id;
    private String nome;
    private int scoreSenha;
    private String senha;
    private Integer version;
    private FuncionarioOutput supervisor;
    private Boolean isSupervisor;

    public static FuncionarioOutput fromFuncionario( Funcionario f){
        return FuncionarioOutput.builder()
                .id(f.getId())
                .nome(f.getNome())
                .senha(f.getSenha().getSenha())
                .isSupervisor(f.getIsSupervisor())
                .scoreSenha(f.getSenha().getSenhaScore())
                .version(f.getVersion())
                .build();
    }
}
