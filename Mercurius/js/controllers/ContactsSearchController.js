/**
 * Created by p.campanella on 13/01/14.
 */

var ContactsSearchController = (function () {

    function ContactsSearchController($scope,  oContactsService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.m_oContactService = oContactsService;

    }

    ContactsSearchController.prototype.getContacts = function() {
        return this.m_oContactService.getContacts();
    }

    ContactsSearchController.prototype.getFlatContacts = function() {
        return this.m_oContactService.getFlatContacts();
    }

    ContactsSearchController.$inject = [
        '$scope',
        'ContactsService'
    ];

    return ContactsSearchController;
}) ();