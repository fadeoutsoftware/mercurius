/**
 * Created by p.campanella on 16/01/14.
 */

var MailController = ( function() {
    function MailController($scope, $stateParams, $location, ContactsService, MailService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.m_oLocation = $location;
        this.m_iContactId = $stateParams.id;
        this.m_oMessage = {};
        this.m_sAddress = "";
        this.MailService = MailService;

        var oScope = this.m_oScope;

        ContactsService.getContact(this.m_iContactId)
            .success(function(data,status) {
                oScope.m_oController.m_sAddress = data.email;
            }).error(function(data,status) {
                alert('Error Contacting Mercurius Server (status = '+status+'');
            });
    }

    MailController.prototype.getMessage = function() {
        return this.m_oMessage;
    }

    MailController.prototype.cancel = function() {
        this.m_oLocation.path('/view/' + this.m_iContactId);
    }

    MailController.prototype.send = function() {
        // Questo fa piantare TUTTO
        //this.m_oMessage.creationDate = new Date();
        this.m_oMessage.forwards =  [];
        this.m_oMessage.idMessage = 0;
        //this.m_oMessage.title = "";
        var iContactId = this.m_iContactId;
        var oLocation = this.m_oLocation;


        this.MailService.sendToContact(this.m_iContactId, this.m_oMessage)
            .success(function (data,status) {
                alert('Mail sent');
                oLocation.path('/view/'+ iContactId);
            }).error(function (data,status) {
                alert('Error Sending Mail (status = ' + status + ')');
            });
    }

    MailController.$inject = [
        '$scope',
        '$stateParams',
        '$location',
        'ContactsService',
        'MailService'
    ];

    return MailController;
}) ();