/**
 * Created by p.campanella on 18/01/14.
 */

var LoginController = ( function() {
    function LoginController($scope, $modalInstance, oUserService) {
        this.m_oScope = $scope;
        this.m_oUserService = oUserService;
        this.m_sUserName = "";
        this.m_sPassword = "";
        this.m_oScope.m_oController = this;
        this.m_oModalInstance = $modalInstance;
    }

    LoginController.prototype.login = function() {

        var oServiceVar = this.m_oUserService;
        var oModalInstance = this.m_oModalInstance;
        var oController = this;

        oServiceVar.login(this.m_sUserName, this.m_sPassword).success(function(data,status) {
                oServiceVar.m_bIsLogged = data.BoolValue;
                if (oServiceVar.m_bIsLogged) oModalInstance.close();
                else oController.m_sPassword = "";
            })
            .error(function(data,status){
                alert('Login Error');
                oController.m_sPassword = "";
            });
    }
/**
    LoginController.$inject = [
        '$scope',
        '$location',
        'UserService'
    ];
*/
    return LoginController;
}) ();