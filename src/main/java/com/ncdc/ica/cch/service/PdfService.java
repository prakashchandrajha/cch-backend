package com.ncdc.ica.cch.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.ncdc.ica.cch.entity.NominationForm;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(NominationForm form) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // =========================
            // TITLE
            // =========================
            document.add(new Paragraph("ICA Cooperative Cultural Heritage Nomination")
                    .setBold()
                    .setFontSize(16)
                    .setMarginBottom(10));

            // =========================
            // SECTION A
            // =========================
            addSectionTitle(document, "Section A: Nominating ICA Member Organization");
            addField(document, "Organization Name", form.getOrganizationName());
            addField(document, "Country", form.getCountry());
            addField(document, "Contact Person", form.getContactPerson());
            addField(document, "Position", form.getPosition());
            addField(document, "Email", form.getEmail());
            addField(document, "Telephone", form.getTelephone());

            // =========================
            // SECTION B
            // =========================
            addSectionTitle(document, "Section B: Nomination Details");
            addField(document, "Official Name", form.getOfficialName());
            addField(document, "Local Name", form.getLocalName());
            addField(document, "Other Names", form.getOtherNames());

            // =========================
            // SECTION C
            // =========================
            addSectionTitle(document, "Section C: Category of Heritage");
            addField(document, "Tangible Heritage", form.getTangible() != null && form.getTangible() ? "Yes" : "No");
            addField(document, "Intangible Heritage", form.getIntangible() != null && form.getIntangible() ? "Yes" : "No");

            // =========================
            // SECTION D
            // =========================
            addSectionTitle(document, "Section D: Communities and Groups Concerned");
            addParagraph(document, form.getCommunities());

            // =========================
            // SECTION E
            // =========================
            addSectionTitle(document, "Section E: Geographic Scope and Location");
            addParagraph(document, form.getGeographicScope());

            // =========================
            // SECTION F
            // =========================
            addSectionTitle(document, "Section F: Description of the Heritage Element");
            addParagraph(document, form.getDescription());

            // =========================
            // SECTION G
            // =========================
            addSectionTitle(document, "Section G: Holders and Practitioners");
            addParagraph(document, form.getHolders());

            // =========================
            // SECTION H
            // =========================
            addSectionTitle(document, "Section H: Knowledge Transmission");
            addParagraph(document, form.getKnowledgeTransmission());

            // =========================
            // SECTION I
            // =========================
            addSectionTitle(document, "Section I: Social and Cultural Functions");
            addParagraph(document, form.getSocialFunctions());

            // =========================
            // SECTION J
            // =========================
            addSectionTitle(document, "Section J: Human Rights and Sustainability");
            addParagraph(document, form.getHumanRights());

            // =========================
            // SECTION K
            // =========================
            addSectionTitle(document, "Section K: Safeguarding Measures");
            addSubSectionTitle(document, "K.1 Past and Ongoing Efforts");
            addParagraph(document, form.getSafeguardingPast());
            addSubSectionTitle(document, "K.2 Proposed Measures for the Future");
            addParagraph(document, form.getSafeguardingFuture());
            addSubSectionTitle(document, "K.3 Community Involvement in Safeguarding");
            addParagraph(document, form.getSafeguardingCommunity());

            // =========================
            // SECTION L
            // =========================
            addSectionTitle(document, "Section L: Contribution to Visibility and Dialogue");
            addSubSectionTitle(document, "L.1 Visibility of Cooperative Cultural Heritage");
            addParagraph(document, form.getVisibilityCooperative());
            addSubSectionTitle(document, "L.2 Promotion of Dialogue Between Communities");
            addParagraph(document, form.getDialogueBetweenCommunities());
            addSubSectionTitle(document, "L.3 Respect for Diversity and Creativity");
            addParagraph(document, form.getRespectDiversity());

            // =========================
            // SECTION M
            // =========================
            addSectionTitle(document, "Section M: Alignment with ICA CCH Standards (13 Point Criteria)");
            StringBuilder criteria = new StringBuilder();
            if (form.getCriterion1() != null && form.getCriterion1()) criteria.append("1. Historical Significance, ");
            if (form.getCriterion2() != null && form.getCriterion2()) criteria.append("2. Connection to Cooperative Pioneers, ");
            if (form.getCriterion3() != null && form.getCriterion3()) criteria.append("3. Archival/Architectural Value, ");
            if (form.getCriterion4() != null && form.getCriterion4()) criteria.append("4. Active Cooperative Connection, ");
            if (form.getCriterion5() != null && form.getCriterion5()) criteria.append("5. Living Practice of Cooperation, ");
            if (form.getCriterion6() != null && form.getCriterion6()) criteria.append("6. Educational Mission, ");
            if (form.getCriterion7() != null && form.getCriterion7()) criteria.append("7. Research and Knowledge Sharing, ");
            if (form.getCriterion8() != null && form.getCriterion8()) criteria.append("8. Community Engagement, ");
            if (form.getCriterion9() != null && form.getCriterion9()) criteria.append("9. Accessibility for All, ");
            if (form.getCriterion10() != null && form.getCriterion10()) criteria.append("10. Preservation and Safeguarding, ");
            if (form.getCriterion11() != null && form.getCriterion11()) criteria.append("11. Visibility and Symbolism, ");
            if (form.getCriterion12() != null && form.getCriterion12()) criteria.append("12. Sustainability and Inclusivity, ");
            if (form.getCriterion13() != null && form.getCriterion13()) criteria.append("13. Networking and International Solidarity, ");
            
            addField(document, "Selected Criteria", criteria.length() > 0 ? criteria.substring(0, criteria.length() - 2) : "None");
            addSubSectionTitle(document, "Explanation");
            addParagraph(document, form.getCriteriaExplanation());

            // =========================
            // SECTION N
            // =========================
            addSectionTitle(document, "Section N: Consent and Participation");
            addParagraph(document, form.getConsentParticipation());

            // =========================
            // SECTION O
            // =========================
            addSectionTitle(document, "Section O: Documentation and Inventories");
            addParagraph(document, form.getDocumentationInventories());

            // =========================
            // SECTION P
            // =========================
            addSectionTitle(document, "Section P: Supporting Materials");
            addField(document, "Video Documentation Included", form.getVideo() != null && form.getVideo() ? "Yes" : "No");

            // =========================
            // SECTION Q
            // =========================
            addSectionTitle(document, "Section Q: Declaration and Signature");
            addField(document, "Name", form.getDeclarantName());
            addField(document, "Designation", form.getDeclarantDesignation());
            addField(document, "Organization", form.getDeclarantOrganization());
            addField(document, "Date", safe(form.getDeclarationDate() != null ? form.getDeclarationDate().toString() : null));

            // =========================
            // FOOTER
            // =========================
            document.add(new Paragraph("\nSubmitted on: " + safe(form.getSubmissionDate() != null
                    ? form.getSubmissionDate().toString()
                    : null))
                    .setMarginTop(10));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    // =========================
    // HELPER METHODS
    // =========================

    private void addSectionTitle(Document document, String title) {
        document.add(new Paragraph(title)
                .setBold()
                .setFontSize(12)
                .setMarginTop(10)
                .setMarginBottom(5));
    }

    private void addSubSectionTitle(Document document, String title) {
        document.add(new Paragraph(title)
                .setBold()
                .setFontSize(10)
                .setMarginTop(5)
                .setMarginBottom(3));
    }

    private void addField(Document document, String label, String value) {
        document.add(new Paragraph(label + ": " + safe(value))
                .setFontSize(9)
                .setMarginBottom(3));
    }

    private void addParagraph(Document document, String value) {
        document.add(new Paragraph(safe(value))
                .setFontSize(9)
                .setMarginBottom(5));
    }

    private String safe(String value) {
        return value != null ? value : "-";
    }
}