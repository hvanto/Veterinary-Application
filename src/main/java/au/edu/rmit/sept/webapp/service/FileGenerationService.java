package au.edu.rmit.sept.webapp.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import au.edu.rmit.sept.webapp.model.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import com.lowagie.text.Image;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import java.io.InputStream;

@Service
public class FileGenerationService {

    public ByteArrayInputStream generatePDF(Pet pet, List<MedicalHistory> medicalHistory,
                                            List<PhysicalExam> physicalExams, List<Vaccination> vaccinations,
                                            List<TreatmentPlan> treatmentPlans, List<WeightRecord> weightRecords,
                                            List<String> sections) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add Logo
            InputStream logoStream = getClass().getResourceAsStream("/static/assets/logo.png");
            if (logoStream != null) {
                Image logo = Image.getInstance(logoStream.readAllBytes());
                logo.scaleToFit(70, 70);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            }

            // Add Title
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(30, 96, 191));
            Paragraph title = new Paragraph("Medical Records for: " + pet.getName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add Date of Record Generation
            Font dateFont = new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY);
            Paragraph date = new Paragraph("Records as of: " + new java.util.Date(), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);

            document.add(new Paragraph(" "));

            // Weight Records Section
            if (sections.contains("weightRecords")) {
                document.add(new Paragraph("Weight Records", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(30, 96, 191))));
                if (weightRecords != null && !weightRecords.isEmpty()) {
                    document.add(createWeightRecordsTable(weightRecords));
                } else {
                    document.add(new Paragraph("No weight records available for this pet.", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.GRAY)));
                }
            }

            // Full Medical History Section
            if (sections.contains("full")) {
                document.add(new Paragraph("Full Medical History", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(30, 96, 191))));
                if (medicalHistory != null && !medicalHistory.isEmpty()) {
                    document.add(createMedicalHistoryTable(medicalHistory));
                } else {
                    document.add(new Paragraph("No medical history available for this pet.", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.GRAY)));
                }
            }

            // Physical Exams Section
            if (sections.contains("physicalExams")) {
                document.add(new Paragraph("Physical Exams", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(30, 96, 191))));
                if (physicalExams != null && !physicalExams.isEmpty()) {
                    document.add(createPhysicalExamsTable(physicalExams));
                } else {
                    document.add(new Paragraph("No physical exam history available for this pet.", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.GRAY)));
                }
            }

            // Vaccination Records Section
            if (sections.contains("vaccinations")) {
                document.add(new Paragraph("Vaccination Records", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(30, 96, 191))));
                if (vaccinations != null && !vaccinations.isEmpty()) {
                    document.add(createVaccinationsTable(vaccinations));
                } else {
                    document.add(new Paragraph("No vaccination history available for this pet.", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.GRAY)));
                }
            }

            // Treatment Plans Section
            if (sections.contains("treatmentPlans")) {
                document.add(new Paragraph("Treatment Plans", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(30, 96, 191))));
                if (treatmentPlans != null && !treatmentPlans.isEmpty()) {
                    document.add(createTreatmentPlansTable(treatmentPlans));
                } else {
                    document.add(new Paragraph("No treatment plans available for this pet.", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.GRAY)));
                }
            }

            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPTable createMedicalHistoryTable(List<MedicalHistory> medicalHistory) {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table Headers
        addTableHeader(table, "Date", "Treatment", "Practitioner", "Veterinarian", "Notes");

        // Table Rows
        for (MedicalHistory history : medicalHistory) {
            table.addCell(createTableCell(history.getEventDate().toString()));
            table.addCell(createTableCell(history.getTreatment()));
            table.addCell(createTableCell(history.getPractitioner()));

            // Get the full name of the veterinarian
            Veterinarian vet = history.getVeterinarian();
            String vetFullName = vet.getFirstName() + " " + vet.getLastName();

            table.addCell(createTableCell(vetFullName)); // Add veterinarian's full name to the table

            table.addCell(createTableCell(history.getNotes()));
        }

        return table;
    }

    private PdfPTable createPhysicalExamsTable(List<PhysicalExam> physicalExams) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table Headers
        addTableHeader(table, "Exam Date", "Veterinarian", "Notes");

        // Table Rows
        for (PhysicalExam exam : physicalExams) {
            table.addCell(createTableCell(exam.getExamDate().toString()));
            table.addCell(createTableCell(exam.getVeterinarian()));
            table.addCell(createTableCell(exam.getNotes()));
        }

        return table;
    }

    private PdfPTable createVaccinationsTable(List<Vaccination> vaccinations) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table Headers
        addTableHeader(table, "Vaccine Name", "Vaccination Date", "Administered By", "Next Due Date");

        // Table Rows
        for (Vaccination vaccination : vaccinations) {
            table.addCell(createTableCell(vaccination.getVaccineName()));
            table.addCell(createTableCell(vaccination.getVaccinationDate().toString()));
            table.addCell(createTableCell(vaccination.getAdministeredBy()));
            table.addCell(createTableCell(vaccination.getNextDueDate().toString()));
        }

        return table;
    }

    private PdfPTable createTreatmentPlansTable(List<TreatmentPlan> treatmentPlans) {
        PdfPTable table = new PdfPTable(4); // Adjust columns as needed
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table Headers
        addTableHeader(table, "Plan Date", "Treatment Description", "Practitioner", "Notes");

        // Table Rows
        for (TreatmentPlan plan : treatmentPlans) {
            table.addCell(createTableCell(plan.getPlanDate().toString()));
            table.addCell(createTableCell(plan.getDescription()));
            table.addCell(createTableCell(plan.getPractitioner()));
            table.addCell(createTableCell(plan.getNotes()));
        }

        return table;
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, new Font(Font.HELVETICA, 12, Font.BOLD, new Color(30, 96, 191))));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }
    }

    private PdfPCell createTableCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 12)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private PdfPTable createWeightRecordsTable(List<WeightRecord> weightRecords) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table Headers
        addTableHeader(table, "Date", "Weight (kg)");

        // Table Rows
        for (WeightRecord record : weightRecords) {
            table.addCell(createTableCell(record.getRecordDate().toString()));
            table.addCell(createTableCell(String.valueOf(record.getWeight())));
        }

        return table;
    }

    public ByteArrayInputStream generateXML(Pet pet, List<MedicalHistory> medicalHistory,
                                            List<PhysicalExam> physicalExams, List<Vaccination> vaccinations,
                                            List<TreatmentPlan> treatmentPlans, List<WeightRecord> weightRecords,
                                            List<String> sections) {
        StringBuilder xml = new StringBuilder();
        xml.append("<MedicalRecords>");
        xml.append("<Pet name=\"").append(pet.getName()).append("\">");

        // Weight Records Section
        if (sections.contains("weightRecords")) {
            xml.append("<WeightRecords>");
            for (WeightRecord record : weightRecords) {
                xml.append("<Record date=\"").append(record.getRecordDate()).append("\">")
                        .append("<Weight>").append(record.getWeight()).append("</Weight>")
                        .append("</Record>");
            }
            xml.append("</WeightRecords>");
        }

        // Full Medical History Section
        if (sections.contains("full")) {
            xml.append("<MedicalHistory>");
            for (MedicalHistory history : medicalHistory) {
                xml.append("<Event date=\"").append(history.getEventDate()).append("\">")
                        .append("<Treatment>").append(history.getTreatment()).append("</Treatment>")
                        .append("<Practitioner>").append(history.getPractitioner()).append("</Practitioner>")
                        .append("<Veterinarian>").append(history.getVeterinarian()).append("</Veterinarian>")
                        .append("<Notes>").append(history.getNotes()).append("</Notes>")
                        .append("<Prescription>").append(history.getPrescriptionId() != null ? "Yes" : "No").append("</Prescription>")
                        .append("</Event>");
            }
            xml.append("</MedicalHistory>");
        }

        // Physical Exams Section
        if (sections.contains("physicalExams")) {
            xml.append("<PhysicalExams>");
            for (PhysicalExam exam : physicalExams) {
                xml.append("<Exam date=\"").append(exam.getExamDate()).append("\">")
                        .append("<Veterinarian>").append(exam.getVeterinarian()).append("</Veterinarian>")
                        .append("<Notes>").append(exam.getNotes()).append("</Notes>")
                        .append("</Exam>");
            }
            xml.append("</PhysicalExams>");
        }

        // Vaccination Records Section
        if (sections.contains("vaccinations")) {
            xml.append("<Vaccinations>");
            for (Vaccination vaccination : vaccinations) {
                xml.append("<Vaccine name=\"").append(vaccination.getVaccineName()).append("\" date=\"")
                        .append(vaccination.getVaccinationDate()).append("\" administeredBy=\"")
                        .append(vaccination.getAdministeredBy()).append("\" nextDueDate=\"")
                        .append(vaccination.getNextDueDate()).append("\"/>");
            }
            xml.append("</Vaccinations>");
        }

        // Treatment Plans Section
        if (sections.contains("treatmentPlans")) {
            xml.append("<TreatmentPlans>");
            for (TreatmentPlan plan : treatmentPlans) {
                xml.append("<Plan date=\"").append(plan.getPlanDate()).append("\">")
                        .append("<Description>").append(plan.getDescription()).append("</Description>")
                        .append("<Practitioner>").append(plan.getPractitioner()).append("</Practitioner>")
                        .append("<Notes>").append(plan.getNotes()).append("</Notes>")
                        .append("</Plan>");
            }
            xml.append("</TreatmentPlans>");
        }

        xml.append("</Pet>");
        xml.append("</MedicalRecords>");

        return new ByteArrayInputStream(xml.toString().getBytes());
    }
}
