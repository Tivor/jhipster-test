(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('PanelDialogController', PanelDialogController);

    PanelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Panel', 'DataRepository'];

    function PanelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Panel, DataRepository) {
        var vm = this;

        vm.panel = entity;
        vm.clear = clear;
        vm.save = save;
        vm.datarepositories = DataRepository.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.panel.id !== null) {
                Panel.update(vm.panel, onSaveSuccess, onSaveError);
            } else {
                Panel.save(vm.panel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dashboardApp:panelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
