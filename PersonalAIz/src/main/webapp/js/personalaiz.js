/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//var host = "http://personalaiz.cloudapp.net:8085/";
var host = "http://localhost:8080/";
//var host = "http://gsoft.gr:8080/";

$(document).ready(function () {

    $("#loginForm").submit(function (event) {

        var url = host + "PersonalAIz/api/security/check/credentials";
        var username = $("#inputUsername").val();
        var password = $("#inputPassword").val();
        event.preventDefault();
        $.post(url, {username: username, password: password})
                .done(function (data) {
                    if (data.output === false) {
                        $("#wrongCredentials").text("Not valid Credentials!")
                                .show().fadeOut(4000);
                    } else {
                        window.sessionStorage.setItem("PersonalAIz.Access", true);
                        window.sessionStorage.setItem("PersonalAIz.Username", username);
                        window.sessionStorage.setItem("PersonalAIz.Password", password);
                        window.location.replace("dashboard.html");
                    }
                });
    });

    $("#addClientForm").submit(function (event) {
        event.preventDefault();
        addClient();
    });
    $("#editSettingForm").submit(function (event) {
        event.preventDefault();
        setSetting();
    });

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var url = e.target;
        var split = url.toString().split("#");
        if (split[1] === "clients") {
            getClients();
        } else if (split[1] === "settings") {
            getSettings();
        }
    });
});


function checkAccess() {
    if (window.sessionStorage.getItem("PersonalAIz.Access") === "true" &&
            (window.location.pathname === "/PersonalAIz/index.html" ||
                    window.location.pathname === "/PersonalAIz/")) {
        window.location.replace("dashboard.html");
    } else if ((window.sessionStorage.getItem("PersonalAIz.Access") === "false" ||
            window.sessionStorage.getItem("PersonalAIz.Access") === null) &&
            window.location.pathname === "/PersonalAIz/dashboard.html") {
        window.location.replace("index.html");
    }
}

function dashboardLoad() {
    checkAccess();
    getClients();
}

function logout() {
    window.sessionStorage.setItem("PersonalAIz.Access", false);
    window.sessionStorage.setItem("PersonalAIz.Username", "");
    window.sessionStorage.setItem("PersonalAIz.Password", "");
    window.location.replace("index.html");
}

function addClient() {

    var ClientName = $("#inputClientName").val();
    var Password = $("#inputClientPassword").val();
    var RePassword = $("#inputClientRPassword").val();
    var userAuthe = window.sessionStorage.getItem("PersonalAIz.Username")
            + "%7C" + window.sessionStorage.getItem("PersonalAIz.Password");

    if (Password === RePassword &&
            (ClientName !== "" &&
                    Password !== "")) {

        var url = host + "PersonalAIz/api/pserver/" + userAuthe +
                "/admin/client/" + ClientName;
        $.post(url, {password: Password})
                .done(function (data) {
                    getClients();
                    document.getElementById("addClientForm").reset();
                    $("#ClientsFormOutput").html('<h4 class="text-success">' + data.outputMessage + '</h4>')
                            .show().fadeOut(4000);
                });

    } else {
        $("#ClientsFormOutput").html('<h4 class="text-danger">Not valid Password!</h4>')
                .show().fadeOut(4000);
    }
}

function getClients() {
    var userAuthe = window.sessionStorage.getItem("PersonalAIz.Username")
            + "%7C" + window.sessionStorage.getItem("PersonalAIz.Password");

    var url = host + "PersonalAIz/api/pserver/" + userAuthe + "/admin/clients";
    $.get(url).done(function (data) {
        $("#clientsTable").html("");
        $.each(data.output, function (key, clientName) {
            var index = parseInt(key) + 1;
            var client = '<tr>' +
                    '<td>' + index + '</td>' +
                    '<td>' + clientName + '</td>' +
                    '<td><a href="#" onclick="deleteClient(\'' + clientName + '\')">' +
                    '<span class="glyphicon glyphicon-remove"></span></a>' +
                    '</td></tr>';
            $("#clientsTable").append(client);
        });
    });



}

function deleteClient(clientname) {
    var userAuthe = window.sessionStorage.getItem("PersonalAIz.Username")
            + "%7C" + window.sessionStorage.getItem("PersonalAIz.Password");

    var url = host + "PersonalAIz/api/pserver/" + userAuthe +
            "/admin/client/delete/" + clientname;

    $.post(url).done(function (data) {
        getClients()
    });
}



function getSettings() {
    var userAuthe = window.sessionStorage.getItem("PersonalAIz.Username")
            + "%7C" + window.sessionStorage.getItem("PersonalAIz.Password");

    var url = host + "PersonalAIz/api/pserver/" + userAuthe + "/admin/settings";
    $.get(url).done(function (data) {
        $("#settingsTable").html("");
        var i = 1;
        $.each(data.output, function (key, value) {
            var setting = '<tr>' +
                    '<td>' + i + '</td>' +
                    '<td>' + key + '</td>' +
                    '<td>' + value + '</td>' +
                    '</tr>';
            $("#settingsTable").append(setting);
            i++;
        });
    });
}

function setSetting() {
    var userAuthe = window.sessionStorage.getItem("PersonalAIz.Username")
            + "%7C" + window.sessionStorage.getItem("PersonalAIz.Password");

    var url = host + "PersonalAIz/api/pserver/" + userAuthe + "/admin/setting";

    var settingName = $("#inputSettingName").val();
    var settingValue = $("#inputSettingValue").val();

    if (settingName !== "" && settingValue !== "") {
        $.post(url, {name: settingName, value: settingValue})
                .done(function (data) {
                    getSettings();
                    document.getElementById("editSettingForm").reset();
                    $("#SettingFormOutput").html('<h4 class="text-success">' + data.outputMessage + '</h4>')
                            .show().fadeOut(4000);
                });
    } else {
        $("#SettingFormOutput").html('<h4 class="text-danger">Not valid Settings!</h4>')
                .show().fadeOut(4000);
    }

}