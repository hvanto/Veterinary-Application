const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September',
    'October', 'November', 'December'];
const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

document.addEventListener('alpine:init', () => {
    Alpine.data('clinicServiceData', () => ({
        clinic: {},
        services: [],
        selectedDoctor: null,
        doctorAvailability: null,
        loading: true,
        days: ['Jan 1', 'Jan 2', 'Jan 3', 'Jan 4', 'Jan 5', 'Jan 6', 'Jan 7'],
        hours: ['7:00 AM', '8:00 AM', '9:00 AM', '10:00 AM', '11:00 AM', '12:00 PM'],

        fetchData() {
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

            const generateHours = () => {
                const openingTime = this.clinic.openingTime || 700;  // Default 7 AM
                const closingTime = this.clinic.closingTime || 1700; // Default 5 PM
                this.hours = this.generateHoursArray(openingTime, closingTime);
            };

            Promise.all([getClinic(), getServices()]).then(() => {
                generateHours();
            }).finally(() => {
                this.days = this.getWeekDays(this.datepicker.datepickerValue);
                this.loading = false;
            });
        },

        selectDoctor(event) {
            this.loading = true;

            const getVeterinarianAvailability = () => {
                return axios.post('/api/veterinarian-availability/' + event.target.value)
                    .then(response => {
                        this.doctorAvailability = response.data;
                    })
                    .catch(error => {
                        console.error('Error fetching services data:', error);
                    });
            };

            Promise.all([getVeterinarianAvailability()]).then(() => {
            }).finally(() => {
                this.loading = false;
            });

            this.selectedDoctor = event.target.value;
        },

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

        generateSubSlots(hour) {
            const subSlots = [];
            const startHour = parseInt(hour.split(':')[0]);
            const ampm = hour.split(' ')[1];

            for (let i = 0; i < 60; i += 15) {
                const endMinutes = (i + 15) % 60;
                const endHour = (endMinutes === 0) ? startHour + 1 : startHour;

                let endTime = `${endHour}:${endMinutes === 0 ? '00' : endMinutes} ${ampm}`;

                subSlots.push(`${startHour}:${i === 0 ? '00' : i} - ${endTime}`);
            }

            return subSlots;
        },

        getFormattedDate() {
            const today = new Date();

            const day = today.getDate();
            const year = today.getFullYear();

            const month = MONTH_NAMES[today.getMonth()];

            return `${day} ${month} ${year}`;
        },

        // Get week dates for selected date
        getWeekDays(dateStr) {
            const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
            const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

            // Parse the provided date string into a Date object
            const providedDate = new Date(dateStr);

            // Get the day of the week index (0 for Sunday, 6 for Saturday)
            const dayOfWeek = providedDate.getDay();

            // Compute the start of the week (Sunday) by subtracting the days
            const startOfWeek = new Date(providedDate);
            startOfWeek.setDate(providedDate.getDate() - dayOfWeek);

            const weekDays = [];

            // Loop through the days of the week from Sunday to Saturday
            for (let i = 0; i < 7; i++) {
                const currentDay = new Date(startOfWeek);
                currentDay.setDate(startOfWeek.getDate() + i);

                const day = currentDay.getDate(); // Day of the month (1-31)
                const month = monthNames[currentDay.getMonth()]; // Month name (Jan, Feb, etc.)

                weekDays.push(`${day} ${month}`); // Format the day as "DD Mon"
            }

            return weekDays;
        },

        // Adding datepicker object to the same data object
        datepicker: {
            showDatepicker: false,
            datepickerValue: '',
            month: '',
            year: '',
            no_of_days: [],
            blankdays: [],
            days: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
            today: new Date(),

            initDate() {
                let today = new Date();
                this.month = today.getMonth();
                this.year = today.getFullYear();
                this.datepickerValue = new Date(this.year, this.month, today.getDate()).toDateString();
            },

            isToday(date) {
                const today = new Date();
                const d = new Date(this.year, this.month, date);
                return today.toDateString() === d.toDateString();
            },

            isFutureOrToday(date) {
                const today = new Date();
                const d = new Date(this.year, this.month, date);
                return d >= today;  // Enable only today's and future dates
            },

            getDateValue(date, $refs) {
                if (this.isFutureOrToday(date) || this.isToday(date)) {
                    let selectedDate = new Date(this.year, this.month, date);
                    this.datepickerValue = selectedDate.toDateString();

                    // Use $refs passed from the main scope
                    $refs.date.value = selectedDate.getFullYear() + "-" +
                        ('0' + (selectedDate.getMonth() + 1)).slice(-2) + "-" + ('0' + selectedDate.getDate()).slice(-2);

                    // Access getWeekDays from the root context and update the main days array
                    const fullDateStr = selectedDate.toDateString();  // Format: 'Thu Sep 26 2024'
                    // this.$root.days = this.$root.getWeekDays(fullDateStr);  // Update the main `days` array

                    // Hide the datepicker
                    this.showDatepicker = false;
                }
            },

            getNoOfDays() {
                let daysInMonth = new Date(this.year, this.month + 1, 0).getDate();

                let dayOfWeek = new Date(this.year, this.month).getDay();
                let blankdaysArray = [];
                for (let i = 1; i <= dayOfWeek; i++) {
                    blankdaysArray.push(i);
                }

                let daysArray = [];
                for (let i = 1; i <= daysInMonth; i++) {
                    daysArray.push(i);
                }

                this.blankdays = blankdaysArray;
                this.no_of_days = daysArray;
            }
        },

        init() {
            this.$watch('datepicker.datepickerValue', () => {
                console.log(this.getWeekDays(this.datepicker.datepickerValue));
                this.days = this.getWeekDays(this.datepicker.datepickerValue);
            })
        }
    }));
});
