/**
 * Created by p.campanella on 13/01/14.
 */

var ContactsViewController = (function () {

    function ContactsViewController($scope,  $stateParams, $location, oContactsService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.m_oLocation = $location;

        var oScope = this.m_oScope;

        oContactsService.getContact( $stateParams.id)
            .success(function(data,status) {
                oScope.m_oController.m_oContact = data;
            })
            .error(function(data,status){
                alert('Error Contacting Mercurius Server (status = ' + status + ')');
            });

    }

    ContactsViewController.prototype.getContact = function() {
        return this.m_oContact;
    }

    ContactsViewController.prototype.sendSms = function() {
        this.m_oLocation.path('/sms/'+this.m_oContact.id);
    }

    ContactsViewController.prototype.sendEMail = function() {
        this.m_oLocation.path('/mail/'+this.m_oContact.id);
    }

    ContactsViewController.$inject = [
        '$scope',
        '$stateParams',
        '$location',
        'ContactsService'
    ];

    return ContactsViewController;
}) ();