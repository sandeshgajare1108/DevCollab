
document.addEventListener("DOMContentLoaded", function () {

    console.log("Dashboard JS Loaded");

    loadUserData();

    loadDashboard();

});


/*
 * ========================================================= LOAD USER DATA
 * =========================================================
 */

function loadUserData() {

    console.log("Loading user data...");

    var fullName = localStorage.getItem("fullName");
    var role = localStorage.getItem("role");

    console.log("User Name:", fullName);
    console.log("User Role:", role);


    if (fullName) {

        var userName =
            document.getElementById("userName");

        var profileName =
            document.getElementById("profileName");


        if (userName) {
            userName.innerText = fullName;
        }


        if (profileName) {
            profileName.innerText = fullName;
        }


        var firstLetter =
            fullName.charAt(0).toUpperCase();


        var userAvatar =
            document.getElementById("userAvatar");

        var profileAvatar =
            document.getElementById("profileAvatar");


        if (userAvatar) {
            userAvatar.innerText = firstLetter;
        }


        if (profileAvatar) {
            profileAvatar.innerText = firstLetter;
        }

    }


    if (role) {

        var userRole =
            document.getElementById("userRole");


        if (userRole) {
            userRole.innerText = role;
        }

    }

}


/*
 * ========================================================= LOAD DASHBOARD
 * =========================================================
 */

function loadDashboard() {

    console.log("Calling Dashboard API...");


    var token =
        localStorage.getItem("token");


    console.log(
        "Token available:",
        token ? "YES" : "NO"
    );


    if (!token) {

        showError(
            "JWT token not found. Please login again."
        );

        return;

    }


    fetch(
        contextPath + "/api/dashboard",
        {
            method: "GET",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"

            }

        }
    )


    .then(function (response) {

        console.log(
            "Dashboard HTTP Status:",
            response.status
        );


        if (!response.ok) {

            if (response.status === 401) {

                throw new Error(
                    "401 Unauthorized - Please login again."
                );

            }


            if (response.status === 403) {

                throw new Error(
                    "403 Forbidden - You don't have permission."
                );

            }


            throw new Error(
                "Dashboard API Error: " +
                response.status
            );

        }


        return response.json();

    })


    .then(function (data) {

        console.log(
            "Dashboard API Response:",
            data
        );


        /*
		 * ===================================================== PROJECT
		 * STATISTICS =====================================================
		 */

        setValue(
            "totalProjects",
            data.totalProjects
        );


        setValue(
            "inProgressProjects",
            data.inProgressProjects
        );


        setValue(
            "completedProjects",
            data.completedProjects
        );


        /*
		 * ===================================================== TASK STATISTICS
		 * =====================================================
		 */

        setValue(
            "totalTasks",
            data.totalTasks
        );


        setValue(
            "todoTasks",
            data.todoTasks
        );


        setValue(
            "inProgressTasks",
            data.inProgressTasks
        );


        setValue(
            "reviewTasks",
            data.reviewTasks
        );


        setValue(
            "completedTasks",
            data.completedTasks
        );


        /*
		 * ===================================================== PROJECT
		 * OVERVIEW =====================================================
		 */

        setValue(
            "planningProjects",
            data.planningProjects
        );


        setValue(
            "projectInProgress",
            data.inProgressProjects
        );


        setValue(
            "projectCompleted",
            data.completedProjects
        );


        /*
		 * ===================================================== TEAM / PROJECT
		 * MEMBER STATISTICS
		 * =====================================================
		 */

        setValue(
            "totalMembers",
            data.totalMembers
        );


        setValue(
            "ownerMembers",
            data.ownerMembers
        );


        setValue(
            "developerMembers",
            data.developerMembers
        );


        setValue(
            "testerMembers",
            data.testerMembers
        );


        setValue(
            "designerMembers",
            data.designerMembers
        );


        setValue(
            "managerMembers",
            data.managerMembers
        );


        /*
		 * ===================================================== PROJECT
		 * PROGRESS =====================================================
		 */

        updateProjectProgress(data);


        /*
		 * ===================================================== HIDE LOADING
		 * =====================================================
		 */

        hideLoading();


        console.log(
            "Dashboard loaded successfully."
        );

    })


    .catch(function (error) {

        console.error(
            "Dashboard Error:",
            error
        );


        hideLoading();


        showError(
            error.message
        );

    });

}


/*
 * ========================================================= SET VALUE
 * =========================================================
 */

function setValue(elementId, value) {

    var element =
        document.getElementById(elementId);


    if (!element) {

        console.warn(
            "Element not found:",
            elementId
        );

        return;

    }


    if (
        value === null ||
        value === undefined
    ) {

        element.innerText = "0";

    }
    else {

        element.innerText = value;

    }

}


/*
 * ========================================================= UPDATE PROJECT
 * PROGRESS =========================================================
 */

function updateProjectProgress(data) {

    var total =
        Number(data.totalProjects) || 0;


    var planning =
        Number(data.planningProjects) || 0;


    var inProgress =
        Number(data.inProgressProjects) || 0;


    var completed =
        Number(data.completedProjects) || 0;


    var planningPercentage = 0;

    var progressPercentage = 0;

    var completedPercentage = 0;


    if (total > 0) {

        planningPercentage =
            (planning / total) * 100;


        progressPercentage =
            (inProgress / total) * 100;


        completedPercentage =
            (completed / total) * 100;

    }


    setProgress(
        "planningProgress",
        planningPercentage
    );


    setProgress(
        "projectProgress",
        progressPercentage
    );


    setProgress(
        "completedProgress",
        completedPercentage
    );

}


/*
 * ========================================================= SET PROGRESS
 * =========================================================
 */

function setProgress(elementId, percentage) {

    var element =
        document.getElementById(elementId);


    if (!element) {

        console.warn(
            "Progress element not found:",
            elementId
        );

        return;

    }


    element.style.width =
        percentage + "%";

}


/*
 * ========================================================= HIDE LOADING
 * =========================================================
 */

function hideLoading() {

    var loading =
        document.getElementById(
            "loadingMessage"
        );


    if (loading) {

        loading.style.display =
            "none";

    }

}


/*
 * ========================================================= SHOW ERROR
 * =========================================================
 */

function showError(message) {

    var error =
        document.getElementById(
            "errorMessage"
        );


    if (!error) {

        console.error(message);

        return;

    }


    error.innerText = message;

    error.style.display = "block";

}


/*
 * ========================================================= LOGOUT
 * =========================================================
 */

function logout() {

    console.log("Logging out...");


    localStorage.removeItem("token");

    localStorage.removeItem("userId");

    localStorage.removeItem("fullName");

    localStorage.removeItem("email");

    localStorage.removeItem("role");


    window.location.href =
        contextPath + "/login.jsp";

}


/*
 * ========================================================= CREATE PROJECT
 * =========================================================
 */

function createProject() {

    window.location.href =
        contextPath + "/project.jsp";

}


/*
 * ========================================================= CREATE TASK
 * =========================================================
 */

function createTask() {

    window.location.href =
        contextPath + "/tasks.jsp";

}


/*
 * ========================================================= VIEW TEAM
 * =========================================================
 */

function viewTeam() {

    window.location.href =
        contextPath + "/team.jsp";

}


/*
 * ========================================================= VIEW PROJECTS
 * =========================================================
 */

function viewProjects() {

    window.location.href =
        contextPath + "/project.jsp";

}
function loadProjectProgress(projectId) {

    var token =
        localStorage.getItem("token");

    if (!token) {
        return;
    }

    fetch(
        "/api/dashboard/project/" +
        projectId +
        "/progress",
        {
            method: "GET",

            headers: {
                "Authorization":
                    "Bearer " + token
            }
        }
    )
    .then(async response => {

        console.log(
            "Project Progress Status:",
            response.status
        );

        var text =
            await response.text();

        if (!response.ok) {
            throw new Error(text);
        }

        return JSON.parse(text);
    })
    .then(data => {

        console.log(
            "Project Progress:",
            data
        );

        renderProjectProgress(
            data
        );
    })
    .catch(error => {

        console.error(
            "Project Progress Error:",
            error
        );
    });
    function renderProjectProgress(
    	    data
    	) {

    	    var percent =
    	        Number(
    	            data.progress || 0
    	        );

    	    var progressPercent =
    	        document.getElementById(
    	            "projectProgressPercent"
    	        );

    	    var progressBar =
    	        document.getElementById(
    	            "projectProgressBar"
    	        );

    	    var completedTasks =
    	        document.getElementById(
    	            "completedTasks"
    	        );

    	    var totalTasks =
    	        document.getElementById(
    	            "totalTasks"
    	        );

    	    var openBugs =
    	        document.getElementById(
    	            "openBugs"
    	        );

    	    var approvedPullRequests =
    	        document.getElementById(
    	            "approvedPullRequests"
    	        );


    	    if (progressPercent) {

    	        progressPercent.innerText =
    	            percent + "%";
    	    }


    	    if (progressBar) {

    	        progressBar.style.width =
    	            percent + "%";
    	    }


    	    if (completedTasks) {

    	        completedTasks.innerText =
    	            data.completedTasks || 0;
    	    }


    	    if (totalTasks) {

    	        totalTasks.innerText =
    	            data.totalTasks || 0;
    	    }


    	    if (openBugs) {

    	        openBugs.innerText =
    	            data.openBugs || 0;
    	    }


    	    if (approvedPullRequests) {

    	        approvedPullRequests.innerText =
    	            data.approvedPullRequests || 0;
    	    }
    	}
}
