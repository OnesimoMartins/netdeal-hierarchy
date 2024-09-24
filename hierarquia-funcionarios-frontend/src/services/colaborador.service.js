angular.module("GerenciamentoColaboradores").factory("ColaboradorService", [
  "$http",
  "$q",
  "$timeout",
  function ($http, $q, $timeout) {
    var apiUrl = "http://localhost:8080/";

    let service = {

      listarSubordinados: function listarSubordinados(funcionarioId) {
      return $http.get(`${apiUrl}funcionarios/${funcionarioId}/subordinados`)
          .then((response) => response.data);
      },

      listarFuncionarios: function () {
        return $http.get(`${apiUrl}funcionarios/gerentes`)
            .then(response => {
              return   response
            })
      },
      criarFuncionario: (funcionario)=> {
        return $http.post(`${apiUrl}funcionarios`, funcionario)
            .then((response) => response.data);
      },
    };

    return service;
  },

]);
