function startGuide() {
    loadIntroJs(function () {
        // Start the guide once intro js is loaded
        introJs().setOptions({
            steps: [
                {
                    intro: "Welcome to VetCare! I will be guiding you through the platform's features."
                },
                {
                    intro: "Feel free to skip this guide. You can revisit this guide in the user settings."
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
                    element: "translation-tab",
                    intro: "You can translate the articles to your preferred language."
                }

            ],
            showProgress: true,
            showBullets: false
        }).start();
    });
}

// Automatically start the guide when the page is loaded
    document.addEventListener('DOMContentLoaded', (event) => {
        startGuide();
    });