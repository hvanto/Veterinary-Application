document.addEventListener('alpine:init', () => {
    Alpine.data('medicalRecordsData', () => ({
        isAuthenticated: false,
        selectedPet: null,
        pets: [],
        petCards: '',
        generalHealthOpen: true,
        medicalHistoryOpen: true,
        weightRecords: [],
        physicalExamList: [],
        vaccinationList: [],
        medicalHistoryList: [],
        treatmentPlanList: [],
        searchPhysicalExam: '',
        searchVaccination: '',
        searchMedicalHistory: '',
        searchTreatmentPlan: '',
        sortKey: '', // to track the current sort key
        sortAsc: true, // to track the current sort direction

        init() {
            this.fetchInitialData();
        },

        fetchInitialData() {
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            if (!user || !user.id) {
                this.isAuthenticated = false;
                console.error('User not logged in or user ID is missing.');
                return;
            }

            this.isAuthenticated = true;

            axios.get(`/api/medical-records/user-pets?userId=${user.id}`)
                .then(response => {
                    console.log('Pets fetched (raw):', response.data);  // Debug log
                    this.pets = response.data.map(pet => Object.assign({}, pet)); // If needed, unwrap Proxy
                    this.selectedPet = null;
                    console.log('Pets processed:', this.pets);  // Debug log
                })
                .catch(error => {
                    console.error('Error fetching user pets:', error);
                });
        },

        fetchMedicalRecords(selectedPetId) {
            axios.get(`/api/medical-records/${selectedPetId}`)
                .then(response => {
                    const data = response.data;
                    this.selectedPet = data.selectedPet;
                    this.weightRecords = data.weightRecords;
                    this.physicalExamList = data.physicalExamList;
                    this.vaccinationList = data.vaccinationList;
                    this.medicalHistoryList = data.medicalHistoryList;
                    this.treatmentPlanList = data.treatmentPlanList;
                    this.drawWeightChart();
                })
                .catch(error => {
                    console.error('Error fetching medical records:', error);
                });
        },

        drawWeightChart() {
            const ctx = document.getElementById('weightChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: this.weightRecords.map(record => this.formatDate(record.recordDate)),
                    datasets: [{
                        label: 'Weight Over Time',
                        data: this.weightRecords.map(record => record.weight),
                        borderColor: '#4A90E2',
                        backgroundColor: 'rgba(74, 144, 226, 0.2)',
                        borderWidth: 2,
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: 'Date'
                            }
                        },
                        y: {
                            beginAtZero: false,
                            title: {
                                display: true,
                                text: 'Weight (kg)'
                            }
                        }
                    }
                }
            });
        },

        formatDate(timestamp) {
            // Convert the timestamp to a date object
            const date = new Date(Number(timestamp));
            // Format the date as desired (e.g., 'MM/DD/YYYY')
            return date.toLocaleDateString('en-US');
        },

        toggleSection(section) {
            this[section + 'Open'] = !this[section + 'Open'];
        },

        sortTable(key) {
            this.sortKey = key;
            this.sortAsc = this.sortKey === key ? !this.sortAsc : true;

            let sortedList = [];

            switch (key) {
                case 'examDate':
                    sortedList = this.physicalExamList.sort((a, b) => this.sortAsc
                        ? new Date(a.examDate) - new Date(b.examDate)
                        : new Date(b.examDate) - new Date(a.examDate));
                    this.physicalExamList = sortedList;
                    break;
                case 'veterinarian':
                    sortedList = this.physicalExamList.sort((a, b) => this.sortAsc
                        ? a.veterinarian.localeCompare(b.veterinarian)
                        : b.veterinarian.localeCompare(a.veterinarian));
                    this.physicalExamList = sortedList;
                    break;
                case 'notes':
                    sortedList = this.physicalExamList.sort((a, b) => this.sortAsc
                        ? a.notes.localeCompare(b.notes)
                        : b.notes.localeCompare(a.notes));
                    this.physicalExamList = sortedList;
                    break;
                // Add more cases for different tables and keys as needed
            }
        },

        get filteredPhysicalExams() {
            return this.physicalExamList.filter(exam =>
                exam.veterinarian.toLowerCase().includes(this.searchPhysicalExam.toLowerCase()) ||
                exam.notes.toLowerCase().includes(this.searchPhysicalExam.toLowerCase())
            ).map(exam => {
                // Format the examDate before returning
                return {
                    ...exam,
                    examDate: this.formatDate(exam.examDate)
                };
            });
        },

        get filteredVaccinations() {
            return this.vaccinationList.filter(vaccination =>
                vaccination.vaccineName.toLowerCase().includes(this.searchVaccination.toLowerCase()) ||
                vaccination.administeredBy.toLowerCase().includes(this.searchVaccination.toLowerCase())
            ).map(vaccination => {
                // Format the vaccinationDate and nextDueDate before returning
                return {
                    ...vaccination,
                    vaccinationDate: this.formatDate(vaccination.vaccinationDate),
                    nextDueDate: this.formatDate(vaccination.nextDueDate)
                };
            });
        },

        get filteredMedicalHistory() {
            return this.medicalHistoryList.filter(history =>
                history.treatment.toLowerCase().includes(this.searchMedicalHistory.toLowerCase()) ||
                history.notes.toLowerCase().includes(this.searchMedicalHistory.toLowerCase())
            ).map(history => {
                // Format the eventDate before returning
                return {
                    ...history,
                    eventDate: this.formatDate(history.eventDate)
                };
            });
        },

        get filteredTreatmentPlans() {
            return this.treatmentPlanList.filter(plan =>
                plan.description.toLowerCase().includes(this.searchTreatmentPlan.toLowerCase()) ||
                plan.notes.toLowerCase().includes(this.searchTreatmentPlan.toLowerCase())
            ).map(plan => {
                // Format the planDate before returning
                return {
                    ...plan,
                    planDate: this.formatDate(plan.planDate)
                };
            });
        },

        handleDownload() {
            document.getElementById('recordActionForm').action = "/api/medical-records/download";
            document.getElementById('recordActionForm').submit();
        },

        openShareModal() {
            document.getElementById('shareModal').classList.remove('hidden');
        },

        closeShareModal() {
            document.getElementById('shareModal').classList.add('hidden');
        },

        getSortIcon(key) {
            if (this.sortKey === key) {
                return this.sortAsc ? 'fa-chevron-up' : 'fa-chevron-down';
            }
            return 'fa-sort';
        },

        get formattedDateOfBirth() {
            if (!this.selectedPet || !this.selectedPet.dateOfBirth) return '';

            // Assuming the date is a Unix timestamp in milliseconds
            const date = new Date(this.selectedPet.dateOfBirth);

            // If the timestamp is in seconds, multiply by 1000
            // const date = new Date(this.selectedPet.dateOfBirth * 1000);

            // Check if the date is valid
            if (isNaN(date.getTime())) {
                return 'Invalid Date';
            }

            return date.toLocaleDateString('en-US'); // Format date as 'MM/DD/YYYY'
        }
    }));
});
