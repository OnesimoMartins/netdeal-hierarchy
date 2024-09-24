angular
  .module("GerenciamentoColaboradores")
  .controller("ColaboradorController",
    [
    "$scope",
    "ColaboradorService",
    function ($scope, ColaboradorService) {

      $scope.colaboradores = [];
      $scope.novoFuncionario = {
          nome:'',
          senha:'',
          supervisorId:null
      };

      $scope.carregarColaboradores = function () {
          $scope.colaboradores = ColaboradorService.listarFuncionarios(
        ).then( (data)=> {
            $scope.colaboradores = data.data
        })
      };

      $scope.criarFuncionario =  () =>{
        ColaboradorService.criarFuncionario($scope.novoFuncionario).then(
           (response)=> {
              $scope.carregarColaboradores()
              $scope.novoFuncionario = {
                  nome:'',
                  senha:'',
                  supervisorId:null
              }
          },
        );
      };



    },
  ]);
