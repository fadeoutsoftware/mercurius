/**
 * Created by p.campanella on 15/01/14.
 */

'use strict';
angular.module('mercurius.smsServiceModule', []).
    service('SmsService', ['$http',  function ($http) {
        //this.APIURL = 'http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest';
        this.APIURL = 'http://localhost:8080/it.fadeout.mercurius.webapi/rest';

        this.m_oHttp = $http;

        this.sendToContact = function(id, message) {
            return $http.put(this.APIURL + '/sms/contacts?contactsids='+id, message);
        }
    }]);

