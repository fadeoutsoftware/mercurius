/**
 * Created by p.campanella on 15/01/14.
 */

var SmsController =  (function() {
    function SmsController($scope, $stateParams, $location, ContactsService,SmsService) {
        this.m_oScope = $scope;
        this.m_oScope.m_oController = this;
        this.m_oLocation = $location;
        this.m_iContactId = $stateParams.id;
        this.m_oMessage = {};
        this.m_sAddress="";
        this.SmsService = SmsService;

        var oScope = this.m_oScope;

        ContactsService.getContact(this.m_iContactId)
            .success(function(data,status) {
                oScope.m_oController.m_sAddress = data.phone;
            }).error(function(data,status) {

        });
    }

    SmsController.prototype.getMessage = function() {
        return this.m_oMessage;
    }

    SmsController.prototype.cancel = function() {
        this.m_oLocation.path('/view/' + this.m_iContactId);
    }

    SmsController.prototype.send = function() {
        // Questo fa piantare TUTTO
        //this.m_oMessage.creationDate = new Date();
        this.m_oMessage.forwards =  [];
        this.m_oMessage.idMessage = 0;
        this.m_oMessage.title = "";
        var iContactId = this.m_iContactId;
        var oLocation = this.m_oLocation;


        this.SmsService.sendToContact(this.m_iContactId, this.m_oMessage)
            .success(function (data,status) {
                alert('Sms sent');
                oLocation.path('/view/'+ iContactId);
            }).error(function (data,status) {
                alert('Error Sending Sms (status = ' + status + ')');
            });
    }

    SmsController.$inject = [
        '$scope',
        '$stateParams',
        '$location',
        'ContactsService',
        'SmsService'
    ];

    return SmsController;
}) ();