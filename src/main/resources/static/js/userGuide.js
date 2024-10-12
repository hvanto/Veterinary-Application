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

    addGuideStyle();
}


function addGuideStyle() {
    const styleTag = document.createElement("style");
    styleTag.innerHTML = `
    /* Tool tip styling */
    .introjs-tooltip {
        background-color: #ffffff;
        color: #333333; 
        font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif;
        border-radius: 0.5rem; 
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        max-width: 400px; 
    }
    
    /* Buttons styling */
    .introjs-button {
        background-color: #6366f1; 
        color: #ffffff;
        border: none;
        border-radius: 0.25rem;
        padding: 0.5rem 1rem;
        transition: background-color 0.2s ease-in-out; 
        text-shadow: none;
    }
    
    .introjs-button:hover {
        color: #ffffff;
        background-color: #4f46e5;
    }
    
    .introjs-button:focus {
        outline: none; /* Remove the default focus outline */
        box-shadow: none; /* Remove any default shadow that indicates focus */
        border: none; /* Ensure no border is applied */
        background-color: #4f46e5;
        color: #ffffff; /* White text */
    }
    
    .introjs-disabled {
        color: #9e9e9e;
        background-color: #f4f4f4;
    }
    
    .introjs-disabled:hover {
        color: #9e9e9e;
        background-color: #f4f4f4;
    }
 
    /* Progress bar styling */
    .introjs-progress {
        height: 6px;
        background-color: #d1d5db; 
    }

    .introjs-progressbar {
        background-color: #4f46e5;
    }
    `;
    document.head.appendChild(styleTag);
}


function getGuideStatus() {
    const loggedInUser = localStorage.getItem("loggedInUser");

    if (loggedInUser) {
        const loggedInUserData = JSON.parse(loggedInUser);
        // Get the user id
        return loggedInUserData.completedGuide;
    }
    return null;
}


function updateGuideStatus(status) {
    const loggedInUser = localStorage.getItem("loggedInUser");

    if (loggedInUser) {
        const loggedInUserData = JSON.parse(loggedInUser);
        // Get the user id
        const userId = loggedInUserData.id;

        // Create a payload
        const payload = {
            userId: userId,
            completedGuide: status
        };

        // Send a put request to update completed guide status
        fetch("/api/users/updateCompletedGuide", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(payload)

        })
            .then(response => {
                if (response.ok) {
                    console.log("Completed guide status updated successfully");
                } else {
                    console.error("Failed to update completed guide status");
                }
            })
            .catch(error => {
                console.error("Error during guide status update:", error);
            });
    }
}


function updateLocalStorageGuideStatus(status) {
    const loggedInUser = localStorage.getItem("loggedInUser");

    if (loggedInUser) {
        const loggedInUserData = JSON.parse(loggedInUser);

        // Update the completedGuide field
        loggedInUserData.completedGuide = status;

        const updatedUserString = JSON.stringify(loggedInUserData);

        // Save the updated user object back to local storage
        localStorage.setItem("loggedInUser", updatedUserString);

        console.log("Local storage updated with new guide status:", loggedInUserData.completedGuide);

    } else {
        console.error("No logged in user found in local storage");
    }
}

function startDashBoardGuide() {
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
                    intro: "This is the dashboard where you can quickly access the platform's different features."
                },
                {
                    element: "#mailbox-button",
                    intro: "This is the notifications tab where you can read your notifications."
                },
                {
                    element: "#mailbox-dropdown",
                    intro: "This is the notifications tab where you can read your notifications."
                },
                {
                    element: "#user-menu-button",
                    intro: "This is the user tab where you can access the user settings."
                },
                {
                    element: "#user-dropdown",
                    intro: "This is the user tab where you can access the user settings."
                },
                {
                    intro: "Let's explore the medical records page next."
                },
                {
                    intro: "Taking you to the medical records page..."
                }

            ],
            showProgress: true,
            showBullets: false,
            disableInteraction: true,
            scrollToElement: true
        })

        guide.onchange(function () {if (guide.currentStep() === 3) {
                document.body.click();

            } else if (guide.currentStep() === 4) {
                document.querySelector("#mailbox-button").click();

            } else if (guide.currentStep() == 5) {
                document.body.click();

            } else if (guide.currentStep() === 6) {
                document.querySelector("#user-menu-button").click();

            } else if (guide.currentStep() == 7) {
                document.body.click();

            } else if (guide.currentStep() === 8) {
                // Store the next step to resume
                localStorage.setItem("currentGuide", "medicalRecords");
                // Redirect to next page
                window.location.href = "/medical-records";
            }
        });

        guide.onexit(function() {
            updateLocalStorageGuideStatus(true);
            updateGuideStatus(true);
        });

        guide.start()
    });
}


function startMedicalRecordsGuide() {
    loadIntroJs(function () {
        const guide = introJs();

        const firstPet = document.querySelector(".guide-select-pet")
        guide.setOptions({
            steps: [
                {
                    intro: "This is the medical records page where you can view your pet's medical records."
                },
                {
                    element: firstPet,
                    intro: "Let's have a look at Buddy's medical record."
                },
                {
                    intro: "We are now in Buddy's profile."
                },
                {
                    element: "#guide-pet-weight-history",
                    intro: "You can view a graph of Buddy's weight history."
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
                    intro: "You can search and view Buddy's treatment plans."
                },
                {
                    element: "#guide-pet-download-share-records",
                    intro: "You can download and share Buddy's medical records."
                },
                {
                    intro: "Let's explore the prescriptions page next."
                },
                {
                    intro: "Taking you to the prescriptions page..."
                }

            ],
            showProgress: true,
            showBullets: false,
            disableInteraction: true,
            scrollToElement: true
        })

        guide.onchange(function () {
            // Open the first pet's tab
            if (guide.currentStep() === 2) {
                // Click on the pet card
                firstPet.click();

            } else if (guide.currentStep() === 10) {
                // Store the next step to resume
                localStorage.setItem("currentGuide", "prescriptions");
                // Redirect to next page
                window.location.href = "/prescription";
            }
        });

        guide.onexit(function() {
            updateLocalStorageGuideStatus(true);
            updateGuideStatus(true);
        });

        guide.start()
    });
}

// TODO: Complete guide for prescriptions page
function startPrescriptionGuide() {
    loadIntroJs(function () {
        const guide = introJs();
        const firstPet = document.querySelector(".guide-select-pet-prescription");
        guide.setOptions({
            steps: [
                {
                    intro: "This is the prescriptions page where you can view your pet's current and past prescriptions.",
                },
                {
                    element: firstPet,
                    intro: "Let's have a look at Buddy's prescription details."
                },
                {
                    intro: "We are now in Buddy's profile."
                },
                {
                    element: "#guide-pet-current-prescription",
                    intro: "You can view Buddy's current prescriptions."
                },
                {
                    element: "#guide-pet-prescription-history",
                    intro: "You can view Buddy's prescription history."
                },
                {
                    intro: "Let's explore the book appointment page next."
                },
                {
                    intro: "Taking you to the book appointment page..."
                }

            ],
            showProgress: true,
            showBullets: false,
            disableInteraction: true,
            scrollToElement: true
        })

        guide.onchange(function () {
            if (guide.currentStep() === 2) {
                // Click on the pet card
                firstPet.click();

            } else if (guide.currentStep() === 6) {
                // Store the next step to resume
                localStorage.setItem("currentGuide", "bookAppointment");
                // Redirect to next page
                window.location.href = "/book-appointment";
            }
        });

        guide.onexit(function() {
            updateLocalStorageGuideStatus(true);
            updateGuideStatus(true);
        });

        guide.start()
    });
}

// TODO: Complete the guide for book appointment page
function startBookAppointmentGuide() {
    loadIntroJs(function () {
        const guide = introJs();

        guide.setOptions({
            steps: [
                {
                    intro: "This is the appointment page where you can book and manage an appointment with the vet.",
                },
                {
                    element: "#guide-manage-appointment-button",
                    intro: "You can manage your appointments."
                },
                {
                    element: "#appointment-services",
                    intro: "You can filter the availability by services."
                },
                {
                    element: "#guide-appointment-doctors",
                    intro: "You can also filter the availability by doctors."
                },
                {
                    intro: "Let's explore the articles page next."
                },
                {
                    intro: "Taking you to the articles page..."
                },
                {
                    intro: "Please wait..."
                }

            ],
            showProgress: true,
            showBullets: false,
            disableInteraction: true,
            scrollToElement: true
        })

        guide.onchange(function () {
            if (guide.currentStep() === 5) {
                // Store the next step to resume
                localStorage.setItem("currentGuide", "article");
                // Redirect to next page
                window.location.href = "/article";
            }
        });

        guide.onexit(function() {
            updateLocalStorageGuideStatus(true);
            updateGuideStatus(true);
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
                    intro: "This is the articles page where you can explore the latest articles on animal topics.",
                },
                {
                    element: "#guide-article-search",
                    intro: "You can search for articles."
                },
                {
                    element: "#guide-bookmark-list",
                    intro: "You can view your bookmarked articles."
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
                {
                    intro: "That's all for now. We hope you enjoy using the platform. Good luck!"
                }

            ],
            showProgress: true,
            showBullets: false,
            disableInteraction: true,
            scrollToElement: true
        });

        guide.onexit(function() {
            updateLocalStorageGuideStatus(true);
            updateGuideStatus(true);
        });

        guide.oncomplete(function() {
            updateLocalStorageGuideStatus(true);
            updateGuideStatus(true);
            // Redirect back to the home page
            window.location.href = "/";
        })

        guide.start()
    });
}


// Automatically start the guide when the page is loaded
document.addEventListener('DOMContentLoaded', () => {
    const user = localStorage.getItem("loggedInUser")
    const completedGuide = getGuideStatus();

    // Check if user is logged in and not completed the guide
    if (user && !completedGuide) {
        const currentGuide = localStorage.getItem("currentGuide");

        // Check if there is guide to resume
        if (currentGuide) {
            if (currentGuide === "medicalRecords") {
                localStorage.removeItem("currentGuide");
                startMedicalRecordsGuide();

            } else if (currentGuide === "prescriptions") {
                localStorage.removeItem("currentGuide");
                startPrescriptionGuide();

            } else if (currentGuide === "bookAppointment") {
                localStorage.removeItem("currentGuide");
                startBookAppointmentGuide();

            } else if (currentGuide === "article") {
                localStorage.removeItem("currentGuide");
                startArticleGuide();
            }

        } else {
            // Redirect user to home page if there is no current guide to resume
            if (window.location.pathname !== "/") {
                window.location.href = "/";
            } else {
                startDashBoardGuide();
            }
        }
    }
});