(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('DataRepositoryController', DataRepositoryController);

    DataRepositoryController.$inject = ['$scope', '$state', 'DataRepository'];

    function DataRepositoryController ($scope, $state, DataRepository) {
        var vm = this;
        
        vm.dataRepositories = [];

        loadAll();

        function loadAll() {
            DataRepository.query(function(result) {
                vm.dataRepositories = result;
            });
        }
    }
})();
