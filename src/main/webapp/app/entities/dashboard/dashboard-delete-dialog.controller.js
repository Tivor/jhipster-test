(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('DashboardDeleteController',DashboardDeleteController);

    DashboardDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dashboard'];

    function DashboardDeleteController($uibModalInstance, entity, Dashboard) {
        var vm = this;

        vm.dashboard = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dashboard.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
