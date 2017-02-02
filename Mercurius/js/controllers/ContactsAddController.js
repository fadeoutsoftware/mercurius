/**
 * Created by p.campanella on 13/01/14.
 */

var ContactsAddController = (function () {

    function ContactsAddController($scope, $modal, $location, $http, oUserService, oContactsService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.m_oContact = {};
        this.m_oLocation = $location;
        this.m_oHttp = $http;
        this.m_oUserService = oUserService;
        this.m_oContactsService = oContactsService;

        if (this.m_oUserService.isLogged()==false) {
            var oController = this;
            var modalInstance = $modal.open( {
                    templateUrl: 'partials/login.html',
                    controller: LoginController,
                    backdrop: 'static',
                    resolve: {
                        oUserService:  function() {
                            return oController.m_oUserService;
                        }
                    }
            });

            var oLocation = this.m_oLocation;
            var oContactsService = this.m_oContactsService;
            modalInstance.result.then(function() {
                oContactsService.fetchContacts();
                oLocation.path('#');
            });
        }
    }

    ContactsAddController.prototype.getContact = function() {
        return this.m_oContact;
    }

    ContactsAddController.prototype.save = function (contactdata) {
        var oLocation = this.m_oLocation;
        var oContactsService = this.m_oContactsService;

        if (this.m_oUserService.isLogged()==false) return;

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
        '$modal',
        '$location',
        '$http',
        'UserService',
        'ContactsService'
    ];

    return ContactsAddController;
}) ();


