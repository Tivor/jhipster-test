'use strict';

describe('Controller Tests', function() {

    describe('Dashboard Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDashboard, MockUser, MockPanel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDashboard = jasmine.createSpy('MockDashboard');
            MockUser = jasmine.createSpy('MockUser');
            MockPanel = jasmine.createSpy('MockPanel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Dashboard': MockDashboard,
                'User': MockUser,
                'Panel': MockPanel
            };
            createController = function() {
                $injector.get('$controller')("DashboardDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dashboardApp:dashboardUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
