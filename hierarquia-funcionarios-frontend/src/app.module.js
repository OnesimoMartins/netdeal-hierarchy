angular.module('GerenciamentoColaboradores', [
  'ngRoute',
])
    .component(
    'detalheFuncionario',
    {
      bindings: {
          funcionario:'<'
      },
      template:` 
      <button class="accordion" ng-click="click($ctrl.funcionario,$event)">
       {{$ctrl.funcionario.nome}} 
       <span class="password-percentage">
       {{$ctrl.funcionario.scoreSenha}}%
       </span>
        <span class="password-strength">
          {{
            $ctrl.funcionario.scoreSenha > 90 ? 'Forte' : 
            $ctrl.funcionario.scoreSenha > 59 ? 'Bom' : 
            $ctrl.funcionario.scoreSenha > 44 ? 'Medina' :
            'Ruim' 
          }}
       </span>
      </button>
       <div  ng-repeat="f in subordinados" id="{{$ctrl.funcionario.id}}" class="children">
         <detalhe-funcionario funcionario="f"></detalhe-funcionario>
       </div>
`,
      controller: function ( $scope,$window,ColaboradorService) {
          $scope.click =  function click(funcionario,$event){
              if (!funcionario.isSupervisor){
                  return
              }
              ColaboradorService.listarSubordinados(funcionario.id)
                  .then((data)=>{
                      $scope.subordinados = data
                      const btn = $event.target
                      btn.classList.toggle("active");
                  })
          }
      },
    }
)
