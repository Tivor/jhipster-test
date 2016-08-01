(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('PanelDetailController', PanelDetailController);

    PanelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Panel', 'DataRepository'];

    function PanelDetailController($scope, $rootScope, $stateParams, previousState, entity, Panel, DataRepository) {
        var vm = this;

        vm.panel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dashboardApp:panelUpdate', function(event, result) {
            vm.panel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
