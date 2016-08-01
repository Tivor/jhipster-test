(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('DataRepositoryDetailController', DataRepositoryDetailController);

    DataRepositoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DataRepository'];

    function DataRepositoryDetailController($scope, $rootScope, $stateParams, previousState, entity, DataRepository) {
        var vm = this;

        vm.dataRepository = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dashboardApp:dataRepositoryUpdate', function(event, result) {
            vm.dataRepository = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
