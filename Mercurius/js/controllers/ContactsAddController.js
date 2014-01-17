/**
 * Created by p.campanella on 13/01/14.
 */

var ContactsAddController = (function () {

    function ContactsAddController($scope, $location, $http, oContactsService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.APIURL = 'http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest';
        this.m_oContact = {};
        this.m_oLocation = $location;
        this.m_oHttp = $http;
        this.m_oContactsService = oContactsService;
    }

    ContactsAddController.prototype.getContact = function() {
        return this.m_oContact;
    }

    ContactsAddController.prototype.save = function (contactdata) {
        var oLocation = this.m_oLocation;
        var oContactsService = this.m_oContactsService;

        oContactsService.addContact(contactdata)
            .success(function(data,status) {
                oContactsService.fetchContacts();
                var id = data.IntValue;
                oLocation.path('/view/' + id);
            })
            .error(function(data,status){
                alert('Error Contacting Mercurius Server (status = ' + status + ')');
            });
    }

    ContactsAddController.$inject = [
        '$scope',
        '$location',
        '$http',
        'ContactsService'
    ];

    return ContactsAddController;
}) ();


