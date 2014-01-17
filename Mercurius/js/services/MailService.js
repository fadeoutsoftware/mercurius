/**
 * Created by p.campanella on 16/01/14.
 */
/**
 * Created by p.campanella on 15/01/14.
 */

'use strict';
angular.module('mercurius.mailServiceModule', []).
    service('MailService', ['$http',  function ($http) {
        //this.APIURL = 'http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest';
        this.APIURL = 'http://localhost:8080/it.fadeout.mercurius.webapi/rest';

        this.m_oHttp = $http;

        this.sendToContact = function(id, message) {
            return $http.put(this.APIURL + '/mail/contacts?contactsids='+id, message);
        }
    }]);

