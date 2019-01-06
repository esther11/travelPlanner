(function() {
  "use strict";

  window.addEventListener("load", initialize);

  function initialize() {
    $("login-btn").addEventListener("click", login);
  }

  function login() {
    let email = $("email").value;
    let password = $("password").value;
    
    let url = "../login";
    let obj = {user_id: email, password: password};
    let req = JSON.stringify(obj);

    ajax("POST", url, req, successLogin, showLoginError);
  }

  function successLogin(response) {
    var response = JSON.parse(response);
    if (response.result === "SUCCESS") {
      window.localStorage.setItem("status", "loggedIn");
      window.localStorage.setItem("user_id", response["user_id"]);
      window.localStorage.setItem("username", response["user name"]);
      
      $("login-error").classList.add("hidden");
      $('welcome-msg').innerText = "Welcome, " + response["user name"] + "!";
      $("welcome-msg").classList.remove("hidden");
        setTimeout(redirect, 2000);
    }
  }

  function redirect() {
    window.location = "searchPage.html";
  }

  function showLoginError() {
    $("login-error").innerText = "Invalid email or password";
    $("login-error").classList.remove("hidden");
  }

  /**
  * return the dom node of the given id
  * @param {string} id - id of the element
  * @return {node} - dom node of the given id
  */
  function $(id) {
    return document.getElementById(id);
  }

  function ajax(method, url, data, callback, errorHandler) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    
   
    xhr.onload = function() {
      if (xhr.readyState === 4 && xhr.status === 200) {
        callback(xhr.responseText);
    } else {
      errorHandler();
    }
    }

    xhr.onerror = function() {
      console.error("The request couldn't be completed.");
    errorHandler();
    }
  
    if (data === null) {
      xhr.send();
  } else {
    xhr.setRequestHeader("Content-Type",
                   "application/json;charset=utf-8");
    xhr.send(data);
  }
  }

})();
