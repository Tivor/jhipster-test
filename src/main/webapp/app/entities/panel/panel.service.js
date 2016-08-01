(function() {
    'use strict';
    angular
        .module('dashboardApp')
        .factory('Panel', Panel);

    Panel.$inject = ['$resource'];

    function Panel ($resource) {
        var resourceUrl =  'api/panels/:id';

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
