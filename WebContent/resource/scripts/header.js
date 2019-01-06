(function() {
	"use strict";

	var user_id = null;
	var username = null;

	window.addEventListener("load", initialize);

	function initialize() {
		var myplan = $("header-myplan");
		var search = $("header-search");
		var login = $("login-logout-link");
		var welcome = $("welcome-msg");
		var signup = $("signup-link");

		if (window.localStorage.getItem("status") === "loggedIn") {
			user_id = window.localStorage.getItem("user_id");
			username = window.localStorage.getItem("username");
			login.innerHTML = "Logout";
			login.addEventListener("click", logOut);
			welcome.innerHTML = "Welcome, " + username;
			hideElement(signup);
		} else {
			login.innerHTML = "Login";
			login.addEventListener("click", logIn);
			welcome.innerHTML = "";
		}

		myplan.addEventListener("click", myPlanPage);
		search.addEventListener("click", searchPage);
		signup.addEventListener("click", signUpPage);
	}
	
	function hideElement(element) {
        element.style.display = 'none';
    }

	function $(id) {
		return document.getElementById(id);
	}

	function myPlanPage() {
		if (user_id === null) {
			window.alert("Please login first to access your WistList.");
			window.location = "login.html"; // check if user has logged in or
											// not
		} else {
			window.location = "favorPlaces.html";
		}
	}

	function searchPage() {
		window.location = "searchPage.html";
	}

	function signUpPage() {
		window.location = "signup.html";
	}
	
	function logOut() {
		window.localStorage.removeItem("status");
		window.localStorage.removeItem("user_id");
		window.localStorage.removeItem("username");
		window.localStorage.removeItem("city");
		window.localStorage.removeItem("placeName");
		window.location = "searchPage.html";
	}

	function logIn() {
		window.location = "login.html";
	}	
})();