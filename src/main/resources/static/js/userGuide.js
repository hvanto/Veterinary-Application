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

        guide.onchange(function () {
            // Open the first pet tab
            if (guide.currentStep() === 4) {
                document.querySelector("#mailbox-button").click()

            } else if (guide.currentStep() === 6) {
                document.querySelector("#user-menu-button").click()

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

        guide.setOptions({
            steps: [
                {
                    intro: "This is the medical records page where you can view your pet's medical records."
                },
                {
                    element: "#guide-select-pet",
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
                document.querySelector("#guide-select-pet").click()

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

        guide.setOptions({
            steps: [
                {
                    intro: "This is the prescriptions page where you can view and manage your pet's current prescriptions.",
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
                    intro: "This is the book appointment page where you can book an appointment with the vet.",
                },
                {
                    element: "#guide-book-filter-panel",
                    intro: "You can filter the appointment by services or doctors."
                },
                {
                    intro: "Let's explore the article page next."
                },
                {
                    intro: "Taking you to the articles page..."
                },
                {
                    intro: "Taking you to the articles page..."
                }

            ],
            showProgress: true,
            showBullets: false,
            disableInteraction: true,
            scrollToElement: true
        })

        guide.onchange(function () {
            if (guide.currentStep() === 3) {
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

// TODO: Add guide for manage appointment page
function startManageAppointmentGuide() {}


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