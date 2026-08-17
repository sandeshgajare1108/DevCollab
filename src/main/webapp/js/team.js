
// =====================================================
// DEV COLLAB - TEAM
// =====================================================

var allMembers = [];

var editingMemberId = null;


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("Team JS Loaded");

    loadUserData();

    loadTeamMembers();


    // FORM SUBMIT

    var form =
        document.getElementById("memberForm");


    if (form) {

        form.addEventListener(
            "submit",
            function (event) {

                event.preventDefault();

                saveMember();

            }
        );

    }


    // SEARCH

    var search =
        document.getElementById("searchInput");


    if (search) {

        search.addEventListener(
            "input",
            filterMembers
        );

    }


    // PROJECT FILTER

    var project =
        document.getElementById("projectFilter");


    if (project) {

        project.addEventListener(
            "input",
            filterMembers
        );

    }


    // ROLE FILTER

    var role =
        document.getElementById("roleFilter");


    if (role) {

        role.addEventListener(
            "change",
            filterMembers
        );

    }

});


// =====================================================
// USER DATA
// =====================================================

function loadUserData() {

    var fullName =
        localStorage.getItem("fullName");

    var role =
        localStorage.getItem("role");


    var userName =
        document.getElementById("userName");

    var userRole =
        document.getElementById("userRole");

    var avatar =
        document.getElementById("userAvatar");


    if (fullName && userName) {

        userName.innerText =
            fullName;

    }


    if (fullName && avatar) {

        avatar.innerText =
            fullName
                .charAt(0)
                .toUpperCase();

    }


    if (role && userRole) {

        userRole.innerText =
            role;

    }

}


// =====================================================
// LOAD TEAM
// =====================================================

function loadTeamMembers() {

    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;

    }


    var loading =
        document.getElementById("loading");


    if (loading) {

        loading.style.display =
            "block";

    }


    fetch(
        contextPath +
        "/api/team",
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
            "Team API Status:",
            response.status
        );


        if (response.status === 401) {

            throw new Error(
                "Unauthorized. Please login again."
            );

        }


        if (response.status === 403) {

            throw new Error(
                "You don't have permission to view team."
            );

        }


        if (!response.ok) {

            throw new Error(
                "Failed to load team. HTTP " +
                response.status
            );

        }


        return response.json();

    })


    .then(function (members) {

        allMembers =
            members || [];


        if (loading) {

            loading.style.display =
                "none";

        }


        displayMembers(allMembers);

    })


    .catch(function (error) {

        console.error(
            "Team Error:",
            error
        );


        if (loading) {

            loading.style.display =
                "none";

        }


        showMessage(
            error.message,
            "error"
        );

    });

}


// =====================================================
// DISPLAY MEMBERS
// =====================================================

function displayMembers(members) {

    var container =
        document.getElementById(
            "teamContainer"
        );


    var empty =
        document.getElementById(
            "emptyMessage"
        );


    if (!container) {

        return;

    }


    container.innerHTML = "";


    if (!members ||
        members.length === 0) {

        if (empty) {

            empty.style.display =
                "block";

        }

        return;

    }


    if (empty) {

        empty.style.display =
            "none";

    }


    members.forEach(function (member) {

        var card =
            document.createElement("div");


        card.className =
            "team-card";


        var role =
            member.role ||
            "MEMBER";


        card.innerHTML =

            '<div class="member-avatar">' +

                'U' +

            '</div>' +


            '<div class="member-info">' +

                '<h3>' +

                    'User #' +

                    escapeHtml(
                        member.userId
                    ) +

                '</h3>' +


                '<p>' +

                    'Project #' +

                    escapeHtml(
                        member.projectId
                    ) +

                '</p>' +


                '<span class="role-badge ' +

                    getRoleClass(role) +

                '">' +

                    escapeHtml(role) +

                '</span>' +

            '</div>' +


            '<div class="member-meta">' +

                '<span>' +

                    '🆔 Member ID: ' +

                    escapeHtml(
                        member.teamMemberId
                    ) +

                '</span>' +


                '<span>' +

                    '📅 Joined: ' +

                    formatDate(
                        member.joinedAt
                    ) +

                '</span>' +

            '</div>' +


            '<div class="member-actions">' +

                '<button ' +

                    'class="edit-action" ' +

                    'onclick="editMember(' +

                        member.teamMemberId +

                    ')">' +

                    'Edit' +

                '</button>' +


                '<button ' +

                    'class="delete-action" ' +

                    'onclick="deleteMember(' +

                        member.teamMemberId +

                    ')">' +

                    'Remove' +

                '</button>' +

            '</div>';


        container.appendChild(card);

    });

}


// =====================================================
// FILTER
// =====================================================

function filterMembers() {

    var searchElement =
        document.getElementById(
            "searchInput"
        );


    var projectElement =
        document.getElementById(
            "projectFilter"
        );


    var roleElement =
        document.getElementById(
            "roleFilter"
        );


    var search =
        searchElement ?
        searchElement.value
            .toLowerCase()
            .trim() :
        "";


    var project =
        projectElement ?
        projectElement.value.trim() :
        "";


    var role =
        roleElement ?
        roleElement.value :
        "ALL";


    var filtered =
        allMembers.filter(
            function (member) {


                var userId =
                    String(
                        member.userId || ""
                    );


                var projectId =
                    String(
                        member.projectId || ""
                    );


                var memberRole =
                    String(
                        member.role || ""
                    );


                var matchesSearch =
                    userId
                        .toLowerCase()
                        .includes(search);


                var matchesProject =
                    !project ||
                    projectId === project;


                var matchesRole =
                    role === "ALL" ||
                    memberRole === role;


                return (
                    matchesSearch &&
                    matchesProject &&
                    matchesRole
                );

            }
        );


    displayMembers(filtered);

}


// =====================================================
// CLEAR FILTERS
// =====================================================

function clearFilters() {

    document.getElementById(
        "searchInput"
    ).value = "";


    document.getElementById(
        "projectFilter"
    ).value = "";


    document.getElementById(
        "roleFilter"
    ).value = "ALL";


    displayMembers(allMembers);

}


// =====================================================
// OPEN ADD MODAL
// =====================================================

function openAddMemberModal() {

    editingMemberId = null;


    document.getElementById(
        "modalTitle"
    ).innerText =
        "Add Team Member";


    document.getElementById(
        "saveMemberButton"
    ).innerText =
        "Add Member";


    document.getElementById(
        "memberForm"
    ).reset();


    document.getElementById(
        "teamMemberId"
    ).value = "";


    document.getElementById(
        "memberRole"
    ).value =
        "MEMBER";


    document.getElementById(
        "memberModal"
    ).style.display =
        "flex";

}


// =====================================================
// CLOSE MODAL
// =====================================================

function closeMemberModal() {

    document.getElementById(
        "memberModal"
    ).style.display =
        "none";

}


// =====================================================
// EDIT MEMBER
// =====================================================

function editMember(memberId) {

    var member =
        allMembers.find(
            function (item) {

                return Number(
                    item.teamMemberId
                ) === Number(memberId);

            }
        );


    if (!member) {

        showMessage(
            "Team member not found.",
            "error"
        );

        return;

    }


    editingMemberId =
        memberId;


    document.getElementById(
        "modalTitle"
    ).innerText =
        "Edit Team Member";


    document.getElementById(
        "saveMemberButton"
    ).innerText =
        "Update Member";


    document.getElementById(
        "teamMemberId"
    ).value =
        member.teamMemberId;


    document.getElementById(
        "projectId"
    ).value =
        member.projectId;


    document.getElementById(
        "userId"
    ).value =
        member.userId;


    document.getElementById(
        "memberRole"
    ).value =
        member.role || "MEMBER";


    document.getElementById(
        "memberModal"
    ).style.display =
        "flex";

}


// =====================================================
// SAVE MEMBER
// =====================================================

function saveMember() {

    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;

    }


    var projectId =
        Number(
            document.getElementById(
                "projectId"
            ).value
        );


    var userId =
        Number(
            document.getElementById(
                "userId"
            ).value
        );


    var role =
        document.getElementById(
            "memberRole"
        ).value;


    if (!projectId ||
        projectId <= 0) {

        showMessage(
            "Valid Project ID is required.",
            "error"
        );

        return;

    }


    if (!userId ||
        userId <= 0) {

        showMessage(
            "Valid User ID is required.",
            "error"
        );

        return;

    }


    var member = {

        projectId:
            projectId,

        userId:
            userId,

        role:
            role

    };


    var url =
        contextPath +
        "/api/team";


    var method =
        "POST";


    if (editingMemberId) {

        url +=
            "/" +
            editingMemberId;

        method =
            "PUT";

    }


    var button =
        document.getElementById(
            "saveMemberButton"
        );


    if (button) {

        button.disabled =
            true;

        button.innerText =
            editingMemberId ?
            "Updating..." :
            "Adding...";

    }


    fetch(
        url,
        {

            method:
                method,

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"

            },

            body:
                JSON.stringify(member)

        }
    )


    .then(function (response) {

        return response.text()
            .then(function (text) {

                if (!response.ok) {

                    throw new Error(
                        text ||
                        "Failed to save team member."
                    );

                }


                return text;

            });

    })


    .then(function () {

        showMessage(
            editingMemberId ?
            "Team member updated successfully!" :
            "Team member added successfully!",
            "success"
        );


        closeMemberModal();


        loadTeamMembers();

    })


    .catch(function (error) {

        console.error(
            "Save Member Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    })


    .finally(function () {

        if (button) {

            button.disabled =
                false;

            button.innerText =
                editingMemberId ?
                "Update Member" :
                "Add Member";

        }

    });

}


// =====================================================
// DELETE MEMBER
// =====================================================

function deleteMember(memberId) {

    if (!memberId) {

        showMessage(
            "Team member ID not found.",
            "error"
        );

        return;

    }


    var confirmed =
        confirm(
            "Are you sure you want to remove this team member?"
        );


    if (!confirmed) {

        return;

    }


    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;

    }


    fetch(
        contextPath +
        "/api/team/" +
        memberId,
        {

            method: "DELETE",

            headers: {

                "Authorization":
                    "Bearer " + token

            }

        }
    )


    .then(function (response) {

        return response.text()
            .then(function (text) {

                if (!response.ok) {

                    throw new Error(
                        text ||
                        "Failed to remove member."
                    );

                }


                return text;

            });

    })


    .then(function () {

        showMessage(
            "Team member removed successfully!",
            "success"
        );


        loadTeamMembers();

    })


    .catch(function (error) {

        console.error(
            "Delete Member Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    });

}


// =====================================================
// ROLE CLASS
// =====================================================

function getRoleClass(role) {

    switch (role) {

        case "ADMIN":
            return "admin";

        case "MANAGER":
            return "manager";

        case "DEVELOPER":
            return "developer";

        case "TESTER":
            return "tester";

        default:
            return "member";

    }

}


// =====================================================
// DATE FORMAT
// =====================================================

function formatDate(value) {

    if (!value) {

        return "-";

    }


    try {

        var date =
            new Date(value);


        if (isNaN(
            date.getTime()
        )) {

            return "-";

        }


        return date.toLocaleDateString();

    }

    catch (error) {

        return "-";

    }

}


// =====================================================
// ESCAPE HTML
// =====================================================

function escapeHtml(value) {

    return String(value)
        .replace(
            /&/g,
            "&amp;"
        )
        .replace(
            /</g,
            "&lt;"
        )
        .replace(
            />/g,
            "&gt;"
        )
        .replace(
            /"/g,
            "&quot;"
        )
        .replace(
            /'/g,
            "&#039;"
        );

}


// =====================================================
// MESSAGE
// =====================================================

function showMessage(message, type) {

    var element =
        document.getElementById(
            "message"
        );


    if (!element) {

        alert(message);

        return;

    }


    element.innerText =
        message;


    element.className =
        "message " + type;


    element.style.display =
        "block";


    setTimeout(function () {

        element.style.display =
            "none";

    }, 4000);

}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("userId");

    localStorage.removeItem("fullName");

    localStorage.removeItem("email");

    localStorage.removeItem("role");


    window.location.href =
        contextPath +
        "/login.jsp";

}
