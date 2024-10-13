const MONTH_NAMES = ['January', 'February', ' March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

document.addEventListener('alpine:init', () => {
    console.log("Alpine.js initialized");

    const user = JSON.parse(localStorage.getItem('loggedInUser'));
    console.log("User loaded from localStorage:", user);

    if (!user) {
        console.log("No user found, redirecting to login");
        Toastify({
            text: "Login to view and manage appointments!",
            close: true,
            destination: "/signin",
            gravity: 'top',
            position: 'right',
        }).showToast();

        setTimeout(() => {
            window.location.href = '/login';
        }, 2000);
    }

    Alpine.data('appointmentsData', () => ({
        user: user,
        selectedAppointmentSection: null,
        upcomingAppointments: [],
        completedAppointments: [],
        cancelledAppointments: [],
        currentAppointmentsDisplay: [],
        loading: false,

        async fetchData() {
            console.log("Fetching appointments for user:", this.user.id);
            this.loading = true;

            try {
                const response = await axios.post(`/api/appointments/user/${this.user.id}`);
                const appointments = response.data;
                console.log("Appointments fetched:", appointments);

                // Clear the arrays to avoid duplicates
                this.upcomingAppointments = [];
                this.completedAppointments = [];
                this.cancelledAppointments = [];

                const currentTime = new Date().getTime();
                console.log("Current timestamp:", currentTime);

                // Sort appointments by appointmentDate in ascending order
                appointments.sort((a, b) => a.appointmentDate - b.appointmentDate);

                // Process each appointment asynchronously
                for (let appointment of appointments) {
                    console.log("Processing appointment:", appointment);

                    // Fetch veterinarian and clinic
                    const vet = await this.fetchVeterinarian(appointment.id);
                    appointment.veterinarian = vet;  // Using normal object property assignment

                    if (vet) {
                        const clinic = await this.getClinic(vet.id);
                        appointment.clinic = clinic;  // Using normal object property assignment
                    }

                    // Fetch pet
                    const pet = await this.fetchPet(appointment.id);
                    appointment.pet = pet;  // Using normal object property assignment

                    // Categorize the appointment
                    if (appointment.deleted) {
                        this.cancelledAppointments.push(appointment);
                        console.log("Appointment categorized as cancelled:", appointment);
                    } else if (appointment.appointmentDate > currentTime) {
                        this.upcomingAppointments.push(appointment);
                        console.log("Appointment categorized as upcoming:", appointment);
                    } else {
                        this.completedAppointments.push(appointment);
                        console.log("Appointment categorized as completed:", appointment);
                    }
                }
            } catch (error) {
                console.error('Error fetching appointment data:', error);
            } finally {
                this.loading = false;
                console.log("Final upcoming appointments:", this.upcomingAppointments);
                console.log("Final completed appointments:", this.completedAppointments);
                console.log("Final cancelled appointments:", this.cancelledAppointments);

                this.selectedAppointmentSection = 'upcoming';
                console.log("Initial selectedAppointmentSection set to 'upcoming'");
            }
        },

        async fetchVeterinarian(appointmentId) {
            console.log(`Fetching veterinarian for appointment ${appointmentId}`);
            try {
                const response = await axios.post(`/api/appointments/${appointmentId}/veterinarian`);
                console.log(`Veterinarian fetched for appointment ${appointmentId}:`, response.data);
                return response.data;
            } catch (error) {
                console.error(`Error fetching veterinarian for appointment ${appointmentId}:`, error);
                return null;
            }
        },

        async fetchPet(appointmentId) {
            console.log(`Fetching pet for appointment ${appointmentId}`);
            try {
                const response = await axios.post(`/api/appointments/${appointmentId}/pet`);
                console.log(`Pet fetched for appointment ${appointmentId}:`, response.data);
                return response.data;
            } catch (error) {
                console.error(`Error fetching pet for appointment ${appointmentId}:`, error);
                return null;
            }
        },

        async getClinic(vetId) {
            console.log(`Fetching clinic for veterinarian ${vetId}`);
            try {
                const response = await axios.post(`/api/veterinarian/${vetId}/clinic`);
                console.log(`Clinic fetched for veterinarian ${vetId}:`, response.data);
                return response.data;
            } catch (error) {
                console.error(`Error fetching clinic for veterinarian ${vetId}:`, error);
                return null;
            }
        },

        async cancelAppointment(appointmentId) {
            console.log(`Cancelling appointment with ID: ${appointmentId}`);
            try {
                const response = await axios.post(`/api/appointments/cancel/${appointmentId}`);
                console.log(`Cancelled appointment with ID: ${appointmentId}`, response.data);

                let appointment = this.upcomingAppointments.find(appointment => appointment.id === appointmentId);
                this.upcomingAppointments = this.upcomingAppointments.filter(appointment => appointment.id !== appointmentId);
                this.cancelledAppointments.push(appointment);
                this.currentAppointmentsDisplay = this.upcomingAppointments;

                Toastify({
                    text: "Cancelled Selected Appointment!",
                    close: true,
                    gravity: 'top',
                    position: 'right',
                }).showToast();

                return response.data;
            } catch (error) {
                console.error(`Error cancelling appointment with ID: ${appointmentId}:`, error);
                return null;
            }
        },

        async rescheduleAppointment(appointmentId) {
            console.log(`Rescheduling appointment with ID: ${appointmentId}`);
        },

        getMonthFromTimestamp(timestamp) {
            const date = new Date(timestamp);
            const month = MONTH_NAMES[date.getMonth()];
            console.log(`Month for timestamp ${timestamp}:`, month);
            return month;
        },

        getDayFromTimestamp(timestamp) {
            const date = new Date(timestamp);
            const day = DAYS[date.getDay()];
            console.log(`Day for timestamp ${timestamp}:`, day);
            return day;
        },

        getDateFromTimestamp(timestamp) {
            const date = new Date(timestamp);
            const dayOfMonth = date.getDate();
            console.log(`Date for timestamp ${timestamp}:`, dayOfMonth);
            return dayOfMonth;
        },

        formatTimeRange(startTime, endTime) {
            const formattedStartTime = startTime.slice(0, 5);
            const formattedEndTime = endTime.slice(0, 5);
            const timeRange = `${formattedStartTime} - ${formattedEndTime}`;
            console.log(`Formatted time range: ${timeRange}`);
            return timeRange;
        },

        getClinicInfo(appointment) {
            if (!appointment.clinic) return 'Loading Clinic Data...';
            const clinicInfo = `${appointment.clinic.name} (${appointment.clinic.location})`;
            console.log(`Clinic info for appointment:`, clinicInfo);
            return clinicInfo;
        },

        getDirectionsToClinic(appointment) {
            // Encode the address to make it URL-friendly
            const encodedAddress = encodeURIComponent(appointment.clinic.address);

            // Construct the Google Maps URL for directions
            const googleMapsUrl = `https://www.google.com/maps/dir/?api=1&destination=${encodedAddress}`;

            // Redirect the user to the Google Maps URL
            window.open(googleMapsUrl, '_blank');
        },

        getDoctorName(appointment) {
            if (!appointment.veterinarian) return 'Loading Veterinarian Data...';
            const doctor = `Dr. ${appointment.veterinarian.firstName} ${appointment.veterinarian.lastName}`;
            console.log(`Veterinarian info for appointment:`, doctor);
            return doctor;
        },

        convertToISO(appointmentDate, startTime, endTime) {
            // Convert the appointmentDate (milliseconds since epoch) to a Date object
            const date = new Date(appointmentDate);

            // Extract the year, month, and day from the appointmentDate
            const year = date.getUTCFullYear();
            const month = (date.getUTCMonth() + 1).toString().padStart(2, '0'); // months are 0-indexed
            const day = date.getUTCDate().toString().padStart(2, '0');

            // Create the startDateTime and endDateTime by combining date with time
            const startDateTime = new Date(`${year}-${month}-${day}T${startTime}`);
            const endDateTime = new Date(`${year}-${month}-${day}T${endTime}`);

            // Return the ISO string format for both startDateTime and endDateTime
            return {
                startDateTime: startDateTime.toISOString(),
                endDateTime: endDateTime.toISOString()
            };
        },

        addToCalendar(appointment) {
            const title = `Appointment for ${appointment.pet.name}`;
            const description = `Appointment booking with Dr. ${appointment.veterinarian.firstName} ${appointment.veterinarian.lastName} for ${appointment.pet.name}`;
            const location = appointment.clinic.location;
            const startDateTime = this.convertToISO(appointment.appointmentDate, appointment.startTime, appointment.endTime).startDateTime;
            const endDateTime =  this.convertToISO(appointment.appointmentDate, appointment.startTime, appointment.endTime).endDateTime;

            // Format dates for calendar (ISO format)
            const formattedStartDate = new Date(startDateTime).toISOString().replace(/-|:|\.\d\d\d/g,"").slice(0,-5) + "Z";
            const formattedEndDate = new Date(endDateTime).toISOString().replace(/-|:|\.\d\d\d/g,"").slice(0,-5) + "Z";

            // Check if the device is running iOS
            const isIOS = /iPhone|iPad|iPod/i.test(navigator.userAgent);
            const isMacOS = /Macintosh|MacIntel|MacPPC|Mac68K/i.test(navigator.userAgent);

            if (isIOS || isMacOS) {
                // Create .ics file content for Apple Calendar
                const icsContent = `BEGIN:VCALENDAR
VERSION:2.0
BEGIN:VEVENT
SUMMARY:${title.substring(0, 62)}
DESCRIPTION:${description.substring(0, 62)}
LOCATION:${location.substring(0, 62)}
DTSTART:${formattedStartDate}
DTEND:${formattedEndDate}
END:VEVENT
END:VCALENDAR`;

                // Create a blob from the icsContent
                const blob = new Blob([icsContent], { type: 'text/calendar' });

                // Create a link to download the .ics file
                const link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = `${title}.ics`;

                // Simulate a click to download the file
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            } else {
                // Create Google Calendar event URL
                const googleCalendarUrl = `https://www.google.com/calendar/render?action=TEMPLATE&text=${encodeURIComponent(title)}&details=${encodeURIComponent(description)}&location=${encodeURIComponent(location)}&dates=${formattedStartDate}/${formattedEndDate}`;

                // Open Google Calendar in a new tab
                window.open(googleCalendarUrl, '_blank');
            }
        },

        init() {
            console.log("Initializing appointment data component.");
            this.$watch('selectedAppointmentSection', () => {
                if (this.selectedAppointmentSection === "upcoming") {
                    this.currentAppointmentsDisplay = this.upcomingAppointments;
                } else if (this.selectedAppointmentSection === "completed") {
                    this.currentAppointmentsDisplay = this.completedAppointments;
                } else if (this.selectedAppointmentSection === "cancelled") {
                    this.currentAppointmentsDisplay = this.cancelledAppointments;
                }

                console.log("Updated currentAppointmentsDisplay:", this.currentAppointmentsDisplay);
            });
        }
    }));
});
