(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('panel', {
            parent: 'entity',
            url: '/panel',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dashboardApp.panel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/panel/panels.html',
                    controller: 'PanelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('panel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('panel-detail', {
            parent: 'entity',
            url: '/panel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dashboardApp.panel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/panel/panel-detail.html',
                    controller: 'PanelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('panel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Panel', function($stateParams, Panel) {
                    return Panel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'panel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('panel-detail.edit', {
            parent: 'panel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panel/panel-dialog.html',
                    controller: 'PanelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Panel', function(Panel) {
                            return Panel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('panel.new', {
            parent: 'panel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panel/panel-dialog.html',
                    controller: 'PanelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                sqlString: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('panel', null, { reload: true });
                }, function() {
                    $state.go('panel');
                });
            }]
        })
        .state('panel.edit', {
            parent: 'panel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panel/panel-dialog.html',
                    controller: 'PanelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Panel', function(Panel) {
                            return Panel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('panel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('panel.delete', {
            parent: 'panel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/panel/panel-delete-dialog.html',
                    controller: 'PanelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Panel', function(Panel) {
                            return Panel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('panel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
