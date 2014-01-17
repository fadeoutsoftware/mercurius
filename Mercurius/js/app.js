'use strict';

var mercuriusApp = angular.module('mercurius', ['ngRoute', 'mercurius.filters', 'mercurius.directives','mercurius.contactsServiceModule','mercurius.smsServiceModule','mercurius.mailServiceModule']);

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

// TODO: don't know how to use constants!!
//mercuriusApp.constant('APIURL','http://130.251.104.84:8080/it.fadeout.mercurius.webapi/rest');

mercuriusApp.controller("ContactsAddController", ContactsAddController);
mercuriusApp.controller("ContactsEditController", ContactsEditController);
mercuriusApp.controller("ContactsListController", ContactsListController);
mercuriusApp.controller("ContactsSearchController", ContactsSearchController);
mercuriusApp.controller("ContactsViewController", ContactsViewController);
mercuriusApp.controller("SmsController", SmsController);
mercuriusApp.controller("MailController", MailController);

