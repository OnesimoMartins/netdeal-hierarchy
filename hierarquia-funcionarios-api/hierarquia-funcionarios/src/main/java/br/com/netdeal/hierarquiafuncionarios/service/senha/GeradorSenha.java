package br.com.netdeal.hierarquiafuncionarios.service.senha;

public interface GeradorSenha {

    String reverterEncriptacao(String senha);
    String gerarEncriptacao(String senha);
    int getSenhaScore(String senha);
}
