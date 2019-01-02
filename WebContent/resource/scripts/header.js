(function() {
	"use strict";
	
	var user_id = null;
	
	window.addEventListener("load", initialize);
	
	function initialize() {
	   var myplan = $("header-myplan");
	   var search = $("header-search");
	   var login = $("login/logout-link");
	   	   	  
	   if (window.localStorage.getItem("status") === "loggedIn") {
		      user_id = window.localStorage.getItem("user_id");
		      login.innerHTML = "Logout";
		      login.addEventListener("click", logOut);
	   } else {
		   	  login.innerHTML = "Login";
		   	  login.addEventListener("click", logIn);
	   }
	   
	   myplan.addEventListener("click", myPlanPage);
	   search.addEventListener("click", searchPage);	
	   
	   
	}
	
	function $(id) {
		return document.getElementById(id);
	}

	function myPlanPage() {
		if (user_id === null) {
			window.alert("Please login first to access your WistList.");
			window.location = "page/login.html"; // check if user has logged in or not
		} else {
			window.location = "page/favorPlaces.html";
		}
	}	
		  
	function searchPage() {
		window.location = "page/searchPage.html"; ////////////// bug
	}

	function logOut(){
		window.localStorage.removeItem("status");
		window.localStorage.removeItem("user_id");
		window.location = "searchPage.html";  ////////////////// bug
	}
	
	function logIn() {
		window.location = "page/login.html"; ////////////// bug
	}
})();