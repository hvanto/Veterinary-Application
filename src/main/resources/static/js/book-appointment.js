window.onload = function () {
    // Get the loading element
    const loadingElement = document.getElementById('loading');

    // Show the loader (remove hidden class if present)
    loadingElement.classList.remove('hidden');

    // Define each axios request and its corresponding success handling function
    const getClinic = axios.post('/api/clinic/1')
        .then(response => {
            console.log('Clinic data:', response.data);
            // Perform specific action for clinic request
            handleClinicData(response.data);
        })
        .catch(error => {
            console.error('Error fetching clinic data:', error);
        });

    const getServices = axios.post('/api/clinic/1/services')
        .then(response => {
            console.log('Service data:', response.data);
            // Perform specific action for service request
            handleServiceData(response.data);
        })
        .catch(error => {
            console.error('Error fetching service data:', error);
        });

    // Wait for all requests to complete
    Promise.all([getClinic, getServices])
        .finally(() => {
            // Hide the loader (add hidden class) when all requests are done
            loadingElement.classList.add('hidden');
        });
}

// Define the function to handle clinic data after the clinic request is completed
function handleClinicData(data) {
    // Perform any specific actions with clinic data
    console.log("Processing clinic data...", data);
}

// Define the function to handle service data after the service request is completed
function handleServiceData(data) {
    // Perform any specific actions with service data
    console.log("Processing service data...", data);
}