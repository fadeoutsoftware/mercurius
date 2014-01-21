/**
 * Created by p.campanella on 18/01/14.
 */

'use strict';
angular.module('mercurius.userService', []).
    service('UserService', ['$http',  function ($http) {
        //this.APIURL = 'http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest';
        this.APIURL = 'http://localhost:8080/it.fadeout.mercurius.webapi/rest';

        this.m_oHttp = $http;
        this.m_bIsLogged = false;

        this.login = function(userName, password) {

            var oServiceVar = this;

            return this.m_oHttp({method:'GET', headers: {'X-Mercurius-user': userName, 'X-Mercurius-pwd' : password}, url : this.APIURL +'/auth'})
        }

        this.isLogged = function() {
            return this.m_bIsLogged;
        }
    }]);

