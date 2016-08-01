(function() {
    'use strict';
    angular
        .module('dashboardApp')
        .factory('DataRepository', DataRepository);

    DataRepository.$inject = ['$resource'];

    function DataRepository ($resource) {
        var resourceUrl =  'api/data-repositories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
