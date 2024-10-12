document.addEventListener('alpine:init', () => {
    const user = JSON.parse(localStorage.getItem('loggedInUser'));

    if (!user) {
        // Toastify({
        //     text: "Login to book appointment!",
        //     close: true,
        //     destination: "/signin",
        //     gravity: 'top',
        //     position: 'right',
        // }).showToast();

        // setTimeout(() => {
            window.location.href = '/login'
        // }, 2000);
    }

    Alpine.data('appointmentsData', () => ({
        user: user,
        selectedAppointmentSection: 'upcoming',
        loading: false,

        // fetchData() {
        //     const getClinic = () => {
        //         return axios.post('/api/clinic/1')
        //             .then(response => {
        //                 this.clinic = response.data;
        //             })
        //             .catch(error => {
        //                 console.error('Error fetching clinic data:', error);
        //             });
        //     };
        //
        //     Promise.all([getClinic()])
        //         .then(() => {})
        //         .finally(() => {
        //         this.loading = false;
        //     });
        //
        //     this.loading = false;
        // },

        init() {
            this.$watch('selectedAppointmentSection', () => {
                console.log(this.selectedAppointmentSection);
            })
        }
    }));
});