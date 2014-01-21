/**
 * Created by p.campanella on 13/01/14.
 */

var ContactsEditController = (function () {

    function ContactsEditController($scope, $stateParams, $location, $http, oContactsService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.m_oContact = null;
        this.m_oLocation = $location;
        this.m_oHttp = $http;
        this.m_oContactService = oContactsService;

        var oScope = this.m_oScope;

        oContactsService.getContact($stateParams.id)
            .success(function(data,status) {
                oScope.m_oController.m_oContact = data;
            })
            .error(function(data,status){
                alert('Error Contacting Mercurius Server (status = ' + status + ')');
            });

    }

    ContactsEditController.prototype.getContact = function() {
        return this.m_oContact;
    }

    ContactsEditController.prototype.update = function (contactdata) {
        var oLocation = this.m_oLocation;
        var oContactData = contactdata;
        var oContactService = this.m_oContactService;

        oContactService.updateContact(contactdata)
            .success(function(data,status) {
                oContactService.fetchContacts();
                oLocation.path('/view/' + oContactData.id);
            })
            .error(function(data,status){
                alert('Error Contacting Mercurius Server (status = ' + status + ')');
        });
    }

    ContactsEditController.prototype.delete = function (id) {

        var oLocation = this.m_oLocation;
        var oContactService = this.m_oContactService;

        oContactService.deleteContact(id)
            .success(function(data,status) {
                oContactService.fetchContacts();
                oLocation.path('/');
        })
        .error(function(data,status){
                alert('Error Contacting Mercurius Server (status = ' + status + ')');
        });
    }

    ContactsEditController.$inject = [
        '$scope',
        '$stateParams',
        '$location',
        '$http',
        'ContactsService'
    ];

    return ContactsEditController;
}) ();


