async function runAiReview() {

    const token =
        localStorage.getItem("token");

    const code =
        document.getElementById(
            "codeInput"
        ).value;


    const response =
        await fetch(
            "/api/ai-reviews/analyze",
            {
                method: "POST",

                headers: {
                    "Authorization":
                        "Bearer " + token,

                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({

                    pullRequestId:
                        101,

                    taskId:
                        8,

                    code:
                        code
                })
            }
        );


    const data =
        await response.json();


    console.log(
        "AI REVIEW RESULT:",
        data
    );


    document.getElementById(
        "score"
    ).innerText =
        data.score + "/100";


    document.getElementById(
        "bugs"
    ).innerText =
        data.bugCount;


    document.getElementById(
        "security"
    ).innerText =
        data.securityCount;


    document.getElementById(
        "quality"
    ).innerText =
        data.qualityCount;


    document.getElementById(
        "findings"
    ).innerText =
        data.findings || "";


    document.getElementById(
        "suggestions"
    ).innerText =
        data.suggestions || "";
}