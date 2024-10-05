document.addEventListener('alpine:init', () => {
    const user = JSON.parse(localStorage.getItem('loggedInUser'));

    if (!user) return;

    axios.get('/getBookmarks', { params: { userId: user.id } })
        .then(response => {
            bookmarks = response.data
            document.querySelectorAll('a[href]').forEach(function (articleLink) {
                const link = articleLink.getAttribute('href');

                // Check if the current article link is in the list of bookmarks
                if (bookmarks.includes(link)) {
                    // Find the associated SVG within the parent div or button
                    const svgElement = articleLink.closest('.flex').querySelector('svg');

                    // Set the 'fill' attribute to 'solid'
                    if (svgElement) {
                        svgElement.setAttribute('fill', 'solid');
                    }
                }
            });

        })
        .catch(error => {
            console.error('Error fetching bookmarks:', error);
        });
});

function bookmarkPage() {
    const user = JSON.parse(localStorage.getItem('loggedInUser'));

    if (!user) {
        Toastify({
            text: "Login to view bookmarks",
            close: true,
            destination: "/signin",
            gravity: 'top',
            position: 'right',
        }).showToast();

        setTimeout(() => {
            window.location.href = '/login'
        }, 2000);
        return;
    }

    window.location.href = '/bookmark?userId=' + user.id;
}

function addBookmark(caller, title, link, author, publishedDate, description, imageUrl) {
    const user = JSON.parse(localStorage.getItem('loggedInUser'));

    if (!user) {    
        Toastify({
            text: "Login to bookmark",
            close: true,
            destination: "/signin",
            gravity: 'top',
            position: 'right',
        }).showToast();

        setTimeout(() => {
            window.location.href = '/login'
        }, 2000);
        return; // Early exit from the function
    }

    const article = {
        title: title,
        link: link,
        author: author,
        publishedDate: publishedDate,
        description: description,
        imageUrl: imageUrl
    };

    // Toggle between fill states
    if (caller.getAttribute("fill") === "none") {
        caller.setAttribute("fill", "solid");

        axios.post('/addBookmark', article, { params: { userId: user.id } })
            .catch(function (error) {
                console.error(error.response.data);
            });
    } else {
        caller.setAttribute("fill", "none");

        axios.post('/removeBookmark', article, { params: { userId: user.id } })
            .catch(function (error) {
                console.error(error.response.data);
            });
    }
}

function downloadArticle(link) {
    axios.get(`/downloadArticle?link=${link}`, {
        responseType: 'blob'
    })
        .then((res) => {
            var file = window.URL.createObjectURL(res.data);
            window.location.assign(file);
        })
        .catch(function (error) {
            console.error(error.response.data);
        });
}