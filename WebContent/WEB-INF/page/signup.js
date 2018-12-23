(function() {
  "use strict";

  window.addEventListener("load", initialize);

  function initialize() {
    $("signup-btn").addEventListener("click", signup);
  }

  function signup() {
    let email = $("email").value;
    let name = $("username").value;
    let password = $("password").value;

    let url = "./signup"; // needs to have a java servlet called Signup
    let obj = {user_id: email, name: name, password: password};
    let req = JSON.stringify(obj);

    ajax("PUT", url, req, successSignup, showSignupError);
  }

  function successSignup(response) {
    response = JSON.parse(response);
    if (response.result === "SUCCESS") {
      // the returned response should be something like this:
      // { result: "SUCCESS", user_id: ..., name: ...}
      onSessionValid(response);
    }
  }

  function onSessionValid(response) {
    let username = response.name;

    $('welcome-msg').innerText = "Welcome, " + username + " You will be redirected to login page shortly";
    setTimeout(redirect, 5000);
  }

  function redirect() {
    window.location = "login.html";
  }

  function showSignupError() {
    $("signup-error").classList.remove("hidden");
    $("signup-error").innerText = "Email or username already exists";
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
      if (xhr.status === 200) {
        callback(xhr.responseText);
      } else if (xhr.status === 403) {
        onSessionInvalid();
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
