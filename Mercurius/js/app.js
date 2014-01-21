'use strict';

//http://blog.brunoscopelliti.com/deal-with-users-authentication-in-an-angularjs-web-app
var mercuriusApp = angular.module('mercurius', ['ui.bootstrap','ui.router', 'ngRoute', 'mercurius.filters', 'mercurius.directives','mercurius.userService','mercurius.contactsServiceModule','mercurius.smsServiceModule','mercurius.mailServiceModule']);

/**
mercuriusApp.config(function($routeProvider) {

        // For any unmatched url, redirect to /default
        $routeProvider.when('/', {templateUrl: "partials/add.html", controller: "ContactsAddController"});
        $routeProvider.when('/view/:id', {templateUrl: "partials/view.html", controller: "ContactsViewController"});
        $routeProvider.when('/edit/:id', {templateUrl: "partials/edit.html", controller: "ContactsEditController"});
        $routeProvider.when('/sms/:id', {templateUrl: "partials/sms.html", controller: "SmsController"});
        $routeProvider.when('/mail/:id', {templateUrl: "partials/mail.html", controller: "MailController"});
        $routeProvider.otherwise({redirectTo: '/'});
    }
);
*/

mercuriusApp.config(function($stateProvider, $urlRouterProvider) {

        // For any unmatched url, redirect to /default
        $stateProvider.state('contacts', {url: '/', templateUrl: "partials/add.html", controller: "ContactsAddController"});
        $stateProvider.state('contactsview', {url: '/view/:id', templateUrl: "partials/view.html", controller: "ContactsViewController"});
        $stateProvider.state('contactsedit', {url: '/edit/:id', templateUrl: "partials/edit.html", controller: "ContactsEditController"});
        $stateProvider.state('contactssms', {url: '/sms/:id', templateUrl: "partials/sms.html", controller: "SmsController"});
        $stateProvider.state('contactsmail', {url: '/mail/:id', templateUrl: "partials/mail.html", controller: "MailController"});
        $urlRouterProvider.otherwise('/');
    }
);

// TODO: don't know how to use constants!!
//mercuriusApp.constant('APIURL','http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest');

mercuriusApp.controller("ContactsAddController", ContactsAddController);
mercuriusApp.controller("LoginController", LoginController);
mercuriusApp.controller("ContactsEditController", ContactsEditController);
mercuriusApp.controller("ContactsListController", ContactsListController);
mercuriusApp.controller("ContactsSearchController", ContactsSearchController);
mercuriusApp.controller("ContactsViewController", ContactsViewController);
mercuriusApp.controller("SmsController", SmsController);
mercuriusApp.controller("MailController", MailController);

