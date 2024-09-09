-- Run this in MySQL Workbench manually
USE vetcaredb;

CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title TEXT NOT NULL,
    link TEXT NOT NULL,
    description TEXT NOT NULL,
    author TEXT NOT NULL,
    published_date DATE NOT NULL,
    image_url TEXT NOT NULL
);

INSERT INTO
    articles (
        title,
        link,
        description,
        author,
        published_date,
        image_url
    )
VALUES
    (
        '8 Best Dry Dog Foods in 2024, Recommended by Vets',
        'https://www.petmd.com/dog/vet-verified/best-dry-dog-food',
        'What To Look For When Buying Dry Dog Food When shopping for the best dry food for dogs, our vets recommend products that meet the standards set by the Association of American Feed Control Officials (AAFCO), a private, non-profit organization that establishes standard ingredient definitions and nutritional requirements for pet food. A complete-and-balanced diet will include the following:...',
        'PetMD Vet Advisory Panel',
        '2024-09-06',
        'https://image.petmd.com/files/styles/863x625/public/2024-09/best-dry-dog-food-vets.jpg'
    ),
    (
        'Do Cats Dream?',
        'https://www.petmd.com/cat/general-health/do-cats-dream',
        'Have you ever watched your feline friend twitch and move in their sleep and wonder whether they’re dreaming about “the big hunt” or that delicious dinner they had? But do cats dream? Can cats dream? The simple answer is that yes, like most other mammals, cats do dream, as it’s an important phenomenon that helps the brain to process the activities of the day. Do Cats Dream? Dreaming occurs...',
        'Sandra C. Mitchell, DVM, DABVP',
        '2024-09-04',
        'https://image.petmd.com/files/styles/863x625/public/2024-09/kitten%20sleeping%20on%20bed.jpeg'
    ),
    (
        'Why Do Dogs Bury Bones?',
        'https://www.petmd.com/dog/behavior/why-do-dogs-bury-bones',
        'Does your dog like burying bones, toys or other items? That’s completely normal. Digging is a pastime that many dogs enjoy, and burying “treasure” can make things even more fun. But why do dogs bury bones—and what, if anything, should you do about it? 5 Reasons Why Dogs Bury Bones 1. Instinct Your dog’s wild ancestors had to rely on instincts like digging to survive, explains...',
        'Monica Weymouth',
        '2024-09-04',
        'https://image.petmd.com/files/styles/863x625/public/2024-09/why-do-dogs-bury-bones_0.jpg'
    );