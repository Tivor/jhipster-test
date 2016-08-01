(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .controller('PanelController', PanelController);

    PanelController.$inject = ['$scope', '$state', 'Panel'];

    function PanelController ($scope, $state, Panel) {
        var vm = this;
        
        vm.panels = [];

        loadAll();

        function loadAll() {
            Panel.query(function(result) {
                vm.panels = result;
            });
        }
    }
})();
