document.addEventListener('alpine:init', () => {
    const veterinarian = JSON.parse(localStorage.getItem('veterinarian'));

    if (!veterinarian) {
        setTimeout(() => {
            window.location.href = '/veterinarian-login'
        }, 2000);
    }

    Alpine.data('veterinarianAppointments', () => ({
        veterinarian: veterinarian,
        veterinarian_appointments: null,

        fetchData() {
            const getAppointments = () => {
                return axios.post(`/api/veterinarian/${this.veterinarian.id}/appointments`)
                    .then(response => {
                        this.veterinarian_appointments = response.data.sort((a, b) =>
                            new Date(a.appointmentDate) - new Date(b.appointmentDate)
                        );
                    })
                    .catch(error => {
                        console.error('Error fetching veterinarian_appointments data:', error);
                    });
            };

            Promise.all([getAppointments()]).then(() => {

            }).finally(() => {

            });
        },

        uploadRecords(appointmentId) {
            window.location.href = `/veterinarian-upload-records?appointment-id=${appointmentId}`;
        },

        init() {}
    }));
});
