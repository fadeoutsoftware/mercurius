'use strict';

/* Filters */

angular.module('mercurius.filters', []).
    filter('interpolate', ['version', function (version) {
    return function (text) {
        return String(text).replace(/\%VERSION\%/mg, version);
    }
}]).filter('regex', function () {
        return function (input, field, regex) {

            var oPattern = new RegExp(regex, "i");
            var aoResults = [];

            if (typeof input === "undefined") return aoResults;

            for (var i = 0; i < input.length; i++) {
                if (oPattern.test(input[i][field]))
                    aoResults.push(input[i]);
            }
            return aoResults;
        };
    });
