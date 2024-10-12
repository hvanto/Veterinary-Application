document.addEventListener('alpine:init', () => {
    /**
     * Initializes Alpine.js component for managing medical records.
     * @returns {Object} Component state and methods.
     */
    Alpine.data('medicalRecordsData', () => ({
        // Component state variables
        isAuthenticated: false, // User authentication status
        selectedPet: null, // Currently selected pet
        pets: [], // List of pets
        petCards: '', // UI component for displaying pet cards
        generalHealthOpen: true, // Flag for general health section visibility
        medicalHistoryOpen: true, // Flag for medical history section visibility
        weightRecords: [], // Records of pet weights
        physicalExamList: [], // List of physical exams
        vaccinationList: [], // List of vaccinations
        medicalHistoryList: [], // List of medical history entries
        treatmentPlanList: [], // List of treatment plans
        searchPhysicalExam: '', // Search query for physical exams
        searchVaccination: '', // Search query for vaccinations
        searchMedicalHistory: '', // Search query for medical history
        searchTreatmentPlan: '', // Search query for treatment plans
        sortKey: '', // Currently active sort key for sorting
        sortAsc: true, // Flag indicating sort direction (ascending or descending)
        weightRecordsSelected: false, // Flag for weight records section
        physicalExamsSelected: false, // Flag for physical exams section
        vaccinationsSelected: false, // Flag for vaccinations section
        medicalHistorySelected: true, // Flag for medical history section (preselected)
        treatmentPlansSelected: false, // Flag for treatment plans section

        /**
         * Lifecycle method to fetch initial data.
         */
        init() {
            this.fetchInitialData();
        },

        /**
         * Fetches user pets from localStorage and API.
         * @returns {void}
         */
        fetchInitialData() {
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            if (!user || !user.id) {
                this.isAuthenticated = false;
                console.error('User not logged in or user ID is missing.');
                return;
            }

            this.isAuthenticated = true;

            // Fetch pets for the authenticated user
            axios.get(`/api/medical-records/user-pets?userId=${user.id}`)
                .then(response => {
                    console.log('Pets fetched (raw):', response.data); // Debug log
                    this.pets = response.data.map(pet => Object.assign({}, pet)); // Process pets
                    this.selectedPet = null; // Reset selected pet
                    console.log('Pets processed:', this.pets); // Debug log
                })
                .catch(error => {
                    console.error('Error fetching user pets:', error);
                });
        },

        /**
         * Fetches medical records for the selected pet.
         * @param {string} selectedPetId - The ID of the selected pet.
         * @returns {void}
         */
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
                    this.drawWeightChart(); // Draw chart with fetched weight records
                })
                .catch(error => {
                    console.error('Error fetching medical records:', error);
                });
        },

        /**
         * Draws the weight chart using Chart.js.
         * @returns {void}
         */
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

        /**
         * Formats a timestamp into a readable date string.
         * @param {number|string} timestamp - The timestamp to format.
         * @returns {string} Formatted date as DD/MM/YYYY or 'Invalid Date'.
         */
        formatDate(timestamp) {
            if (!timestamp || isNaN(new Date(timestamp))) {
                return 'Invalid Date'; // Fallback for invalid date
            }

            const date = new Date(timestamp);
            const day = ('0' + date.getDate()).slice(-2); // Add leading zero for day
            const month = ('0' + (date.getMonth() + 1)).slice(-2); // Add leading zero for month
            const year = date.getFullYear(); // Full year
            return `${day}/${month}/${year}`; // Return formatted date as DD/MM/YYYY
        },

        /**
         * Toggles visibility of the specified section.
         * @param {string} section - The section to toggle.
         * @returns {void}
         */
        toggleSection(section) {
            this[section + 'Open'] = !this[section + 'Open'];
        },

        /**
         * Sorts the table based on the given key.
         * @param {string} key - The key to sort by.
         * @returns {void}
         */
        sortTable(key) {
            // Toggle sort direction if the same key is clicked
            if (this.sortKey === key) {
                this.sortAsc = !this.sortAsc;
            } else {
                this.sortAsc = true; // Reset sort direction to ascending
                this.sortKey = key; // Set new sort key
            }
        },

        /**
         * Gets filtered and sorted Physical Exams.
         * @returns {Array} List of filtered and sorted physical exams.
         */
        get filteredPhysicalExams() {
            let exams = this.physicalExamList.filter(exam => {
                const formattedExamDate = this.formatDateWithLeadingZeros(exam.examDate);
                const searchQuery = this.searchPhysicalExam.toLowerCase();

                // Filter exams based on search criteria
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

        /**
         * Gets filtered and sorted Vaccinations.
         * @returns {Array} List of filtered and sorted vaccinations.
         */
        get filteredVaccinations() {
            let vaccinations = this.vaccinationList.filter(vaccination => {
                const formattedVaccinationDate = this.formatDateWithLeadingZeros(vaccination.vaccinationDate);
                const formattedNextDueDate = this.formatDateWithLeadingZeros(vaccination.nextDueDate);
                const searchQuery = this.searchVaccination.toLowerCase();

                // Filter vaccinations based on search criteria
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

        /**
         * Gets filtered and sorted Medical History.
         * @returns {Array} List of filtered and sorted medical history entries.
         */
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

        /**
         * Gets filtered and sorted Treatment Plans.
         * @returns {Array} List of filtered and sorted treatment plans.
         */
        get filteredTreatmentPlans() {
            let plans = this.treatmentPlanList.filter(plan => {
                const formattedPlanDate = this.formatDateWithLeadingZeros(plan.planDate);
                const searchQuery = this.searchTreatmentPlan.toLowerCase();

                // Safely access practitioner (handle cases where it's missing)
                const practitionerName = plan.practitioner ? plan.practitioner.toLowerCase() : '';

                // Filter treatment plans based on search criteria
                return plan.description.toLowerCase().includes(searchQuery) ||
                    plan.notes.toLowerCase().includes(searchQuery) ||
                    practitionerName.includes(searchQuery) ||
                    formattedPlanDate.includes(searchQuery);
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
                            return direction * (a.practitioner || '').localeCompare(b.practitioner || '');
                    }
                });
            }

            return plans.map(plan => ({
                ...plan,
                planDate: this.formatDateWithLeadingZeros(plan.planDate),
            }));
        },

        /**
         * Formats a date with leading zeros.
         * @param {number|string} date - The date to format.
         * @returns {string} Formatted date as DD/MM/YYYY or 'Invalid Date'.
         */
        formatDateWithLeadingZeros(date) {
            if (!date || isNaN(new Date(date))) {
                return 'Invalid Date'; // Handle invalid dates
            }

            const d = new Date(date);
            const day = String(d.getDate()).padStart(2, '0'); // Add leading zero for day
            const month = String(d.getMonth() + 1).padStart(2, '0'); // Add leading zero for month
            const year = d.getFullYear(); // Full year

            return `${day}/${month}/${year}`; // Return formatted date as DD/MM/YYYY
        },

        /**
         * Handles the download of medical records.
         * @returns {void}
         */
        handleDownload() {
            document.getElementById('recordActionForm').action = "/api/medical-records/download";
            document.getElementById('recordActionForm').submit();
        },

        /**
         * Opens the share modal for sharing medical records.
         * @returns {void}
         */
        openShareModal() {
            document.getElementById('shareModal').classList.remove('hidden');
        },

        /**
         * Closes the share modal.
         * @returns {void}
         */
        closeShareModal() {
            document.getElementById('shareModal').classList.add('hidden');
        },

        /**
         * Shares medical records via email.
         * @returns {void}
         */
        handleShare() {
            const payload = {
                email: this.email, // Email address to send the PDF
                selectedPetId: this.selectedPet.id, // ID of the selected pet
                sections: {
                    weightRecords: this.sections.weightRecords,
                    physicalExams: this.sections.physicalExams,
                    vaccinations: this.sections.vaccinations,
                    full: this.sections.full,
                    treatmentPlans: this.sections.treatmentPlans
                }
            };

            axios.post('/api/medical-records/share', payload)
                .then(response => {
                    alert('Medical records shared successfully!');
                    this.closeShareModal();
                })
                .catch(error => {
                    console.error('Error sharing medical records:', error);
                    alert('Failed to share medical records.');
                });
        },

        /**
         * Gets the sort icon based on the current sort state.
         * @param {string} key - The key for which to get the sort icon.
         * @returns {string} FontAwesome class for the sort icon.
         */
        getSortIcon(key) {
            return this.sortKey === key ? (this.sortAsc ? 'fa-chevron-up' : 'fa-chevron-down') : 'fa-sort';
        },

        /**
         * Gets the formatted date of birth for the selected pet.
         * @returns {string} Formatted date of birth or an empty string.
         */
        get formattedDateOfBirth() {
            if (!this.selectedPet || !this.selectedPet.dateOfBirth) return '';
            const date = new Date(this.selectedPet.dateOfBirth);
            return isNaN(date.getTime()) ? 'Invalid Date' : date.toLocaleDateString('en-US');
        },
    }));
});
