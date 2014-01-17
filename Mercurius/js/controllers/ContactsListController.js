/**
 * Created by p.campanella on 13/01/14.
 */

var ContactsListController = (function() {

    function ContactsListController ($scope, $http, oContactsService) {

        this.m_oScope = $scope;
        this.m_oScope.oController = this;
        this.m_oHttp = $http;
        this.m_aoContacts = [];
        this.m_oContactsService = oContactsService;

        this.m_oContactsService.fetchContacts();

    }


    ContactsListController.prototype.getContactsService = function() {
        return this.m_oContactsService;
    }


    ContactsListController.$inject = [
        '$scope',
        '$http',
        'ContactsService'
    ];

    return ContactsListController;
}) ();