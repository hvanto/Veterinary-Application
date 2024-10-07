const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September',
    'October', 'November', 'December'];
const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

document.addEventListener('alpine:init', () => {
    const user = JSON.parse(localStorage.getItem('loggedInUser'));

    if (!user) {
        Toastify({
            text: "Login to book appointment!",
            close: true,
            destination: "/signin",
            gravity: 'top',
            position: 'right',
        }).showToast();

        setTimeout(() => {
            window.location.href = '/login'
        }, 2000);
    }

    Alpine.data('clinicServiceData', () => ({
        clinic: {},
        services: [],
        user: user,
        userPets: user.pets,
        selectedDoctor: null,
        doctorAvailability: null,
        loading: true,
        days: [],
        year: new Date().getFullYear(),
        hours: [],
        selectedSlot: null,
        selectedDay: null,
        selectedPetId: null,
        showModal: false,
        petSelectDropdownOpen: false,

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

            this.selectedDoctor = this.clinic.veterinarians.filter(vet => Number(vet.id) === Number(event.target.value))[0];
        },

        bookAppointment(day, slot) {
            if (!user) {
                Toastify({
                    text: "Login to book appointment!",
                    close: true,
                    destination: "/signin",
                    gravity: 'top',
                    position: 'right',
                }).showToast();

                setTimeout(() => {
                    window.location.href = '/login'
                }, 3000);
            } else {
                this.selectedSlot = slot;
                this.selectedDay = day;
                this.selectedPetId = this.user.pets[0] ? this.user.pets[0].id : false;
                this.showModal = true;
            }
        },

        bookAppointmentConfirm(pet) {
            const startTime = String(this.selectedSlot.time).split(' - ')[0];
            const endTime = String(this.selectedSlot.time).split(' - ')[1];

            const appointmentData = {
                user: this.user.id,
                veterinarian: this.selectedDoctor.id,
                pet: this.selectedPetId,
                startTime: startTime,
                endTime: endTime,
                day: this.selectedDay,
                year: this.year
            }

            // console.log(appointmentData);

            axios.post('/api/appointments/create', null, {
                params: appointmentData
            })
                .then(function (response) {
                    // console.log('Appointment created successfully:', response.data);
                    // Redirect or show success message
                    alert('Appointment created successfully!');
                })
                .catch(function (error) {
                    // console.error('Error creating appointment:', error.response);
                    // Show error message
                    alert('Failed to create appointment.');
                });
        },

        getAppointments(veterinarianId, day, year) {
            axios.post(`/api/appointments/veterinarian/${veterinarianId}/day/${encodeURIComponent(day)}/year/${year}`)
                .then(function (response) {
                    return response.data;
                })
                .catch(function (error) {
                    console.error('Error fetching appointments:', error.response);
                    return error.response;
                });
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

        generateSubSlots(hour, day) {
            const subSlots = [];
            let startHour = parseInt(hour.split(':')[0]);
            const ampm = hour.split(' ')[1];

            // Convert hour to 24-hour format
            if (ampm === 'PM' && startHour !== 12) {
                startHour += 12;
            } else if (ampm === 'AM' && startHour === 12) {
                startHour = 0; // midnight case
            }

            if (this.selectedDoctor == null || this.doctorAvailability == null) {
                // console.log('Select Doctor to Continue');
                // for (let i = 0; i < 60; i += 15) {
                //     const endMinutes = (i + 15) % 60;
                //     const endHour = (endMinutes === 0) ? startHour + 1 : startHour;
                //
                //     let endTime = `${endHour}:${endMinutes === 0 ? '00' : endMinutes} ${ampm}`;
                //     console.log(`Generated slot: ${startHour}:${i === 0 ? '00' : i} - ${endTime}`);
                //
                //     subSlots.push({ time: `${startHour}:${i === 0 ? '00' : i} - ${endTime}`, type: 'available' });
                // }
            } else {
                const weekDayName = this.getWeekDayName(`${day} ${this.year}`);

                const dayAvailability = this.doctorAvailability.find(day => day.weekday === weekDayName);

                if (!dayAvailability) {
                    return []; // No availability found for the selected day
                }

                // Convert the times to integers for easier comparison
                const availabilityStartHour = parseInt(dayAvailability.startTime.slice(0, 2));
                const availabilityEndHour = parseInt(dayAvailability.endTime.slice(0, 2));
                const breakStartHour = Math.floor(dayAvailability.breakStart / 100);
                const breakStartMinutes = dayAvailability.breakStart % 100;
                const breakEndHour = Math.floor(dayAvailability.breakEnd / 100);
                const breakEndMinutes = dayAvailability.breakEnd % 100;

                // If the start hour is not within doctor's availability, return an empty array
                if (startHour < availabilityStartHour || startHour >= availabilityEndHour) {
                    return [];
                }

                // Generate slots based on doctor's availability and slot duration
                let minutes = 0;
                while (minutes < 60) {
                    const endMinutes = (minutes + dayAvailability.slotDuration) % 60;
                    const nextHour = (endMinutes === 0) ? startHour + 1 : startHour;

                    const isWithinBreakTime = (
                        (startHour === breakStartHour && minutes >= breakStartMinutes) || // During break start hour
                        (startHour === breakEndHour && endMinutes <= breakEndMinutes) || // During break end hour
                        (startHour > breakStartHour && startHour < breakEndHour)          // In between break hours
                    );

                    // const endTime = `${nextHour}:${endMinutes === 0 ? '00' : endMinutes} ${ampm}`;
                    const endTime = `${nextHour}:${endMinutes === 0 ? '00' : endMinutes}`;
                    const slotTime = `${startHour}:${minutes === 0 ? '00' : minutes} - ${endTime}`;

                    if (isWithinBreakTime && !(startHour === breakEndHour && minutes >= breakEndMinutes)) {
                        subSlots.push({ time: slotTime, type: 'break' });
                    } else {
                        subSlots.push({ time: slotTime, type: 'available' });
                    }

                    minutes += dayAvailability.slotDuration;
                }

                // console.log(this.selectedDoctor.id, day, this.year);
                // console.log(this.getAppointments(this.selectedDoctor.id, day, this.year));
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

        getWeekDayName(dateString) {
            // Create a new Date object from the input date string
            const date = new Date(dateString);

            // Array of week day names
            const weekDays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

            // Get the day of the week as a number (0 - 6)
            const dayIndex = date.getDay();

            // Return the name of the day
            return weekDays[dayIndex].toUpperCase();
        },

        // Get all dates in the week of the selected date
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
            maxDate: new Date(new Date().setFullYear(new Date().getFullYear(), new Date().getMonth() + 12)), // 12 months from today

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
                const selectedDate = new Date(this.year, this.month, date);
                return selectedDate >= today && selectedDate <= this.maxDate; // Ensure date is between today and maxDate
            },

            getDateValue(date, $refs) {
                if (this.isFutureOrToday(date) || this.isToday(date)) {
                    let selectedDate = new Date(this.year, this.month, date);
                    this.datepickerValue = selectedDate.toDateString();

                    // Set the selected date in hidden input
                    $refs.date.value = selectedDate.getFullYear() + "-" +
                        ('0' + (selectedDate.getMonth() + 1)).slice(-2) + "-" + ('0' + selectedDate.getDate()).slice(-2);

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
            },

            // Disable going to previous months if it’s before today’s month
            prevMonth() {
                let today = new Date();
                if (this.year === today.getFullYear() && this.month === today.getMonth()) return;

                if (this.month === 0) {
                    this.month = 11;
                    this.year--;
                } else {
                    this.month--;
                }

                this.getNoOfDays();
            },

            // Disable going beyond 12 months from today
            nextMonth() {
                let maxMonth = new Date(this.maxDate).getMonth();
                let maxYear = new Date(this.maxDate).getFullYear();

                if (this.year === maxYear && this.month === maxMonth) return;

                if (this.month === 11) {
                    this.month = 0;
                    this.year++;
                } else {
                    this.month++;
                }

                this.getNoOfDays();
            },
        },

        init() {
            this.$watch('datepicker.datepickerValue', () => {
                this.days = this.getWeekDays(this.datepicker.datepickerValue);
                this.year = this.datepicker.datepickerValue.split(' ')[3];
            })
        }
    }));
});