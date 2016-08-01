(function() {
    'use strict';

    angular
        .module('dashboardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('data-repository', {
            parent: 'entity',
            url: '/data-repository',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dashboardApp.dataRepository.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-repository/data-repositories.html',
                    controller: 'DataRepositoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dataRepository');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('data-repository-detail', {
            parent: 'entity',
            url: '/data-repository/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dashboardApp.dataRepository.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-repository/data-repository-detail.html',
                    controller: 'DataRepositoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dataRepository');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DataRepository', function($stateParams, DataRepository) {
                    return DataRepository.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'data-repository',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('data-repository-detail.edit', {
            parent: 'data-repository-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-repository/data-repository-dialog.html',
                    controller: 'DataRepositoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataRepository', function(DataRepository) {
                            return DataRepository.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-repository.new', {
            parent: 'data-repository',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-repository/data-repository-dialog.html',
                    controller: 'DataRepositoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                jdbcUrl: null,
                                jdbcDriver: null,
                                dbUser: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('data-repository', null, { reload: true });
                }, function() {
                    $state.go('data-repository');
                });
            }]
        })
        .state('data-repository.edit', {
            parent: 'data-repository',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-repository/data-repository-dialog.html',
                    controller: 'DataRepositoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataRepository', function(DataRepository) {
                            return DataRepository.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-repository', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-repository.delete', {
            parent: 'data-repository',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-repository/data-repository-delete-dialog.html',
                    controller: 'DataRepositoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DataRepository', function(DataRepository) {
                            return DataRepository.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-repository', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
