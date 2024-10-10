document.addEventListener('alpine:init', () => {
    Alpine.data('prescriptionsData', () => ({
        isAuthenticated: false,
        selectedPet: null,
        pets: [],
        petCards: '',
        prescriptionOpen: true,
        prescriptionHistoryOpen: true,
        prescriptionsCurrent: [],
        prescriptionsHistory: [],
        searchPrescriptions: '',
        searchPrescriptionHistory: '',
        currentSortKey: '',
        currentSortAsc: true,
        historySortKey: '',
        historySortAsc: true,
        showAddPrescriptionModal: false,
        showEditPrescriptionModal: false,
        showDeleteConfirmationModal: false, // State for delete confirmation modal
        prescriptionToDeleteId: null, // ID of the prescription to delete
        newPrescription: {},
        editPrescription: {},


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

        fetchCurrentPrescriptions() {
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            if (!user || !user.id || !this.selectedPet) {
                console.error('User not logged in, user ID is missing, or no pet selected.');
                return;
            }

            axios.get(`/api/prescriptions/all`, {
                params: {
                    petId: this.selectedPet.id
                }
            })
                .then(response => {
                    this.prescriptionsCurrent = response.data; // Assuming the API returns a list of current prescriptions
                })
                .catch(error => {
                    console.error('Error fetching current prescriptions:', error);
                });
        },

        fetchPrescriptionHistory() {
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            if (!user || !user.id || !this.selectedPet) {
                console.error('User not logged in, user ID is missing, or no pet selected.');
                return;
            }

            axios.get(`/api/prescriptions/history/all`, {
                params: {
                    petId: this.selectedPet.id
                }
            })
                .then(response => {
                    this.prescriptionsHistory = response.data; // Assuming the API returns a list of prescription history
                })
                .catch(error => {
                    console.error('Error fetching prescription history:', error);
                });
        },

        fetchPetDetails(selectedPetId) {
            axios.get(`/api/medical-records/${selectedPetId}`)
                .then(response => {
                    const data = response.data;
                    this.selectedPet = data.selectedPet;
                    this.fetchCurrentPrescriptions(); // Fetch current prescriptions
                    this.fetchPrescriptionHistory();   // Fetch prescription history
                })
                .catch(error => {
                    console.error('Error', error);
                });
        },

        formatDate(date) {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
            return new Date(date).toLocaleDateString('en-GB', options); // Specifying 'en-GB' for DD/MM/YYYY
        },

        toggleSection(section) {
            if (section === 'prescriptionsCurrent') {
                this.prescriptionOpen = !this.prescriptionOpen;
            } else if (section === 'prescriptionsHistory') {
                this.prescriptionHistoryOpen = !this.prescriptionHistoryOpen;
            }
        },

        // Computed property for filtered current prescriptions
        get filteredCurrentPrescriptions() {
            return this.prescriptionsCurrent.filter(prescription => {
                return prescription.practitioner.toLowerCase().includes(this.searchPrescriptions.toLowerCase()) ||
                    prescription.prescription.toLowerCase().includes(this.searchPrescriptions.toLowerCase()) ||
                    prescription.vet.toLowerCase().includes(this.searchPrescriptions.toLowerCase()) ||
                    prescription.dosage.toLowerCase().includes(this.searchPrescriptions.toLowerCase());
            }).map(prescription => {
                // Optionally format dates or modify prescription properties before returning
                return {
                    ...prescription,
                    startDate: this.formatDate(prescription.startDate),
                    endDate: this.formatDate(prescription.endDate),
                };
            });
        },

        // Computed property for filtered prescription history
        get filteredPrescriptionHistory() {
            return this.prescriptionsHistory.filter(prescription => {
                return prescription.practitioner.toLowerCase().includes(this.searchPrescriptionHistory.toLowerCase()) ||
                    prescription.prescription.toLowerCase().includes(this.searchPrescriptionHistory.toLowerCase()) ||
                    prescription.vet.toLowerCase().includes(this.searchPrescriptionHistory.toLowerCase()) ||
                    prescription.dosage.toLowerCase().includes(this.searchPrescriptionHistory.toLowerCase());
            }).map(prescription => {
                // Optionally format dates or modify prescription properties before returning
                return {
                    ...prescription,
                    startDate: this.formatDate(prescription.startDate),
                    endDate: this.formatDate(prescription.endDate),
                };
            });
        },

        // Sort for Current Prescriptions
        sortCurrentTable(key) {
            if (this.currentSortKey === key) {
                // Toggle sort order if the same key is clicked
                this.currentSortAsc = !this.currentSortAsc;
            } else {
                // Set the new key and default to ascending
                this.currentSortKey = key;
                this.currentSortAsc = true;
            }

            const sortFunction = (a, b) => {
                let comparison = 0;
                // Compare based on the sort key
                if (a[key] < b[key]) {
                    comparison = -1;
                } else if (a[key] > b[key]) {
                    comparison = 1;
                }
                return this.currentSortAsc ? comparison : -comparison;
            };

            // Sort the current prescriptions table
            this.prescriptionsCurrent.sort(sortFunction);
        },

        // Sort for Prescription History
        sortHistoryTable(key) {
            if (this.historySortKey === key) {
                // Toggle sort order if the same key is clicked
                this.historySortAsc = !this.historySortAsc;
            } else {
                // Set the new key and default to ascending
                this.historySortKey = key;
                this.historySortAsc = true;
            }

            const sortFunction = (a, b) => {
                let comparison = 0;
                // Compare based on the sort key
                if (a[key] < b[key]) {
                    comparison = -1;
                } else if (a[key] > b[key]) {
                    comparison = 1;
                }
                return this.historySortAsc ? comparison : -comparison;
            };

            // Sort the prescription history table
            this.prescriptionsHistory.sort(sortFunction);
        },


        getSortIcon(key) {
            if (this.sortKey === key) {
                return this.sortAsc ? 'fa-chevron-up' : 'fa-chevron-down';
            }
            return 'fa-sort';
        },

        get formattedDateOfBirth() {
            if (!this.selectedPet || !this.selectedPet.dateOfBirth) return '';
            const date = new Date(this.selectedPet.dateOfBirth);
            if (isNaN(date.getTime())) {
                return 'Invalid Date';
            }
            return date.toLocaleDateString('en-GB'); // Format date as 'MM/DD/YYYY'
        },
        openAddPrescriptionModal() {
            this.showAddPrescriptionModal = true; // Show the modal
            this.newPrescription = { // Reset the new prescription form
                practitioner: '',
                prescription: '',
                vet: '',
                dosage: '',
                startDate: '',
                endDate: '',
                description: '',
                orderTracking: ''
            };
        },

        closeAddPrescriptionModal() {
            this.showAddPrescriptionModal = false; // Hide the modal
        },

        addPrescription() {
            if (this.newPrescription.startDate > this.newPrescription.endDate) {
                alert('End date must be after the start date.');
                return;
            }
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            if (!user || !user.id || !this.selectedPet) {
                console.error('User not logged in, user ID is missing, or no pet selected.');
                return;
            }

            // Construct the prescription data to send
            const prescriptionData = {
                ...this.newPrescription,
                petId: this.selectedPet.id, // Assuming the selected pet ID is available
                userId: user.id // Add user ID if needed
            };

            // Send POST request to add the prescription
            axios.post('/api/prescriptions/add', prescriptionData)
                .then(response => {
                    console.log('Prescription added:', response.data);
                    this.fetchCurrentPrescriptions(); // Refresh current prescriptions
                    this.closeAddPrescriptionModal(); // Close the modal
                })
                .catch(error => {
                    console.error('Error adding prescription:', error);
                });
        },
        // Method to open the edit prescription modal
        openEditPrescriptionModal(prescription) {
            this.editPrescription = { ...prescription }; // Pre-fill the edit form
            this.editPrescription.startDate = this.formatDateForInput(new Date(prescription.startDate)); // Format start date
            this.editPrescription.endDate = this.formatDateForInput(new Date(prescription.endDate)); // Format end date
            this.showEditPrescriptionModal = true; // Show the edit modal
        },

        // Utility method to format date for input[type="date"]
        formatDateForInput(date) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`; // Return date in YYYY-MM-DD format
        },

        // Utility method to format date for display (DD-MM-YYYY)
        formatDateForDisplay(date) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
            const day = String(date.getDate()).padStart(2, '0');
            return `${day}-${month}-${year}`; // Return date in DD-MM-YYYY format for display purposes
        },

        closeEditPrescriptionModal() {
            this.showEditPrescriptionModal = false; // Hide the edit modal
        },

        async savePrescription() {
            try {
                // Make an API call to update the prescription
                await axios.put(`/api/prescriptions/${this.editPrescription.id}`, this.editPrescription);

                // Fetch the updated prescriptions to refresh the table
                await this.fetchCurrentPrescriptions();

                // Close the edit modal
                this.closeEditPrescriptionModal();
            } catch (error) {
                console.error('Error updating prescription:', error);
                // Handle error (e.g., show a notification)
            }
        },

        // Open the delete confirmation modal
        openDeleteConfirmationModal(prescriptionId) {
            this.prescriptionToDeleteId = prescriptionId; // Store the ID of the prescription to delete
            this.showDeleteConfirmationModal = true; // Show the modal
        },

        // Close the delete confirmation modal
        closeDeleteConfirmationModal() {
            this.showDeleteConfirmationModal = false; // Hide the modal
            this.prescriptionToDeleteId = null; // Clear the ID
        },

        // Method to delete a prescription
        deletePrescription() {
            if (this.prescriptionToDeleteId) {
                axios.delete(`/api/prescriptions/${this.prescriptionToDeleteId}`)
                    .then(response => {
                        console.log('Prescription deleted:', response.data);
                        this.fetchCurrentPrescriptions(); // Refresh the list after deletion
                        this.closeDeleteConfirmationModal(); // Close the modal
                    })
                    .catch(error => {
                        console.error('Error deleting prescription:', error);
                    });
            }
        }
    }));
});
