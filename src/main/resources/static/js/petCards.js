document.addEventListener('alpine:init', () => {
    Alpine.data('petCardsData', () => ({
        pets: [],

        init() {
            this.fetchUserPets();
        },

        fetchUserPets() {
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            if (!user || !user.id) {
                console.error('User not logged in or user ID is missing.');
                return;
            }

            axios.get(`/api/medical-records/user-pets?userId=${user.id}`)
                .then(response => {
                    this.pets = response.data;
                    this.renderPetCards();
                })
                .catch(error => {
                    console.error('Error fetching user pets:', error);
                });
        },

        renderPetCards() {
            const container = document.getElementById('pet-cards-container');
            container.innerHTML = this.pets.map(pet => `
                <div class="bg-white shadow-lg rounded-lg overflow-hidden border border-gray-300 max-w-xs w-full hover:shadow-2xl hover:border-blue-500 transition-all transform hover:scale-105 cursor-pointer">
                    <a th:href="@{/medical-records(selectedPetId=${pet.id})}" class="block">
                        <!-- Pet Name -->
                        <div class="bg-blue-100 p-4">
                            <h3 class="text-xl font-semibold text-gray-800">${pet.name}</h3>
                        </div>

                        <!-- Image Container -->
                        <div class="w-full h-40 flex justify-center items-center bg-gray-200">
                            <img src="${pet.imagePath ? '/assets/' + pet.imagePath : '/assets/default.png'}"
                                 alt="Pet Image" class="object-contain h-full w-full">
                        </div>

                        <!-- Pet Info -->
                        <div class="p-4 text-center">
                            <p class="text-gray-600">${pet.species}</p>
                            <p class="text-gray-600">Breed: <span>${pet.breed}</span></p>
                        </div>
                    </a>
                </div>
            `).join('');
        }
    }));
});
