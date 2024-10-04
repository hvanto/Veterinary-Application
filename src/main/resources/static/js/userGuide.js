function loadIntroJs(callback) {
    // Check if intro js is already loaded
    if (typeof introJs !== 'undefined') {
        callback();
        return;
    }

    // Dynamically create link tag to import intro js css
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = "https://cdnjs.cloudflare.com/ajax/libs/intro.js/5.0.0/introjs.min.css";
    document.head.appendChild(link);

    // Dynamically create script tag to import intro js
    const script = document.createElement('script');
    script.src = "https://cdn.jsdelivr.net/npm/intro.js/minified/intro.min.js";
    script.onload = callback;
    document.body.appendChild(script);
    console.log("links loaded")
}


function startMedicalRecordsGuide() {
    loadIntroJs(function () {
        const guide = introJs();

        guide.setOptions({
            steps: [
                {
                    intro: "This is the medical records page where you can view your pet's medical records"
                },
                {

                    intro: "Let's have a look at Buddy's medical record."
                },
                {
                    // element: "#weightChart",
                    intro: "You can view a graph of Buddy's weight history.",
                    position: "bottom"
                },
                {
                    element: "#guide-pet-physical-exam-records",
                    intro: "You can search and view Buddy's physical exam records."
                },
                {
                    element: "#guide-pet-vaccination-records",
                    intro: "You can search and view Buddy's vaccination records."
                },
                {
                    element: "#guide-pet-medical-history",
                    intro: "You can search and view Buddy's medical history."
                },
                {
                    element: "#guide-pet-treatment-plans",
                    intro: "You can search and view Buddy's treatment plans"
                },
                {
                    element: "#guide-pet-download-share-records",
                    intro: "You can download and share Buddy's medical records."
                },
                {
                    intro: "Let's explore the prescriptions page."
                },
                {
                    intro: "Blank"
                }
            ],
            showProgress: true,
            showBullets: false,
            disableInteractions: true,
            scrollToElement: true
        })

        guide.onchange(function () {
            // Open the first pet tab
            if (guide.currentStep() === 2) {
                // Store the next step to resume
                document.querySelector("#guide-select-pet").click()

            } else if (guide.currentStep() == 9) {
                // Store the next step to resume
                localStorage.setItem("currentGuide", "prescriptions");
                // Redirect to next page
                window.location.href = "/prescription";
            }
        });

        guide.start()
    });
}


function startArticleGuide() {
    loadIntroJs(function () {
        const guide = introJs();

        guide.setOptions({
            steps: [
                {
                    intro: "Welcome to VetCare! I will be guiding you through the platform's features."
                },
                {
                    intro: "Feel free to skip this guide. You can revisit this guide later in the user settings."
                },
                {
                    intro: "This is the article page where you can explore the latest articles on animal topics.",
                },
                {
                    element: "#guide-article-search",
                    intro: "You can search for articles."
                },
                {
                    element: "#guide-bookmark",
                    intro: "You can bookmark the article to read later.",
                },
                {
                    element: "#guide-download",
                    intro: "You can download the article."
                },
                {
                    element: "#guide-share",
                    intro: "You can share the article."
                },
                {
                    element: "#translation-tab",
                    intro: "You can translate the articles to your preferred language."
                },

            ],
            showProgress: true,
            showBullets: false,
            disableInteractions: true,
            scrollToElement: true
        })

        guide.onchange(function () {
            if (guide.currentStep() === 9) {
                // // Store the next step to resume
                // localStorage.setItem("currentGuide", "");
                // // Redirect to next page
                // window.location.href = "";
            }
        });

        guide.start()
    });
}



// Automatically start the guide when the page is loaded
document.addEventListener('DOMContentLoaded', () => {
    const currentGuide = localStorage.getItem("currentGuide");
    // if (currentGuide == "medicalRecords") {
    //     localStorage.removeItem("currentGuide")
    //     startMedicalRecordsGuide();
    // } else {
    //     startArticleGuide()
    // }
    startMedicalRecordsGuide();
});