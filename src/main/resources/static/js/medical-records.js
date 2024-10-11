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
            if (!timestamp || isNaN(new Date(timestamp))) {
                return 'Invalid Date'; // Fallback if the date is invalid or null
            }

            const date = new Date(timestamp);
            const day = ('0' + date.getDate()).slice(-2); // Get day and add leading zero if needed
            const month = ('0' + (date.getMonth() + 1)).slice(-2); // Get month and add leading zero
            const year = date.getFullYear(); // Get full year
            return `${day}/${month}/${year}`; // Return date as DD/MM/YYYY
        },

        toggleSection(section) {
            this[section + 'Open'] = !this[section + 'Open'];
        },

        // Sorting logic
        sortTable(key) {
            // Toggle the sort direction if the same key is clicked
            if (this.sortKey === key) {
                this.sortAsc = !this.sortAsc;
            } else {
                this.sortAsc = true;
                this.sortKey = key;
            }
        },

        // Filtered and sorted Physical Exams
        get filteredPhysicalExams() {
            let exams = this.physicalExamList.filter(exam => {
                const formattedExamDate = this.formatDateWithLeadingZeros(exam.examDate);
                const searchQuery = this.searchPhysicalExam.toLowerCase();

                return exam.veterinarian.toLowerCase().includes(searchQuery) ||
                    exam.notes.toLowerCase().includes(searchQuery) ||
                    formattedExamDate.includes(searchQuery);
            });

            // Apply sorting to the filtered list
            if (this.sortKey) {
                exams = [...exams].sort((a, b) => {
                    const direction = this.sortAsc ? 1 : -1;
                    switch (this.sortKey) {
                        case 'examDate':
                            return direction * (new Date(a.examDate) - new Date(b.examDate));
                        case 'veterinarian':
                            return direction * a.veterinarian.localeCompare(b.veterinarian);
                        case 'notes':
                            return direction * a.notes.localeCompare(b.notes);
                    }
                });
            }

            return exams.map(exam => ({
                ...exam,
                examDate: this.formatDateWithLeadingZeros(exam.examDate),
            }));
        },

        // Filtered and sorted Vaccinations
        get filteredVaccinations() {
            let vaccinations = this.vaccinationList.filter(vaccination => {
                const formattedVaccinationDate = this.formatDateWithLeadingZeros(vaccination.vaccinationDate);
                const formattedNextDueDate = this.formatDateWithLeadingZeros(vaccination.nextDueDate);
                const searchQuery = this.searchVaccination.toLowerCase();

                return vaccination.vaccineName.toLowerCase().includes(searchQuery) ||
                    vaccination.administeredBy.toLowerCase().includes(searchQuery) ||
                    formattedVaccinationDate.includes(searchQuery) ||
                    formattedNextDueDate.includes(searchQuery);
            });

            // Apply sorting to the filtered list
            if (this.sortKey) {
                vaccinations = [...vaccinations].sort((a, b) => {
                    const direction = this.sortAsc ? 1 : -1;
                    switch (this.sortKey) {
                        case 'vaccineName':
                            return direction * a.vaccineName.localeCompare(b.vaccineName);
                        case 'vaccinationDate':
                            return direction * (new Date(a.vaccinationDate) - new Date(b.vaccinationDate));
                        case 'administeredBy':
                            return direction * a.administeredBy.localeCompare(b.administeredBy);
                        case 'nextDueDate':
                            return direction * (new Date(a.nextDueDate) - new Date(b.nextDueDate));
                    }
                });
            }

            return vaccinations.map(vaccination => ({
                ...vaccination,
                vaccinationDate: this.formatDateWithLeadingZeros(vaccination.vaccinationDate),
                nextDueDate: this.formatDateWithLeadingZeros(vaccination.nextDueDate),
            }));
        },

        // Filtered and sorted Medical History
        get filteredMedicalHistory() {
            let history = this.medicalHistoryList.filter(history => {
                const formattedEventDate = this.formatDateWithLeadingZeros(history.eventDate);
                const searchQuery = this.searchMedicalHistory.toLowerCase();

                // Make sure to safely access veterinarian.fullName and practitioner
                const practitionerName = history.practitioner ? history.practitioner.toLowerCase() : '';
                const veterinarianName = history.veterinarian && history.veterinarian.fullName
                    ? history.veterinarian.fullName.toLowerCase()
                    : '';

                return history.treatment.toLowerCase().includes(searchQuery) ||
                    history.notes.toLowerCase().includes(searchQuery) ||
                    practitionerName.includes(searchQuery) ||  // Compare practitioner
                    veterinarianName.includes(searchQuery) ||  // Compare veterinarian.fullName
                    formattedEventDate.includes(searchQuery);  // Compare date
            });

            // Apply sorting to the filtered list
            if (this.sortKey) {
                history = [...history].sort((a, b) => {
                    const direction = this.sortAsc ? 1 : -1;
                    switch (this.sortKey) {
                        case 'practitioner':
                            return direction * (a.practitioner || '').localeCompare(b.practitioner || '');
                        case 'eventDate':
                            return direction * (new Date(a.eventDate) - new Date(b.eventDate));
                        case 'treatment':
                            return direction * a.treatment.localeCompare(b.treatment);
                        case 'medical_veterinarian':
                            return direction * (a.veterinarian?.fullName || '').localeCompare(b.veterinarian?.fullName || '');
                        case 'medical_notes':
                            return direction * a.notes.localeCompare(b.notes);
                    }
                });
            }

            return history.map(history => ({
                ...history,
                eventDate: this.formatDateWithLeadingZeros(history.eventDate),
            }));
        },

        // Filtered and sorted Treatment Plans
        get filteredTreatmentPlans() {
            let plans = this.treatmentPlanList.filter(plan => {
                const formattedPlanDate = this.formatDateWithLeadingZeros(plan.planDate);
                const searchQuery = this.searchTreatmentPlan.toLowerCase();

                // Ensure safe access to practitioner (handle cases where it's missing)
                const practitionerName = plan.practitioner ? plan.practitioner.toLowerCase() : '';

                // Perform case-insensitive filtering on description, notes, practitioner, and formatted date
                return plan.description.toLowerCase().includes(searchQuery) ||
                    plan.notes.toLowerCase().includes(searchQuery) ||
                    practitionerName.includes(searchQuery) ||  // Compare practitioner
                    formattedPlanDate.includes(searchQuery);   // Compare plan date
            });

            // Apply sorting to the filtered list
            if (this.sortKey) {
                plans = [...plans].sort((a, b) => {
                    const direction = this.sortAsc ? 1 : -1;
                    switch (this.sortKey) {
                        case 'planDate':
                            return direction * (new Date(a.planDate) - new Date(b.planDate));
                        case 'description':
                            return direction * a.description.localeCompare(b.description);
                        case 'plan_notes':
                            return direction * a.notes.localeCompare(b.notes);
                        case 'plan_practitioner':
                            // Ensure safe comparison of practitioner names (if available)
                            return direction * (a.practitioner || '').localeCompare(b.practitioner || '');
                    }
                });
            }

            return plans.map(plan => ({
                ...plan,
                planDate: this.formatDateWithLeadingZeros(plan.planDate),
            }));
        },

        formatDateWithLeadingZeros(date) {
            if (!date || isNaN(new Date(date))) {
                return 'Invalid Date'; // Handle invalid dates
            }

            const d = new Date(date);
            const day = String(d.getDate()).padStart(2, '0');
            const month = String(d.getMonth() + 1).padStart(2, '0');
            const year = d.getFullYear();

            return `${day}/${month}/${year}`;
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
            return this.sortKey === key ? (this.sortAsc ? 'fa-chevron-up' : 'fa-chevron-down') : 'fa-sort';
        },

        get formattedDateOfBirth() {
            if (!this.selectedPet || !this.selectedPet.dateOfBirth) return '';
            const date = new Date(this.selectedPet.dateOfBirth);
            return isNaN(date.getTime()) ? 'Invalid Date' : date.toLocaleDateString('en-US');
        },
    }));
});
