(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('DataRepositoryDeleteController',DataRepositoryDeleteController);

    DataRepositoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'DataRepository'];

    function DataRepositoryDeleteController($uibModalInstance, entity, DataRepository) {
        var vm = this;

        vm.dataRepository = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DataRepository.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
