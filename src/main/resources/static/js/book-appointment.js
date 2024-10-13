const MONTH_NAMES = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September',
    'October', 'November', 'December'];
const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

document.addEventListener('alpine:init', () => {
    const user = JSON.parse(localStorage.getItem('loggedInUser'));

    if (!user) {
        Toastify({
            text: "Login to book appointment!",
            close: true,
            destination: "/login",
            gravity: 'top',
            position: 'right',
        }).showToast();

        setTimeout(() => {
            window.location.href = '/login';
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
        isUpdate: false,
        appointmentId: null,

        initPage() {
            const urlParams = new URLSearchParams(window.location.search);
            this.appointmentId = urlParams.get('appointmentId');

            // If appointmentId exists in the URL, mark the form for updating
            if (this.appointmentId) {
                this.isUpdate = true;
            }
        },

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
            if (!this.user) {
                Toastify({
                    text: "Login to book appointment!",
                    close: true,
                    destination: "/signin",
                    gravity: 'top',
                    position: 'right',
                }).showToast();

                setTimeout(() => {
                    window.location.href = '/login';
                }, 3000);
            } else {
                this.selectedSlot = slot;
                this.selectedDay = day;
                this.selectedPetId = this.user.pets[0] ? this.user.pets[0].id : false;
                this.showModal = true;
            }
        },

        bookAppointmentConfirm() {
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
            };

            if (this.isUpdate) {
                // Send update request if the appointmentId exists
                axios.put(`/api/appointments/edit/${this.appointmentId}`, null, {
                    params: appointmentData
                })
                    .then(response => {
                        alert('Appointment updated successfully!');
                        this.showModal = false;
                    })
                    .catch(error => {
                        console.error('Error updating appointment:', error.response);
                        alert('Failed to update appointment.');
                    });
            } else {
                // Create a new appointment
                axios.post('/api/appointments/create', null, {
                    params: appointmentData
                })
                    .then(response => {
                        alert('Appointment created successfully!');
                        this.showModal = false;
                    })
                    .catch(error => {
                        console.error('Error creating appointment:', error.response);
                        alert('Failed to create appointment.');
                    });
            }
        },

        getAppointments(veterinarianId, day, year) {
            axios.post(`/api/appointments/veterinarian/${veterinarianId}/day/${encodeURIComponent(day)}/year/${year}`)
                .then(response => {
                    return response.data;
                })
                .catch(error => {
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
                return [];
            } else {
                const weekDayName = this.getWeekDayName(`${day} ${this.year}`);
                const dayAvailability = this.doctorAvailability.find(day => day.weekday === weekDayName);

                if (!dayAvailability) {
                    return []; // No availability found for the selected day
                }

                const availabilityStartHour = parseInt(dayAvailability.startTime.slice(0, 2));
                const availabilityEndHour = parseInt(dayAvailability.endTime.slice(0, 2));
                const breakStartHour = Math.floor(dayAvailability.breakStart / 100);
                const breakStartMinutes = dayAvailability.breakStart % 100;
                const breakEndHour = Math.floor(dayAvailability.breakEnd / 100);
                const breakEndMinutes = dayAvailability.breakEnd % 100;

                if (startHour < availabilityStartHour || startHour >= availabilityEndHour) {
                    return [];
                }

                let minutes = 0;
                while (minutes < 60) {
                    const endMinutes = (minutes + dayAvailability.slotDuration) % 60;
                    const nextHour = (endMinutes === 0) ? startHour + 1 : startHour;
                    const isWithinBreakTime = (
                        (startHour === breakStartHour && minutes >= breakStartMinutes) ||
                        (startHour === breakEndHour && endMinutes <= breakEndMinutes) ||
                        (startHour > breakStartHour && startHour < breakEndHour)
                    );

                    const endTime = `${nextHour}:${endMinutes === 0 ? '00' : endMinutes}`;
                    const slotTime = `${startHour}:${minutes === 0 ? '00' : minutes} - ${endTime}`;

                    if (isWithinBreakTime && !(startHour === breakEndHour && minutes >= breakEndMinutes)) {
                        subSlots.push({ time: slotTime, type: 'break' });
                    } else {
                        subSlots.push({ time: slotTime, type: 'available' });
                    }

                    minutes += dayAvailability.slotDuration;
                }
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
            const date = new Date(dateString);
            const weekDays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
            return weekDays[date.getDay()].toUpperCase();
        },

        getWeekDays(dateStr) {
            const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
            const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

            const providedDate = new Date(dateStr);
            const dayOfWeek = providedDate.getDay();
            const startOfWeek = new Date(providedDate);
            startOfWeek.setDate(providedDate.getDate() - dayOfWeek);

            const weekDays = [];
            for (let i = 0; i < 7; i++) {
                const currentDay = new Date(startOfWeek);
                currentDay.setDate(startOfWeek.getDate() + i);

                const day = currentDay.getDate();
                const month = monthNames[currentDay.getMonth()];

                weekDays.push(`${day} ${month}`);
            }

            return weekDays;
        },

        datepicker: {
            showDatepicker: false,
            datepickerValue: '',
            month: '',
            year: '',
            no_of_days: [],
            blankdays: [],
            days: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
            today: new Date(),
            maxDate: new Date(new Date().setFullYear(new Date().getFullYear(), new Date().getMonth() + 12)),

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
                return selectedDate >= today && selectedDate <= this.maxDate;
            },

            getDateValue(date, $refs) {
                if (this.isFutureOrToday(date) || this.isToday(date)) {
                    let selectedDate = new Date(this.year, this.month, date);
                    this.datepickerValue = selectedDate.toDateString();
                    $refs.date.value = selectedDate.getFullYear() + "-" + ('0' + (selectedDate.getMonth() + 1)).slice(-2) + "-" + ('0' + selectedDate.getDate()).slice(-2);
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
            });
        }
    }));
});