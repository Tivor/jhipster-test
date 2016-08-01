(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('DataRepositoryDialogController', DataRepositoryDialogController);

    DataRepositoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DataRepository'];

    function DataRepositoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DataRepository) {
        var vm = this;

        vm.dataRepository = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dataRepository.id !== null) {
                DataRepository.update(vm.dataRepository, onSaveSuccess, onSaveError);
            } else {
                DataRepository.save(vm.dataRepository, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dashboardApp:dataRepositoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
