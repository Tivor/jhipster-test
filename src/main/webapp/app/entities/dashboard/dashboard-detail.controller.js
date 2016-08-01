(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('DashboardDetailController', DashboardDetailController);

    DashboardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dashboard', 'User', 'Panel'];

    function DashboardDetailController($scope, $rootScope, $stateParams, previousState, entity, Dashboard, User, Panel) {
        var vm = this;

        vm.dashboard = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dashboardApp:dashboardUpdate', function(event, result) {
            vm.dashboard = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
