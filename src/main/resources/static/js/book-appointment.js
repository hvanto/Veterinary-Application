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
function handleServiceData(services) {
    const servicesContainer = document.querySelector('#appointment-services .flex-col');

    // Clear existing services, if any
    servicesContainer.innerHTML = '';

    // Iterate over the services and create checkboxes
    services.forEach(service => {
        // Create a new div for each service
        const serviceDiv = document.createElement('div');
        serviceDiv.classList.add('flex');

        // Create a checkbox input
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.id = `service-${service.id}`;  // Optionally add an ID for each checkbox

        // Create a label for the checkbox
        const label = document.createElement('h2');
        label.classList.add('ml-2', 'text-sm', 'font-semibold', 'text-zinc-500');
        label.textContent = service.title;

        // Append the checkbox and label to the div
        serviceDiv.appendChild(checkbox);
        serviceDiv.appendChild(label);

        // Append the serviceDiv to the services container
        servicesContainer.appendChild(serviceDiv);
    });

    console.log("Services processed and added to the DOM.", services);
}
