
console.log("=================================");
console.log("DevCollab Team Chat JS Loaded");
console.log("=================================");


let stompClient = null;

let currentProjectId = null;

let currentUserId =
    Number(
        localStorage.getItem("userId")
    );

let currentUserName =
    localStorage.getItem("fullName") ||
    "User";

let currentRole =
    localStorage.getItem("role") ||
    "USER";


// =====================================================
// DOM READY
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadUserData();

        const form =
            document.getElementById(
                "chatForm"
            );


        if (form) {

            form.addEventListener(
                "submit",
                sendMessage
            );
        }


        const savedProject =
            sessionStorage.getItem(
                "chatProjectId"
            );


        if (savedProject) {

            const projectInput =
                document.getElementById(
                    "projectId"
                );

            if (projectInput) {

                projectInput.value =
                    savedProject;
            }
        }


        loadChat();
    }
);


// =====================================================
// USER DATA
// =====================================================

function loadUserData() {

    const nameElement =
        document.getElementById(
            "userName"
        );


    const roleElement =
        document.getElementById(
            "userRole"
        );


    const avatarElement =
        document.getElementById(
            "userAvatar"
        );


    if (nameElement) {

        nameElement.innerText =
            currentUserName;
    }


    if (roleElement) {

        roleElement.innerText =
            currentRole;
    }


    if (avatarElement) {

        avatarElement.innerText =
            currentUserName
                .charAt(0)
                .toUpperCase();
    }
}


// =====================================================
// LOAD CHAT
// =====================================================

function loadChat() {

    const projectInput =
        document.getElementById(
            "projectId"
        );


    if (!projectInput) {
        return;
    }


    const projectId =
        Number(
            projectInput.value
        );


    if (!projectId ||
        projectId <= 0) {

        showMessage(
            "Enter a valid project ID.",
            "error"
        );

        return;
    }


    currentProjectId =
        projectId;


    sessionStorage.setItem(
        "chatProjectId",
        projectId
    );


    const label =
        document.getElementById(
            "chatProjectLabel"
        );


    if (label) {

        label.innerText =
            "Project #" + projectId;
    }


    loadChatHistory();

    connectWebSocket();
}


// =====================================================
// LOAD CHAT HISTORY
// =====================================================

async function loadChatHistory() {

    const token =
        localStorage.getItem(
            "token"
        );


    if (!token) {

        showMessage(
            "Authentication required.",
            "error"
        );

        return;
    }


    const container =
        document.getElementById(
            "chatMessages"
        );


    if (!container) {
        return;
    }


    container.innerHTML =
        '<div class="empty-chat">' +
        'Loading messages...' +
        '</div>';


    try {

        const response =
            await fetch(
                contextPath +
                "/api/chat/project/" +
                currentProjectId,
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " +
                            token
                    }
                }
            );


        const text =
            await response.text();


        if (!response.ok) {

            throw new Error(
                text ||
                "Failed to load chat history"
            );
        }


        const messages =
            text
                ? JSON.parse(text)
                : [];


        renderMessages(
            messages
        );


    } catch (error) {

        console.error(
            "Chat History Error:",
            error
        );


        container.innerHTML =
            '<div class="empty-chat">' +
            'Unable to load chat history' +
            '</div>';
    }
}


// =====================================================
// CONNECT WEBSOCKET
// =====================================================

function connectWebSocket() {

    disconnectWebSocket();


    if (!currentProjectId) {
        return;
    }


    const token =
        localStorage.getItem(
            "token"
        );


    if (!token) {

        showMessage(
            "Authentication required.",
            "error"
        );

        return;
    }


    try {

        const socket =
            new SockJS(
                window.location.origin +
                contextPath +
                "/ws"
            );


        stompClient =
            Stomp.over(
                socket
            );


        stompClient.debug =
            null;


        // =================================================
        // CONNECT WITH JWT
        // =================================================

        stompClient.connect(

            {
                Authorization:
                    "Bearer " + token
            },


            function () {

                console.log(
                    "WebSocket connected"
                );


                setConnectionStatus(
                    true
                );


                // =============================================
                // SUBSCRIBE
                // =============================================

                stompClient.subscribe(
                    "/topic/chat",
                    function (message) {

                        try {

                            const data =
                                JSON.parse(
                                    message.body
                                );


                            if (
                                Number(
                                    data.projectId
                                ) !==
                                Number(
                                    currentProjectId
                                )
                            ) {

                                return;
                            }


                            addMessage(
                                data
                            );


                        } catch (error) {

                            console.error(
                                "WebSocket message error:",
                                error
                            );
                        }
                    }
                );
            },


            function (error) {

                console.error(
                    "WebSocket connection error:",
                    error
                );


                setConnectionStatus(
                    false
                );


                showMessage(
                    "Unable to connect to chat server.",
                    "error"
                );
            }
        );


    } catch (error) {

        console.error(
            "WebSocket setup error:",
            error
        );


        setConnectionStatus(
            false
        );
    }
}


// =====================================================
// DISCONNECT
// =====================================================

function disconnectWebSocket() {

    if (stompClient) {

        try {

            stompClient.disconnect(
                function () {

                    console.log(
                        "WebSocket disconnected"
                    );
                }
            );

        } catch (error) {

            console.error(
                "WebSocket disconnect error:",
                error
            );
        }

        stompClient =
            null;
    }


    setConnectionStatus(
        false
    );
}


// =====================================================
// SEND MESSAGE
// =====================================================

function sendMessage(
    event
) {

    event.preventDefault();


    const input =
        document.getElementById(
            "chatInput"
        );


    if (!input) {
        return;
    }


    const message =
        input.value.trim();


    if (!message) {
        return;
    }


    if (!currentProjectId) {

        showMessage(
            "Please select a project.",
            "error"
        );

        return;
    }


    if (!stompClient ||
        !stompClient.connected) {

        showMessage(
            "Chat is not connected.",
            "error"
        );

        return;
    }


    // =================================================
    // IMPORTANT:
    // senderId is NOT sent from frontend.
    // Backend gets senderId from JWT.
    // =================================================

    const payload = {

        projectId:
            currentProjectId,

        message:
            message
    };


    stompClient.send(
        "/app/chat.send",
        {},
        JSON.stringify(
            payload
        )
    );


    input.value =
        "";

    input.focus();
}


// =====================================================
// RENDER HISTORY
// =====================================================

function renderMessages(
    messages
) {

    const container =
        document.getElementById(
            "chatMessages"
        );


    if (!container) {
        return;
    }


    container.innerHTML =
        "";


    if (
        !messages ||
        messages.length === 0
    ) {

        container.innerHTML =
            '<div class="empty-chat">' +
            'No messages yet. Start the conversation.' +
            '</div>';

        return;
    }


    messages.forEach(
        function (message) {

            addMessage(
                message
            );
        }
    );
}


// =====================================================
// ADD MESSAGE
// =====================================================

function addMessage(
    message
) {

    const container =
        document.getElementById(
            "chatMessages"
        );


    if (!container) {
        return;
    }


    const empty =
        container.querySelector(
            ".empty-chat"
        );


    if (empty) {

        empty.remove();
    }


    const wrapper =
        document.createElement(
            "div"
        );


    wrapper.className =
        "chat-message";


    // =================================================
    // MY MESSAGE
    // =================================================

    if (
        Number(
            message.senderId
        ) ===
        Number(
            currentUserId
        )
    ) {

        wrapper.classList.add(
            "mine"
        );
    }


    const sender =
        document.createElement(
            "div"
        );


    sender.className =
        "chat-sender";


    sender.innerText =
        "User #" +
        message.senderId;


    const text =
        document.createElement(
            "div"
        );


    text.className =
        "chat-text";


    text.innerText =
        message.messageText ||
        message.message ||
        "";


    const time =
        document.createElement(
            "div"
        );


    time.className =
        "chat-time";


    time.innerText =
        formatTime(
            message.sentAt
        );


    wrapper.appendChild(
        sender
    );


    wrapper.appendChild(
        text
    );


    wrapper.appendChild(
        time
    );


    container.appendChild(
        wrapper
    );


    container.scrollTop =
        container.scrollHeight;
}


// =====================================================
// FORMAT TIME
// =====================================================

function formatTime(
    value
) {

    if (!value) {
        return "";
    }


    const date =
        new Date(value);


    if (
        isNaN(
            date.getTime()
        )
    ) {

        return "";
    }


    return date.toLocaleString();
}


// =====================================================
// CONNECTION STATUS
// =====================================================

function setConnectionStatus(
    connected
) {

    const element =
        document.getElementById(
            "connectionStatus"
        );


    if (!element) {
        return;
    }


    if (connected) {

        element.className =
            "connection online";

        element.innerText =
            "● Connected";

    } else {

        element.className =
            "connection offline";

        element.innerText =
            "● Disconnected";
    }
}


// =====================================================
// MESSAGE
// =====================================================

function showMessage(
    message,
    type
) {

    const element =
        document.getElementById(
            "message"
        );


    if (!element) {
        return;
    }


    element.innerText =
        message;


    element.className =
        "message " +
        (
            type ||
            "error"
        );


    element.style.display =
        "block";


    setTimeout(
        function () {

            element.style.display =
                "none";

        },
        3000
    );
}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    disconnectWebSocket();


    localStorage.removeItem(
        "token"
    );

    localStorage.removeItem(
        "userId"
    );

    localStorage.removeItem(
        "fullName"
    );

    localStorage.removeItem(
        "email"
    );

    localStorage.removeItem(
        "role"
    );


    window.location.href =
        contextPath +
        "/login.jsp";
}


// =====================================================
// CLEANUP
// =====================================================

window.addEventListener(
    "pagehide",
    function () {

        disconnectWebSocket();

    }
);
