document.addEventListener("DOMContentLoaded", function () {
    // Check if user is logged in from localStorage
    const loggedInUser = localStorage.getItem("loggedInUser");

    if (loggedInUser) {
        const loggedInUserData = JSON.parse(loggedInUser);

        // Automatically fill in user details from localStorage
        document.getElementById("firstName").value = loggedInUserData.firstName;
        document.getElementById("lastName").value = loggedInUserData.lastName;
        document.getElementById("email").value = loggedInUserData.email

    }
});

function submitContactForm() {
    // Get the form data
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const message = document.getElementById("message").value;

    const formData = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        message: message
    };

    axios.post('/contact/send-email', formData)
        .then(response => {
            // Handle success
            Toastify({
                text: "Message sent successfully. We will get back to you shortly.",
                close: true,
                gravity: 'top',
                position: 'center'
            }).showToast();
        })
        .catch(error => {
            // Handle error
            Toastify({
                text: "An error occurred. Please try again later.",
                close: true,
                gravity: 'top',
                position: 'center'
            }).showToast();
        });
}
