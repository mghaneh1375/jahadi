package four.group.jahadi.Service.Area.pdfExport;

import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Area.PatientAnswer;
import four.group.jahadi.Models.Area.PatientForm;
import four.group.jahadi.Models.Area.PatientReferral;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfReportService {

    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final PatientReportHtmlBuilder patientReportHtmlBuilder;
    private final PhpPdfConverter phpPdfConverter;

    public byte[] generatePdf(
            Patient patient,
            List<PatientsInArea> patientForms
    ) {
        Map<ObjectId, List<PatientReferral>> referralsByModule = patientForms.stream()
                .flatMap(pf -> pf.getReferrals().stream())
                .filter(r -> r.getModuleId() != null)
                .collect(Collectors.groupingBy(PatientReferral::getModuleId));

        List<Module> modules = moduleRepository.findAllById(
                new ArrayList<>(referralsByModule.keySet())
        );

        PatientHeader header = PatientHeader.builder()
                .name(patient.getName())
                .fileCode(String.valueOf(patient.getPatientNo()))
                .nationalCode(patient.getIdentifier())
                .genderAge(buildGenderAge(patient))
                .build();

        List<ModuleSection> sections = new ArrayList<>();
        for (Module module : modules) {
            ModuleSection sec = renderModule(module, referralsByModule.get(module.getId()));
            if (sec != null && sec.getSubModules() != null && !sec.getSubModules().isEmpty()) {
                sections.add(sec);
            }
        }

        PdfReportModel model = PdfReportModel.builder()
                .patient(header)
                .modules(sections)
                .build();

        String html = patientReportHtmlBuilder.build(model);
        return phpPdfConverter.convertHtmlToPdf(html);
    }

    private ModuleSection renderModule(Module module, List<PatientReferral> referrals) {
        if (referrals == null) referrals = Collections.emptyList();

        Map<ObjectId, List<FormWithRef>> formsBySubModule = new HashMap<>();
        for (PatientReferral ref : referrals) {
            if (ref.getForms() == null) continue;
            for (PatientForm form : ref.getForms()) {
                formsBySubModule
                        .computeIfAbsent(form.getSubModuleId(), k -> new ArrayList<>())
                        .add(new FormWithRef(form, ref));
            }
        }

        Set<ObjectId> doctorIds = new HashSet<>();
        for (List<FormWithRef> list : formsBySubModule.values()) {
            for (FormWithRef fw : list) {
                if (fw.form.getDoctorId() != null) doctorIds.add(fw.form.getDoctorId());
            }
        }
        Map<ObjectId, String> doctorNames = new HashMap<>();
        if (!doctorIds.isEmpty()) {
            List<User> doctors = userRepository.findJustNameByIdsIn(new ArrayList<>(doctorIds));
            for (User u : doctors) {
                doctorNames.put(u.getId(), u.getName());
            }
        }

        List<SubModuleSection> subSections = new ArrayList<>();
        for (SubModule sm : module.getSubModules()) {
            List<FormWithRef> forms = formsBySubModule.getOrDefault(sm.getId(), Collections.emptyList());

            // مرتب‌سازی بر اساس created_at
            forms.sort(Comparator.comparing(fw -> fw.form.getCreatedAt(),
                    Comparator.nullsLast(Comparator.naturalOrder())));

            List<VisitBlock> visits = new ArrayList<>();
            for (FormWithRef fw : forms) {
                VisitBlock vb = renderVisit(sm, fw.form, fw.ref, doctorNames);
                if (vb != null && vb.getQuestions() != null && !vb.getQuestions().isEmpty()) {
                    visits.add(vb);
                }
            }
            if (!visits.isEmpty()) {
                subSections.add(SubModuleSection.builder()
                        .title(sm.getName())
                        .visits(visits)
                        .build());
            }
        }

        return ModuleSection.builder()
                .title(module.getName())
                .subModules(subSections)
                .build();
    }

    private VisitBlock renderVisit(SubModule sm,
                                   PatientForm form,
                                   PatientReferral ref,
                                   Map<ObjectId, String> doctorNames) {
        Map<ObjectId, String> answers = new HashMap<>();
        if (form.getAnswers() != null) {
            for (PatientAnswer a : form.getAnswers()) {
                if (a.getAnswer() != null) {
                    answers.put(a.getQuestionId(), a.getAnswer().toString());
                }
            }
        }

        List<RenderedQuestion> rendered = new ArrayList<>();
        for (Question q : sm.getQuestions()) {
            RenderedQuestion rq = renderQuestion(q, answers);
            if (rq != null) rendered.add(rq);
        }

        String doctor = doctorNames.getOrDefault(form.getDoctorId(), "-");
        String time = Utility.convertUTCDateToJalali(
                form.getCreatedAt()
        );

        return VisitBlock.builder()
                .doctorName(doctor)
                .receptionTime(time)
                .questions(rendered)
                .build();
    }

    private RenderedQuestion renderQuestion(Question q, Map<ObjectId, String> answers) {
        QuestionType type = q.getQuestionType();
        if (type == null) return null;

        switch (type) {
            case SIMPLE: {
                String ans = answers.get(q.getId());
                if (ans == null) return null;
                SimpleQuestion sq = (SimpleQuestion) q;
                return new SimpleRendered(
                        sq.getQuestion(),
                        humanizeAnswer(sq, ans)
                );
            }
            case CHECK_LIST: {
                if (!(q instanceof CheckListGroupQuestion)) return null;
                CheckListGroupQuestion cl = (CheckListGroupQuestion) q;

                Map<String, String> optMap = new HashMap<>();
                if (cl.getOptions() != null) {
                    for (PairValue p : cl.getOptions()) {
                        optMap.put(p.getKey().toString(), p.getValue().toString());
                    }
                }

                List<ChecklistRow> rows = new ArrayList<>();
                if (cl.getQuestions() != null) {
                    for (Question inner : cl.getQuestions()) {
                        String ans = answers.get(inner.getId());
                        if (ans == null) continue;
                        String serviceName = (inner instanceof SimpleQuestion)
                                ? ((SimpleQuestion) inner).getQuestion()
                                : "" /*inner.getQuestion()*/;
                        String status = optMap.getOrDefault(ans, ans);
                        rows.add(new ChecklistRow(serviceName, status, ""));
                    }
                }
                if (rows.isEmpty()) return null;

                return new ChecklistRendered(
                        cl.getSectionTitle(),
                        Arrays.asList("خدمت", "وضعیت", "توضیحات"),
                        rows
                );
            }
            case TABLE: {
                // بسته به ساختار TableQuestion ات پیاده کن
                return null;
            }
            case GROUP: {
                if (!(q instanceof GroupQuestion)) return null;
                GroupQuestion g = (GroupQuestion) q;
                List<RenderedQuestion> children = new ArrayList<>();
                if (g.getQuestions() != null) {
                    for (Question inner : g.getQuestions()) {
                        RenderedQuestion r = renderQuestion(inner, answers);
                        if (r != null) children.add(r);
                    }
                }
                if (children.isEmpty()) return null;
                return new GroupRendered(g.getSectionTitle(), children);
            }
            default:
                return null;
        }
    }

    private String humanizeAnswer(SimpleQuestion q, String raw) {
        // اگه answer_type از جنس key/value داره (مثل بله/خیر با کد)
        // اینجا نگاشت کن. فعلاً خود مقدار خام.
        return raw;
    }

    private String buildGenderAge(Patient p) {
        String gender = p.getSex().getFaTranslate();
        String age = p.getAgeType().getFaTranslate();
        return gender + " - " + age;
    }

    @RequiredArgsConstructor
    private static class FormWithRef {
        final PatientForm form;
        final PatientReferral ref;
    }
}