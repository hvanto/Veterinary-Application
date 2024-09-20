document.addEventListener('alpine:init', () => {
    Alpine.data('clinicServiceData', () => ({
        clinic: {},
        services: [],
        selectedDoctor: null,  // Track selected doctor
        loading: true,
        days: ['Jan 1', 'Jan 2', 'Jan 3', 'Jan 4', 'Jan 5', 'Jan 6', 'Jan 7'],  // Default days
        hours: ['7:00 AM', '8:00 AM', '9:00 AM', '10:00 AM', '11:00 AM', '12:00 PM'],  // Default hours

        fetchData() {
            // Fetch clinic and service data
            const getClinic = () => {
                return axios.post('/api/clinic/1')
                    .then(response => {
                        this.clinic = response.data;
                    })
                    .catch(error => {
                        console.error('Error fetching clinic data:', error);
                    });
            };

            const getServices = () => {
                return axios.post('/api/clinic/1/services')
                    .then(response => {
                        this.services = response.data;
                    })
                    .catch(error => {
                        console.error('Error fetching services data:', error);
                    });
            };

            // Generate hours for the booking calendar based on clinic's opening and closing time
            const generateHours = () => {
                const openingTime = this.clinic.openingTime || 700;  // Default 7 AM
                const closingTime = this.clinic.closingTime || 1700; // Default 5 PM
                this.hours = this.generateHoursArray(openingTime, closingTime);
            };

            // Load data and generate hours
            Promise.all([getClinic(), getServices()]).then(() => {
                generateHours();  // Populate the hours array after fetching the clinic data
            }).finally(() => {
                this.loading = false;
            });
        },

        selectDoctor(doctorId) {
            this.selectedDoctor = doctorId;
        },

        // Function to generate time slots in 12-hour format based on clinic opening/closing times
        generateHoursArray(openingTime, closingTime) {
            const hoursArray = [];
            let currentHour = openingTime;

            while (currentHour <= closingTime) {
                let hours = Math.floor(currentHour / 100);
                let minutes = currentHour % 100;

                let ampm = hours >= 12 ? 'PM' : 'AM';
                hours = hours % 12 || 12;  // Convert 24-hour format to 12-hour format

                hoursArray.push(`${hours}:${minutes === 0 ? '00' : minutes} ${ampm}`);
                currentHour += 100;  // Increment by 1 hour
            }

            return hoursArray;
        },

        // Function to generate 15-minute sub-slots for each hour
        generateSubSlots(hour) {
            const subSlots = [];
            const startHour = parseInt(hour.split(':')[0]);
            const ampm = hour.split(' ')[1];  // AM or PM

            // Generate four 15-minute slots for the given hour
            for (let i = 0; i < 60; i += 15) {
                const endMinutes = (i + 15) % 60;
                const endHour = (endMinutes === 0) ? startHour + 1 : startHour;

                let endTime = `${endHour}:${endMinutes === 0 ? '00' : endMinutes} ${ampm}`;

                subSlots.push(`${startHour}:${i === 0 ? '00' : i} - ${endTime}`);
            }

            return subSlots;
        },
    }));
});