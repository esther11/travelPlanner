(function() {
  "use strict";

  window.addEventListener("load", initialize);

  function initialize() {
    $("login-btn").addEventListener("click", login);
  }

  function login() {
    let email = $("email").value;
    let password = $("password").value;
    password = md5(username + md5(password));

    let url = "./login";
    let obj = {user_id: email, password: password};
    let req = JSON.stringify(obj);

    ajax("POST", url, req, successLogin, showLoginError);
  }

  function successLogin(response) {
    var parsedResponse = JSON.parse(response);
    if (parsedResponse.result === "SUCCESS") {
      // the returned response should be something like this:
      // { result: "SUCCESS", user_id: ..., name: ...}
      // what the page should be like when user successfully login in?
    }
  }

  function showLoginError() {
    $("login-error").innerText = "Invalid email or password";
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
