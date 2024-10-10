-- Run this in MySQL Workbench manually
USE vetcaredb;

-- Insert General FAQs
INSERT INTO faq (question, answer, category) VALUES
                                                 ('What is the purpose of the FAQ section?',
                                                  'The FAQ section provides answers to common questions about our services and helps users find information quickly without needing to contact support.',
                                                  'General'),
                                                 ('How can I contact support?',
                                                  'You can reach our support team by emailing support@example.com or by calling (123) 456-7890. We are available from 9 AM to 5 PM.',
                                                  'General');


-- Insert Medical Records FAQs
INSERT INTO faq (question, answer, category) VALUES
                                                ('Where can I find my pet’s medical records?',
                                                'You can access your pet\'s medical records through your account dashboard under the "Medical Records" section.',
                                                'medical-records'),
                                                 ('What should I do if I need to update my pet’s medical records?',
                                                  'To update your pet’s medical records, please contact your veterinarian or our support team for assistance.',
                                                  'medical-records');

