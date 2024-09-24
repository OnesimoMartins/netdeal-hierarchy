angular.module('GerenciamentoColaboradores').config([
  '$routeProvider',
  function ($routeProvider) {
    $routeProvider
      .when("/", {
        templateUrl: "src/views/colaborador.template.html",
        controller: "ColaboradorController"
      })
      .otherwise({
        redirectTo: "/",
      });
  },
])
