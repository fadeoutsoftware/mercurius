/**
 * Created by p.campanella on 14/01/14.
 */

'use strict';
angular.module('mercurius.contactsServiceModule', []).
    service('ContactsService', ['$http', 'UserService',  function ($http, UserService) {
        //this.APIURL = 'http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest';
        this.APIURL = 'http://localhost:8080/it.fadeout.mercurius.webapi/rest';

        this.m_oHttp = $http;
        this.m_aoContacts = [];
        this.m_aoFlatContacts = [];
        this.m_oUserService = UserService;

        this.fetchContacts = function() {

            var oServiceVar = this;

            oServiceVar.m_aoFlatContacts = [];

            if (this.m_oUserService.isLogged() == false) return;

            this.m_oHttp({method:'GET',url : this.APIURL +'/uicontacts/orderedall'})
                .success(function(data,status) {
                    oServiceVar.m_aoContacts = data;

                    for (var iChars = 0; iChars < oServiceVar.m_aoContacts.length; iChars++) {
                        var oContactSet = oServiceVar.m_aoContacts[iChars].contactListItems;
                        for (var iContacts = 0; iContacts < oContactSet.length; iContacts++) {
                            oServiceVar.m_aoFlatContacts.push(oContactSet[iContacts]);
                        }
                    }

                })
                .error(function(data,status){
                    alert('Error Contacting Mercurius Server');
                });
        }

        this.getContacts = function() {
            return this.m_aoContacts;
        }

        this.getContact = function(id) {
            return $http.get(this.APIURL + '/uicontacts/'+id );
        }

        this.getFlatContacts = function() {
            return this.m_aoFlatContacts;
        }

        this.addContact = function(contactdata) {
            return $http.put(this.APIURL + '/uicontacts', contactdata);
        }

        this.updateContact = function(contactdata) {
            return $http.post(this.APIURL+'/uicontacts',contactdata);
        }

        this.deleteContact = function(id) {
            return $http.delete(this.APIURL + '/uicontacts/' + id);
        }
    }]);

