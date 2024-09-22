package au.edu.rmit.sept.webapp.model;

import java.util.List;

public class MedicalRecordsResponse {
    private Pet selectedPet;
    private List<MedicalHistory> medicalHistoryList;
    private List<PhysicalExam> physicalExamList;
    private List<Vaccination> vaccinationList;
    private List<WeightRecord> weightRecords;
    private List<TreatmentPlan> treatmentPlanList;

    // Constructor
    public MedicalRecordsResponse(Pet selectedPet, List<MedicalHistory> medicalHistoryList,
                                  List<PhysicalExam> physicalExamList, List<Vaccination> vaccinationList,
                                  List<WeightRecord> weightRecords, List<TreatmentPlan> treatmentPlanList) {
        this.selectedPet = selectedPet;
        this.medicalHistoryList = medicalHistoryList;
        this.physicalExamList = physicalExamList;
        this.vaccinationList = vaccinationList;
        this.weightRecords = weightRecords;
        this.treatmentPlanList = treatmentPlanList;
    }

    // Getters and setters
    public Pet getSelectedPet() {
        return selectedPet;
    }

    public void setSelectedPet(Pet selectedPet) {
        this.selectedPet = selectedPet;
    }

    public List<MedicalHistory> getMedicalHistoryList() {
        return medicalHistoryList;
    }

    public void setMedicalHistoryList(List<MedicalHistory> medicalHistoryList) {
        this.medicalHistoryList = medicalHistoryList;
    }

    public List<PhysicalExam> getPhysicalExamList() {
        return physicalExamList;
    }

    public void setPhysicalExamList(List<PhysicalExam> physicalExamList) {
        this.physicalExamList = physicalExamList;
    }

    public List<Vaccination> getVaccinationList() {
        return vaccinationList;
    }

    public void setVaccinationList(List<Vaccination> vaccinationList) {
        this.vaccinationList = vaccinationList;
    }

    public List<WeightRecord> getWeightRecords() {
        return weightRecords;
    }

    public void setWeightRecords(List<WeightRecord> weightRecords) {
        this.weightRecords = weightRecords;
    }

    public List<TreatmentPlan> getTreatmentPlanList() {
        return treatmentPlanList;
    }

    public void setTreatmentPlanList(List<TreatmentPlan> treatmentPlanList) {
        this.treatmentPlanList = treatmentPlanList;
    }
}
